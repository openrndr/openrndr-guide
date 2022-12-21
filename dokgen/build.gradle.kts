@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    `maven-publish`
}

group = "org.openrndr"
version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}

dependencies {
    implementation(libs.kotlin.compiler.embeddable)
    compileOnly(libs.bundles.openrndr.core)
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "org.openrndr.dokgen-gradle"
            implementationClass = "org.openrndr.dokgen.GradlePlugin"
        }
    }
}