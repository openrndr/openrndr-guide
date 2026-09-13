rootProject.name = "openrndr-guide"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/") {
            name = "Central Portal Snapshots"

            content {
                includeGroup("org.openrndr")
                includeGroup("org.openrndr.extra")
            }
        }
    }
}

includeBuild("dokgen")
