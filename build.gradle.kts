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
