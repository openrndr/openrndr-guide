@file:Suppress("UNUSED_EXPRESSION")
@file:Title("NoClear")
@file:ParentTitle("Extensions")
@file:Order("130")
@file:URL("extensions/noClear")

package docs.`45_Extensions`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.noclear.NoClear

fun main() {

    @Text
    """
    ## The `NoClear` extension

    Creative coding frameworks have two different defaults: either they clear the screen before
    each animation frame or they don't. OPENRNDR belongs to the first group.

    Switching to "draw-without-clearing-the-screen" can be useful to produce complex designs with simple programs. 
   
    It is also how pen and paper seems to work: we add ink to the paper and the previous ink does not disappear.
    
    Such behavior can easily be enabled by adding `extend(NoClear())` to our programs.    
    Here an example that draws circles at the current mouse position:
    """

    @Code
    application {
        program {
            backgroundColor = ColorRGBa.PINK
            extend(NoClear())
            extend {
                drawer.circle(mouse.position, 20.0)
            }
        }
    }

    @Text
    """
    Without `NoClear` only one circle would be visible at the current mouse location.
        
    Find [additional examples and the source code of orx-no-clear](https://github.com/openrndr/orx/tree/master/orx-no-clear) in GitHub.         
    """
}