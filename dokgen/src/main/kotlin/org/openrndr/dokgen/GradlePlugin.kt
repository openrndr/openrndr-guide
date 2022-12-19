package org.openrndr.dokgen


import org.gradle.api.*
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.*
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.io.Serializable
import javax.inject.Inject

const val PLUGIN_NAME = "dokgen"
const val DOCS_DIR = "src/main/kotlin/docs"

class DokGenException(message: String) : Exception("$PLUGIN_NAME exception: $message")

fun generatedExamplesDirectory(project: Project): File {
    return File(project.buildDir, "$PLUGIN_NAME/generated/examples")
}

fun generatedExamplesForExportDirectory(project: Project): File {
    return File(project.buildDir, "$PLUGIN_NAME/generated/examples-export")
}

fun DefaultTask.getResourcePath(name: String) =
    javaClass.classLoader.getResource(name)!!.path.split('!')[0]

// this class provides the configuration dsl
// the inner classes represent configurable data
// the methods represent closures in the dsl through which configuration can be set
open class DokGenPluginExtension @Inject constructor(objectFactory: ObjectFactory) : Serializable {
    open class ExamplesConf : Serializable {
        var webRootUrl: String? = null
    }

    open class RunnerConf : Serializable {
        var jvmArgs = mutableListOf<String>()
    }

    open class JekyllConf : Serializable {
        var assets: List<File>? = null
        var media: List<File>? = null
    }

    var examplesConf: ExamplesConf? = objectFactory.newInstance(ExamplesConf::class.java)
    var runnerConf: RunnerConf? = objectFactory.newInstance(RunnerConf::class.java)
    var jekyllConf: JekyllConf? = objectFactory.newInstance(JekyllConf::class.java)

    fun runner(action: Action<RunnerConf>) {
        runnerConf?.let {
            action.execute(it)
        }
    }

    fun examples(action: Action<ExamplesConf>) {
        examplesConf?.let {
            action.execute(it)
        }
    }

    fun jekyll(action: Action<JekyllConf>) {
        jekyllConf?.let {
            action.execute(it)
        }
    }
}

abstract class ProcessSourcesTask @Inject constructor(
    @Input
    val examplesConf: DokGenPluginExtension.ExamplesConf?
) : DefaultTask() {
    init {
        group = PLUGIN_NAME
        description = "Process src folder, generate markdown and .kt examples"
    }

    @get:Incremental
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputDirectory
    val inputDir: DirectoryProperty = project.objects.directoryProperty().also {
        it.set(File(project.projectDir, DOCS_DIR))
    }

    @OutputDirectory
    var mdOutputDir: File = File(project.buildDir, "$PLUGIN_NAME/md")

    @OutputDirectory
    var examplesOutputDir: File = generatedExamplesDirectory(project)

    @OutputDirectory
    var examplesForExportOutputDir: File = generatedExamplesForExportDirectory(project)


    @TaskAction
    fun run(inputChanges: InputChanges) {
        println(
            if (inputChanges.isIncremental) "Executing incrementally"
            else "Executing non-incrementally"
        )
        val toProcess = mutableListOf<File>()
        for (change in inputChanges.getFileChanges(inputDir)) {
            println("${change.changeType}: ${change.normalizedPath}")
            toProcess.add(change.file)
        }

        DokGen.processSources(
            toProcess,
            inputDir.asFile.get(),
            mdOutputDir,
            examplesOutputDir,
            examplesForExportOutputDir,
            webRootUrl = examplesConf?.webRootUrl
        )
    }
}

open class RunExamplesTask @Inject constructor(
    @Input
    val runnerConf: DokGenPluginExtension.RunnerConf?
) : DefaultTask() {
    init {
        group = PLUGIN_NAME
        description = "Run the exported example programs to produce media files"
    }


    @get:Incremental
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputDirectory
    val examplesDirectory: DirectoryProperty = project.objects.directoryProperty().also {
        it.set(generatedExamplesDirectory(project))
    }


    // TODO: Why this is needed?
    // Paths of exported media is specified in the .kt src files themselves
    @OutputDirectory
    val outputDirectory = File("media")

    @TaskAction
    fun execute(inputChanges: InputChanges) {
        val skipMediaGeneration = System.getenv("skipMediaGeneration") == "true"
        println("`skipMediaGeneration` environment variable = $skipMediaGeneration")
        if(skipMediaGeneration) {
            return
        }

        val toRun = mutableListOf<File>()

        for (change in inputChanges.getFileChanges(examplesDirectory)) {
            println("Find changes: $change")
            toRun.add(change.file)
        }

        val sourceSetContainer = project.property("sourceSets") as SourceSetContainer
        val ss = sourceSetContainer.getByName("GeneratedExamples")
        val execClasses = DokGen.getExamplesClassNames(
            toRun, examplesDirectory.get().asFile
        )

        for ((index, klass) in execClasses.withIndex()) {
            println("Running example [${index+1}/${execClasses.size}]: $klass")
            try {
                project.javaexec { spec ->
                    spec.classpath = ss.runtimeClasspath
                    runnerConf?.let {
                        spec.jvmArgs = it.jvmArgs
                    }
                    spec.mainClass.set(klass)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

open class JekyllTask @Inject constructor(
    private val jekyllConf: DokGenPluginExtension.JekyllConf?
) : DefaultTask() {
    init {
        group = PLUGIN_NAME
        description = "Copies files into Jekyll docs folder"
    }

    @InputDirectory
    val dokgenBuildDir = File(project.buildDir, PLUGIN_NAME)

    @InputDirectory
    val dokgenMdDir = File(dokgenBuildDir, "md")

    @OutputDirectory
    val jekyllBuildDir = File(dokgenBuildDir, "jekyll")

    @OutputDirectory
    var docsDir = File(jekyllBuildDir, "docs")

    @OutputDirectory
    var mediaOutputDirectory = File(docsDir, "media")

    @TaskAction
    fun run() {
        // A .jar file is a .zip file with specific layout
        val jar = project.zipTree(getResourcePath("jekyll"))
        project.copy { spec ->
            spec.from(jar)
            spec.into(dokgenBuildDir)
            spec.include("jekyll/**/*")
        }

        jekyllConf?.let { gradleConf ->
            // Copy and merge media directories
            gradleConf.media?.let { mediaDirectories ->
                project.copy { spec ->
                    spec.from(mediaDirectories)
                    spec.into(mediaOutputDirectory)
                }
            }
            // Copy all assets into the web root
            gradleConf.assets?.let { assets ->
                project.copy { spec ->
                    spec.from(assets)
                    spec.into(docsDir)
                }
            }
        }

        project.copy { spec ->
            spec.from(dokgenMdDir)
            spec.into(docsDir)
        }
    }

}

open class WebServerStartTask @Inject constructor() : DefaultTask() {

    @InputDirectory
    var docsDir: File = File(project.buildDir, "$PLUGIN_NAME/jekyll/docs")

    init {
        group = PLUGIN_NAME
        description = "Starts a local web server"
    }

    @TaskAction
    fun run() {
        project.exec { exec ->
            println("Please wait, downloading and starting jekyll can take one minute")
            exec.workingDir = docsDir
            exec.executable = "./webServerStart.sh"
        }
    }
}

open class WebServerStopTask @Inject constructor() : DefaultTask() {

    @InputDirectory
    var docsDir: File = File(project.buildDir, "$PLUGIN_NAME/jekyll/docs")

    init {
        group = PLUGIN_NAME
        description = "Stops local web server"
    }

    @TaskAction
    fun run() {
        project.exec { exec ->
            exec.workingDir = docsDir
            exec.executable = "./webServerStop.sh"
        }
    }
}


class GradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val conf = project.extensions.create(
            PLUGIN_NAME,
            DokGenPluginExtension::class.java,
            project.objects
        )

        project.afterEvaluate { _ ->
            val sourceSets = project.property("sourceSets") as SourceSetContainer
            val mss = sourceSets.getByName("main")

            // I think this line creates tasks
            // `compileGeneratedExamplesKotlin` and
            // `compileGeneratedExamplesJava`
            val gess = sourceSets.create("GeneratedExamples")
            gess.compileClasspath += mss.compileClasspath
            gess.runtimeClasspath += mss.runtimeClasspath
            gess.java.srcDir(generatedExamplesDirectory(project))

            val dokGenTask = project.tasks.create(PLUGIN_NAME)
            dokGenTask.group = PLUGIN_NAME
            dokGenTask.description = "Main task which runs other tasks"

            // Next line produces this error:
            // kotlin scripting plugin: applied in the non-supported environment
            // (error received: Cannot query the value of task
            // ':compileGeneratedExamplesKotlin' property
            // 'sourceSetName$kotlin_gradle_plugin' because it has no value available.)
            // More info at https://github.com/google/ksp/pull/693
            val compileKotlinTask = project.tasks.getByPath("compileGeneratedExamplesKotlin")

            (compileKotlinTask as KotlinCompile).apply {
                kotlinOptions {
                    jvmTarget = "11"
                }
            }

            val processSources =
                project.tasks.create("processSources", ProcessSourcesTask::class.java, conf.examplesConf)

            val runExamples =
                project.tasks.create("runExamples", RunExamplesTask::class.java, conf.runnerConf)

            compileKotlinTask.dependsOn(processSources)
            runExamples.dependsOn(compileKotlinTask)

            dokGenTask.dependsOn(runExamples)

            val jekyllTask = project.tasks.create("jekyll", JekyllTask::class.java, conf.jekyllConf)

            dokGenTask.finalizedBy(jekyllTask)
            jekyllTask.dependsOn(dokGenTask)

            val webServerStartTask = project.tasks.create("webServerStart", WebServerStartTask::class.java)
            webServerStartTask.dependsOn(jekyllTask)

            project.tasks.create("webServerStop", WebServerStopTask::class.java)
        }
    }
}
