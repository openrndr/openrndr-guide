@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Screenshots")
@file:ParentTitle("Extensions")
@file:Order("110")
@file:URL("extensions/screenshots")

package docs.`45_Extensions`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.extensions.Screenshots

fun main() {

    @Text
    """
    ## The `Screenshots` extension

    The Screenshots extension saves the current window as an image file under the `screenshots` folder in your project.
    The default key that triggers saving the image is the space bar key. 

    To setup the screenshots we only need to add one line to our program:
    """

    @Code
    application {
        program {
            extend(Screenshots())
            extend {
                // -- draw here
            }
        }
    }

    @Text
    """
    The extension provides many configurable options. This example demonstrates how to adjust some of them:
    """

    @Code
    application {
        program {
            extend(Screenshots()) {
                key = "s"
                folder = "work-in-progress"
                async = false
            }
            extend {
                // -- draw here
            }
        }
    }

    @Text
    """
    To discover other configurable options you can use the autocomplete feature (ctrl+space by default) in 
    IntelliJ IDEA or explore [its source code](https://github.com/openrndr/openrndr/blob/master/openrndr-extensions/src/jvmMain/kotlin/org/openrndr/extensions/Screenshots.kt).
    """
}
