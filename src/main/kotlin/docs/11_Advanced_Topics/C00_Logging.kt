@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Logging")
@file:ParentTitle("Advanced topics")
@file:Order("100")
@file:URL("advancedTopics/logging")

package docs.`11_Advanced_Topics`

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    # Logging

    OPENRNDR uses kotlin-logging, which is a Kotlin flavoured wrapper 
    around slf4j, to log its internal workings.

    ##### Enabling logging to console

    First add `slf4j-simple` to your project's dependencies.

    ```
    dependencies {
        compile 'org.slf4j:slf4j-simple:1.7.5'
    }
    ```
    
    Then add `-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG` to the 
    VM arguments in the launch configuration.
    
    ##### Enabling OpenGL debug messages

    If your graphics hardware and drivers support OpenGL debug contexts 
    you can use `-Dorg.openrndr.gl3.debug=TRUE` to enable the debug messages.
    """.trimIndent()
}