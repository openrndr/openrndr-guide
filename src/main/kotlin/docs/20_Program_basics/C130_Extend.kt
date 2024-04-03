@file:Suppress("UNUSED_EXPRESSION", "UNREACHABLE_CODE")
@file:Title("Extend")
@file:ParentTitle("Program basics")
@file:Order("130")
@file:URL("programBasics/extend")

package docs.`03_Program_basics`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    ## Extend
    
    The `program` block usually contains an `extend` block
    which gets executed as often as possible.    
    """

    @Code
    application {
        program {
            extend {
                drawer.circle(width / 2.0, height / 2.0, 50.0)
            }
        }
    }

    @Text
    """
    The `extend` block serves as a "draw loop", which is what we
    need for drawing smooth animations. To demonstrate that the
    result is not a still image, let's draw a circle located 
    wherever the mouse cursor is:
    
    """.trimIndent()

    @Code
    application {
        program {
            extend {
                drawer.circle(mouse.position, 50.0)
            }
        }
    }

}
