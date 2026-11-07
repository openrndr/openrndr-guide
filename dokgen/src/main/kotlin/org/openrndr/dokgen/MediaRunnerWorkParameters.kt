package org.openrndr.dokgen

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters

interface MediaRunnerWorkParameters : WorkParameters {
    val classPath: Property<String>
    val mainClass: Property<String>
    val jvmArgs: ListProperty<String>
    /** Written with [sourceHash] once the program ran successfully */
    val stampFile: RegularFileProperty
    val sourceHash: Property<String>
}