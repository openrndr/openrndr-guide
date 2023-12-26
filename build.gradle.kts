import org.apache.tools.ant.filters.ReplaceTokens

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

val gitPublishPush: Task by tasks.getting

val publishDocs by tasks.registering {
    group = org.openrndr.dokgen.PLUGIN_NAME
    description = "Publish website to https://github.com/openrndr/openrndr-guide"

    doLast {
        gitPublish.repoUri.set("git@github.com:openrndr/openrndr-guide.git")
        gitPublish.branch.set("generated")
        gitPublish.repoDir.set(file("$buildDir/gitrepo-docs"))
        gitPublish.contents.from("build/dokgen/jekyll") {
            exclude("docs/.jekyll*", "docs/_site", "docs/*-cache", "docs/*.sh")
        }
        gitPublish.commitMessage.set("Update docs")
    }
    finalizedBy(gitPublishPush)
}

val publishExamples by tasks.registering {
    group = org.openrndr.dokgen.PLUGIN_NAME
    description = "Publish examples to https://github.com/openrndr/openrndr-examples"

    val repoTemplate = "$projectDir/src/main/resources/examples-repo-template"
    doLast {
        gitPublish.repoDir.set(file("$buildDir/gitrepo-examples"))
        gitPublish.contents {
            from(repoTemplate) {
                exclude("settings.gradle.kts")
                rename("_git(.*)", ".git$1") // skipped without this trick
            }
            from(repoTemplate) {
                include("settings.gradle.kts")
                filter<ReplaceTokens>(
                    "tokens" to mapOf(
                        "openrndrVersion" to libs.versions.openrndr.get(),
                        "orxVersion" to libs.versions.orx.get(),
                        "ormlVersion" to libs.versions.orml.get()
                    )
                )
            }
            from("$projectDir/build/dokgen/generated/examples-export") {
                into("src/main/kotlin/examples")
            }
            from("$projectDir/data/images") {
                into("data/images")
            }
            from("$projectDir/data/fonts") {
                into("data/fonts")
            }
            from("$projectDir/data/compute-shaders") {
                into("data/compute-shaders")
            }
        }
        gitPublish.repoUri.set("git@github.com:openrndr/openrndr-examples.git")
        gitPublish.branch.set("master")
        gitPublish.commitMessage.set("Update examples")
    }
    finalizedBy(gitPublishPush)
}

task("add IDE file scopes") {
    group = " ★ OPENRNDR"
    val scopesFolder = File("${project.projectDir}/.idea/scopes")
    scopesFolder.mkdirs()

    val files = listOf(
        "Code" to "file:*.kt||file:*.frag||file:*.vert||file:*.glsl",
        "Text" to "file:*.txt||file:*.md||file:*.xml||file:*.json",
        "Gradle" to "file[*buildSrc*]:*/||file:*gradle.*||file:*.gradle||file:*/gradle-wrapper.properties||file:*.toml",
        "Images" to "file:*.png||file:*.jpg||file:*.dds||file:*.exr"
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
