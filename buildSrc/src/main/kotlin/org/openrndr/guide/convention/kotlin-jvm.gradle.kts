package org.openrndr.guide.convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.net.URI

val libs = the<LibrariesForLibs>()

plugins {
    java
    kotlin("jvm")
    id("org.openrndr.guide.convention.component-metadata-rule")
}

repositories {
    mavenCentral()
    maven {
        // This is needed to resolve `com.github.ricardomatias:delaunator`
        url = URI("https://maven.openrndr.org")
    }
    mavenLocal()
}

group = "org.openrndr"

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}

@Suppress("UNUSED_VARIABLE")
val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging.exceptionFormat = TestExceptionFormat.FULL
}