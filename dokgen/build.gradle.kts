@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
//    kotlin("jvm")
    `maven-publish`
//    `kotlin-dsl`
}

group = "org.openrndr"
version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
//    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.compiler.embeddable)

    compileOnly(libs.bundles.openrndr.core)

    testImplementation(libs.kotlin.test)
    testRuntimeOnly(libs.kotlin.reflect)
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "org.openrndr.dokgen-gradle"
            implementationClass = "org.openrndr.dokgen.GradlePlugin"
        }
    }
}