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
    implementation(libs.orx.kotlin.parser)
    compileOnly(libs.bundles.openrndr.core)
    testImplementation(libs.kotlin.test)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        register("simplePlugin") {
            id = "org.openrndr.dokgen-gradle"
            implementationClass = "org.openrndr.dokgen.GradlePlugin"
        }
    }
}