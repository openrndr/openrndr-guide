import org.apache.tools.ant.filters.ReplaceTokens
import java.io.ByteArrayOutputStream
import java.net.URI

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
//    alias(libs.plugins.kotlin.jvm)
    kotlin("jvm")
    alias(libs.plugins.git.publish)
    id("org.openrndr.dokgen-gradle")
    org.openrndr.guide.convention.`component-metadata-rule`
}

group = "org.openrndr.guide"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        // This is needed to resolve `com.github.ricardomatias:delaunator`
        url = URI("https://maven.openrndr.org")
    }
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.jsoup)
    implementation(libs.gson)
    implementation(libs.csv)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)
    testImplementation(libs.junit)
    implementation(libs.slf4j.simple)
    implementation(libs.bundles.openrndr.core)
    implementation(libs.bundles.openrndr.rest)
    implementation(libs.bundles.orx)
    implementation(libs.dokgen)
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
    description = "Publish website to github.com/openrndr/openrndr-guide"
    doLast {
        gitPublish.repoUri.set("git@github.com:openrndr/openrndr-guide.git")
        gitPublish.branch.set("generated")
        gitPublish.repoDir.set(file("$buildDir/gitrepo-docs"))
        gitPublish.contents.from("build/dokgen/jekyll") {
            exclude("docs/.jekyll*", "docs/_site", "docs/*-cache", "docs/*.sh")
        }
        gitPublish.commitMessage.set("Update docs")
    }

    group = "dokgen"
    finalizedBy(gitPublishPush)
}


fun getRepoLastVersion(repo: String): String {
    val output = ByteArrayOutputStream()
    exec {
        commandLine("git", "ls-remote", "--refs", "--tags", repo)
        standardOutput = output
    }
    return output.toString().lines().map { line ->
        line.substringAfter("refs/tags/v", "")
    }.last()
}

val publishExamples by tasks.registering {
    description = "Publish examples to github.com/openrndr/openrndr-examples"
    val openrndrVersion = getRepoLastVersion("https://github.com/openrndr/openrndr")
    val orxVersion = getRepoLastVersion("https://github.com/openrndr/orx")
    val ormlVersion = getRepoLastVersion("https://github.com/openrndr/orml")
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
                        openrndrVersion to openrndrVersion,
                        orxVersion to orxVersion,
                        ormlVersion to ormlVersion
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
    group = "dokgen"
    finalizedBy(gitPublishPush)
}
