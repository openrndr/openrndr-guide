package org.openrndr.dokgen

import org.gradle.process.ExecOperations
import org.gradle.workers.WorkAction
import javax.inject.Inject

abstract class MediaRunnerWorkAction : WorkAction<MediaRunnerWorkParameters> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun execute() {
        try {
            execOperations.javaexec {
                it.classpath(parameters.classPath.get())
                it.jvmArgs = parameters.jvmArgs.get()
                it.mainClass.set(parameters.klass)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}