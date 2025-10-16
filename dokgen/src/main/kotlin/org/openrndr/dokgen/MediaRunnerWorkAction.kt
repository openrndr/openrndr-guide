package org.openrndr.dokgen

import org.gradle.process.ExecOperations
import org.gradle.workers.WorkAction
import javax.inject.Inject

abstract class MediaRunnerWorkAction : WorkAction<MediaRunnerWorkParameters> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun execute() {
        for (i in 0 until 10) {
            try {
                execOperations.javaexec {
                    it.classpath(parameters.classPath.get())
                    it.jvmArgs = parameters.jvmArgs.get()
                    it.mainClass.set(parameters.mainClass)
                }
                break
            } catch (e: Exception) {
                if (i == 9) {
                    throw RuntimeException(e)
                } else {
                    System.err.println("Failed to run media example, retrying in a second...")
                    Thread.sleep(1000)
                }
            }
        }
    }
}
