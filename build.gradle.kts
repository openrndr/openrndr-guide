import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    org.openrndr.guide.convention.`kotlin-jvm`
    alias(libs.plugins.git.publish)
    alias(libs.plugins.dokgen)
    alias(libs.plugins.versions)
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.jsoup)
    implementation(libs.gson)
    implementation(libs.csv)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.bundles.openrndr.core)
    implementation(libs.bundles.openrndr.rest)
    implementation(libs.bundles.orx)
    implementation(libs.dokgen) {
        // Otherwise includes Gradle's slf4j implementation
        isTransitive = false
    }
    runtimeOnly(libs.slf4j.simple)
}

tasks {
    dependencyUpdates {
        gradleReleaseChannel = "current"

        val nonStableKeywords = listOf("alpha", "beta", "rc")

        fun isNonStable(
            version: String
        ) = nonStableKeywords.any {
            version.lowercase().contains(it)
        }

        rejectVersionIf {
            isNonStable(candidate.version) && !isNonStable(currentVersion)
        }
    }
}


dokgen {
    runner {
        if (System.getProperty("os.name") == "Mac OS X") {
            jvmArgs = mutableListOf("-XstartOnFirstThread")
        }
    }

    examples {
        webRootUrl = "https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin"
    }

    jekyll {
        media = listOf(file("$projectDir/media"), file("$projectDir/static-media"))
        assets = listOf(file("$projectDir/data/jekyll-assets"))
    }
}

task("add IDE file scopes") {
    group = " ★ OPENRNDR"
    val scopesFolder = File("${project.projectDir}/.idea/scopes")
    scopesFolder.mkdirs()

    val files = listOf(
        "Code" to "file:*.kt||file:*.frag||file:*.vert||file:*.glsl",
        "Text" to "file:*.txt||file:*.md||file:*.xml||file:*.json",
        "Gradle" to "file[*buildSrc*]:*/||file:*gradle.*||file:*.gradle||file:*/gradle-wrapper.properties||file:*.toml",
        "Media" to "file:*.png||file:*.jpg||file:*.dds||file:*.exr||file:*.mp3||file:*.wav||file:*.mp4||file:*.mov||file:*.svg"
    )
    files.forEach { (name, pattern) ->
        val file = File(scopesFolder, "__$name.xml")
        if (!file.exists()) {
            file.writeText(
                """
                    <component name="DependencyValidationManager">
                      <scope name=" ★ $name" pattern="$pattern" />
                    </component>
                    """.trimIndent()
            )
        }
    }
}

/**
 * Helper class for reading and updating the annotations in
 * openrndr-guide .kt files.
 *
 * The class reads the passed `ktFile` and puts all the
 * `@file` annotations into a mutable map. The map can be
 * updated using the update method, like this:
 *
 * ```
 * val af = AnnotatedFile(File("95_Use_cases/index.kt"))
 * af.update(indexFiles, dryRun = true) // saves if dryRun is false
 * ```
 *
 * `indexFiles` is a Map<File, AnnotatedFile> containing all
 * index.kt files. These are used to extract the Title annotation
 * of index.kt files, which must be referred to as ParentTitle
 * in other files in the same folder.
 *
 * Thanks to this helper class and Gradle task, one only needs to
 * set the file names and Title annotations.
 * Suppress, ParentTitle, Order and URL are set automatically.
 *
 * Tip: to customize the URL, change the file and folder name.
 */
class AnnotatedFile(val ktFile: File) {
    private val folder = ktFile.parentFile
    private val lines = ktFile.readLines()
    private val contentStartIndex = lines.indexOfFirst {
        !it.startsWith("@file:")
    }.coerceAtLeast(0)
    private val annotationsLines = lines.take(contentStartIndex)

    private val rxAnnotation = Regex("""@file:(\w+)\("(.+?)"\)""")
    private val rxFolderName = Regex("""kotlin\/docs\/(.+?)\/""")

    private val annotations = annotationsLines.associate { line ->
        val (k, v) = rxAnnotation.find(line)!!.destructured
        k to v
    }.toMutableMap()

    private fun getUpdatedFileContent(): String {
        val updatedAnnotations = annotations.map {
            """@file:${it.key}("${it.value}")"""
        }.joinToString("\n", postfix = "\n")

        // Add annotations import if missing. This allows creating a new empty .kt file
        // and this Gradle task would add the generated annotations and the required import.
        val mutableLines = lines.toMutableList()
        if(lines.none { it.contains("import org.openrndr.dokgen.annotations") }) {
            val packageLineNum = mutableLines.indexOfFirst { it.startsWith("package") }
            if(packageLineNum >= 0) {
                mutableLines.addAll(listOf("import org.openrndr.dokgen.annotations.*"))
            }
        }

        return updatedAnnotations + mutableLines.drop(contentStartIndex).joinToString("\n")
    }

    fun getAnnotation(k: String) = annotations[k]

    fun isIndexFile() = ktFile.name == "index.kt"

    private fun File.createTempFile(suffix: String): File {
        val temp = Files.createTempFile(
            parentFile.toPath(),
            "${nameWithoutExtension}_temp", suffix
        ).toFile()
        temp.deleteOnExit()
        return temp
    }

    private fun List<String>.toCamelCase(): String {
        val parts = this.toMutableList()
        var nameCamel = parts.drop(1).joinToString(
            "", transform = { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
        )
        // Make first character case match second character case
        // For cases like OSC or SVG.
        if (nameCamel.isNotEmpty() && nameCamel[1].isLowerCase()) {
            nameCamel = nameCamel.replaceFirstChar { it.lowercaseChar() }
        }
        return nameCamel
    }

    fun update(indexFiles: Map<File, AnnotatedFile>, dryRun: Boolean) {
        val originalAnnotations = annotations.toMap()

        val result = rxFolderName.find(ktFile.absolutePath)?.groups
        val folderName = result?.get(1)?.value ?: ""
        val folderParts = folderName.split('_', '-')
        val folderNameCamel = folderParts.toCamelCase()

        annotations["Suppress"] = "UNUSED_EXPRESSION"

        if (!annotations.containsKey("Title")) {
            annotations["Title"] = "Untitled ${System.currentTimeMillis()}"
        }

        if (ktFile.name == "index.kt") {
            annotations["Order"] = if (folderParts.size == 1)
                "0"
            else
                (folderParts.first().toInt() + 1000).toString()

            annotations.remove("ParentTitle")

            annotations["URL"] = "$folderNameCamel/index"
        } else {
            annotations["Order"] = ktFile.name.split('_', '-')[0].substring(1)

            indexFiles[folder]?.getAnnotation("Title")?.let {
                annotations["ParentTitle"] = it
            }
            val fileNameParts = ktFile.nameWithoutExtension.split('_', '-')
            val fileNameCamel = fileNameParts.toCamelCase()
            annotations["URL"] = "$folderNameCamel/$fileNameCamel"
        }

        if (annotations != originalAnnotations) {
            println("\nUpdating $folderName/${ktFile.name}. Annotations changed:")
            println(originalAnnotations)
            println(annotations)
            if (!dryRun) {
                val tempFile = ktFile.createTempFile(".kt")
                tempFile.writeText(getUpdatedFileContent())

                // Atomic replacement of original file
                Files.move(
                    Paths.get(tempFile.absolutePath),
                    Paths.get(ktFile.absolutePath),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
                )
            }
        }
    }
}

task("Update just-the-docs annotations") {
    group = " ★ OPENRNDR"

    // Source directory containing Kotlin files
    val sourceDir = file("src/main/kotlin/docs")

    doFirst {
        // Collect all kotlin files and scan their annotations
        val kotlinFiles = sourceDir.walkTopDown().filter { file ->
            file.name.endsWith(".kt")
        }.map { file ->
            AnnotatedFile(file)
        }

        // Scan for repeated titles and throw an exception if found
        val repeatedTitles = kotlinFiles.groupingBy {
            it.getAnnotation("Title")
        }.eachCount().filter {
            it.value > 1
        }.map { it.key }

        if (repeatedTitles.isNotEmpty()) {
            repeatedTitles.forEach { title ->
                println("""The title "$title" is used repeatedly:""")
                kotlinFiles.filter {
                    it.getAnnotation("Title") == title
                }.forEach {
                    println("- ${it.ktFile.absolutePath}")
                }
            }
            throw StopExecutionException()
        }

        // Make a map of all index files pointing at the folder where they are located
        val indexFiles = kotlinFiles.filter {
            it.isIndexFile()
        }.associateBy { it.ktFile.parentFile }

        // Update all Kotlin files
        kotlinFiles.forEach { it.update(indexFiles, dryRun = false) }
    }
}
