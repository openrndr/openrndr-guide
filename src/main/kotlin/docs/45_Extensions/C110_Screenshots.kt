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
    ## The `ScreenShots` extension

    foo bar

    To setup the screenshots do the following:
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
}