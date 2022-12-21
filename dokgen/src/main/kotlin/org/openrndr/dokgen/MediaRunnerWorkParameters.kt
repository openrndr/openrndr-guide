package org.openrndr.dokgen

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters

interface MediaRunnerWorkParameters : WorkParameters {
    val classPath: Property<String>
    val klass: Property<String>
    val jvmArgs: ListProperty<String>
}