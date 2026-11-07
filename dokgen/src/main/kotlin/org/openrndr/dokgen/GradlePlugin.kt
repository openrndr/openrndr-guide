package org.openrndr.dokgen

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import org.gradle.work.DisableCachingByDefault
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import java.io.File
import java.io.Serializable
import java.security.MessageDigest
import javax.inject.Inject

private val Project.buildDirFile: File
    get() = layout.buildDirectory.get().asFile

const val PLUGIN_NAME = "dokgen"
const val DOCS_DIR = "src/main/kotlin/docs"

class DokGenException(message: String) : Exception("$PLUGIN_NAME exception: $message")

fun generatedExamplesDirectory(buildDir: File): File {
    return File(buildDir, "$PLUGIN_NAME/generated/examples")
}

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

    open class ZolaConf : Serializable {
        /** Folders copied into the root of the Zola site (config.toml, templates, static) */
        var assets: List<File>? = null
        /** Folders whose files are copied into the site's static/media folder */
        var media: List<File>? = null
    }

    var examplesConf: ExamplesConf = objectFactory.newInstance(ExamplesConf::class.java)
    var runnerConf: RunnerConf = objectFactory.newInstance(RunnerConf::class.java)
    var zolaConf: ZolaConf = objectFactory.newInstance(ZolaConf::class.java)

    fun runner(action: Action<RunnerConf>) {
        action.execute(runnerConf)
    }

    fun examples(action: Action<ExamplesConf>) {
        action.execute(examplesConf)
    }

    fun zola(action: Action<ZolaConf>) {
        action.execute(zolaConf)
    }
}

@CacheableTask
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
    var mdOutputDir: File = File(project.buildDirFile, "$PLUGIN_NAME/md")

    @OutputDirectory
    var examplesOutputDir: File = generatedExamplesDirectory(project.buildDirFile)

    @OutputDirectory
    var examplesForExportOutputDir: File = File(project.buildDirFile, "$PLUGIN_NAME/generated/examples-export")


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

/**
 * Runs the generated example programs which produce media (screenshots, videos).
 *
 * Deliberately not an `@Incremental` task: Gradle runs incremental tasks
 * non-incrementally (reporting every file as changed, and deleting the
 * previous outputs) whenever the task implementation changes, e.g. after
 * editing dokgen or the build scripts. Instead, after a program runs
 * successfully, a stamp holding the hash of its source is written to
 * [stampsDirectory]. A program runs again only if its source differs from
 * the stamp. Use `--rerun-tasks` or `-Pdokgen.rerunAll` to run all of them.
 */
@DisableCachingByDefault(because = "Runs programs; tracks which ones need to run again itself")
abstract class RunExamplesTask @Inject constructor(
    @Input
    val runnerConf: DokGenPluginExtension.RunnerConf?
) : DefaultTask() {
    init {
        group = PLUGIN_NAME
        description = "Run the exported example programs to produce media files"
    }

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputDirectory
    val examplesDirectory: DirectoryProperty = project.objects.directoryProperty().also {
        it.set(generatedExamplesDirectory(project.buildDirFile))
    }

    // Paths of exported media are specified in the .kt src files themselves,
    // relative to the project directory
    @OutputDirectory
    val outputDirectory: File = project.file("media")

    /** Kept next to the media, so deleting the media also forgets the stamps */
    @Internal
    val stampsDirectory: File = File(outputDirectory, MEDIA_STAMPS_DIR)

    /**
     * An input, so a normal run after a `skipMediaGeneration=true` run
     * is not considered up-to-date
     */
    @get:Input
    val skipMediaGeneration: Provider<Boolean> = project.providers
        .environmentVariable("skipMediaGeneration").map { it == "true" }.orElse(false)

    @Internal
    val rerunAll: Boolean = project.gradle.startParameter.isRerunTasks ||
            project.hasProperty("dokgen.rerunAll")

    @TaskAction
    fun execute() {
        val skip = skipMediaGeneration.get()
        println("`skipMediaGeneration` environment variable = $skip")
        if (skip) {
            return
        }

        val examplesRoot = examplesDirectory.get().asFile
        val sources = examplesRoot.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        val mainClasses = DokGen.getExamplesClassNames(sources, examplesRoot)

        stampsDirectory.mkdirs()
        // Forget programs which no longer exist
        val existing = mainClasses.toSet()
        stampsDirectory.listFiles()?.filter { it.name !in existing }?.forEach { it.delete() }

        val toRun = sources.zip(mainClasses).mapNotNull { (source, mainClass) ->
            val hash = sha256(source)
            val stamp = File(stampsDirectory, mainClass)
            if (!rerunAll && stamp.isFile && stamp.readText() == hash) null
            else Triple(source, mainClass, hash)
        }
        println("Running ${toRun.size} of ${sources.size} example programs" +
                if (rerunAll) " (rerun all requested)" else "")

        val sourceSetContainer = project.property("sourceSets") as SourceSetContainer
        val ss = sourceSetContainer.getByName("GeneratedExamples")

        val workQueue: WorkQueue = workerExecutor.noIsolation()

        for ((source, mainClass, hash) in toRun) {
            println("Run: ${source.relativeTo(examplesRoot)}")
            // Invalidate first, so a failed run is retried next time
            File(stampsDirectory, mainClass).delete()
            workQueue.submit(MediaRunnerWorkAction::class.java) {
                it.classPath.set(ss.runtimeClasspath.asPath)
                it.jvmArgs.set(runnerConf?.jvmArgs ?: emptyList())
                it.mainClass.set(mainClass)
                it.stampFile.set(File(stampsDirectory, mainClass))
                it.sourceHash.set(hash)
            }
        }
    }

    private fun sha256(file: File): String =
        MessageDigest.getInstance("SHA-256").digest(file.readBytes())
            .joinToString("") { "%02x".format(it) }
}

/** Folder inside the media folder holding the [RunExamplesTask] stamps */
const val MEDIA_STAMPS_DIR = ".dokgen-stamps"

/**
 * Assembles a Zola site in build/dokgen/zola:
 * - the site assets (config.toml, templates, static files),
 * - the media files into static/media,
 * - the Markdown files generated from the .kt sources into content/.
 */
@CacheableTask
open class ZolaTask @Inject constructor(
    private val zolaConf: DokGenPluginExtension.ZolaConf?
) : DefaultTask() {
    init {
        group = PLUGIN_NAME
        description = "Assembles the Zola site in build/dokgen/zola"
    }

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputDirectory
    val dokgenMdDir = File(project.buildDirFile, "$PLUGIN_NAME/md")

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFiles
    val assetFiles: FileCollection = project.files(zolaConf?.assets ?: emptyList<File>())

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFiles
    val mediaFiles: FileCollection = project.files(zolaConf?.media ?: emptyList<File>())

    @OutputDirectory
    val siteDir = zolaSiteDirectory(project.buildDirFile)

    @TaskAction
    fun run() {
        zolaConf?.assets?.let { assets ->
            // Mirror the assets, so files deleted or renamed there disappear.
            // Keep what is generated by the steps below and by Zola.
            project.sync { spec ->
                spec.from(assets)
                spec.into(siteDir)
                spec.preserve { p ->
                    p.include("content/**", "static/media/**", "public/**")
                }
            }
        }
        zolaConf?.media?.let { mediaDirectories ->
            project.copy { spec ->
                spec.from(mediaDirectories)
                spec.exclude("$MEDIA_STAMPS_DIR/**")
                spec.into(File(siteDir, "static/media"))
                // If a file exists in several media folders, the last one wins
                spec.duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
        // Mirror the generated Markdown, removing pages that no longer exist
        project.sync { spec ->
            spec.from(dokgenMdDir)
            spec.into(File(siteDir, "content"))
        }
    }
}

fun zolaSiteDirectory(buildDir: File) = File(buildDir, "$PLUGIN_NAME/zola")

/**
 * Runs `zola serve` on the assembled site. Uses the `zola` executable found in
 * the PATH, or the one given with `-Pzola=/path/to/zola`.
 * Alternatively run `zola serve` in build/dokgen/zola from a terminal.
 */
@DisableCachingByDefault
open class ZolaServeTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @Internal
    val siteDir: File = zolaSiteDirectory(project.buildDirFile)

    @Internal
    val zolaExecutable: String = (project.findProperty("zola") as String?) ?: "zola"

    init {
        group = PLUGIN_NAME
        description = "Serves the guide at http://127.0.0.1:1111 (needs Zola, see contributing.md)"
    }

    @TaskAction
    fun run() {
        execOperations.exec { exec ->
            exec.workingDir = siteDir
            exec.executable = zolaExecutable
            exec.args("serve")
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
            gess.java.srcDir(generatedExamplesDirectory(project.buildDirFile))

            val dokGenTask = project.tasks.register(PLUGIN_NAME)

            // Next line produces this error:
            // kotlin scripting plugin: applied in the non-supported environment
            // (error received: Cannot query the value of task
            // ':compileGeneratedExamplesKotlin' property
            // 'sourceSetName$kotlin_gradle_plugin' because it has no value available.)
            // More info at https://github.com/google/ksp/pull/693
            val compileKotlinTask = project.tasks.getByPath("compileGeneratedExamplesKotlin")

            val processSources =
                project.tasks.register("processSources", ProcessSourcesTask::class.java, conf.examplesConf)

            val runExamples =
                project.tasks.register("runExamples", RunExamplesTask::class.java, conf.runnerConf)

            compileKotlinTask.dependsOn(processSources)
            runExamples.configure { it.dependsOn(compileKotlinTask) }

            val zolaTask = project.tasks.register("zola", ZolaTask::class.java, conf.zolaConf)

            dokGenTask.configure {
                it.group = PLUGIN_NAME
                it.description = "Main task which runs other tasks"
                it.dependsOn(runExamples)
                it.finalizedBy(zolaTask)
            }

            zolaTask.configure { it.dependsOn(dokGenTask) }

            val zolaServeTask = project.tasks.register("zolaServe", ZolaServeTask::class.java)
            zolaServeTask.configure { it.dependsOn(zolaTask) }
        }
    }
}
