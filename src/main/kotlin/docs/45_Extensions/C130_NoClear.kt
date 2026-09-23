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
import org.openrndr.extra.noise.shapes.uniform
import org.openrndr.extra.shapes.hobbycurve.hobbyCurve
import kotlin.random.Random

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

    // Simulated screenshot
    @Media.Image "../media/orx-no-clear-001.png"
    @Application
    @ProduceScreenshot("media/orx-no-clear-001.png")
    application {
        program {
            backgroundColor = ColorRGBa.PINK
            extend {
                val r = Random(3)
                hobbyCurve(List(7) {
                    drawer.bounds.offsetEdges(-150.0).uniform(r)
                }, false).equidistantPositions(200).forEach {
                    drawer.circle(it, 20.0)
                }
            }
        }
    }

    @Text
    """
    Without `NoClear` only one circle would be visible at the current mouse location.
        
    ### Configurable properties
    
    `orx-no-clear` provides configurable properties:
    
    - A `multisample` value can be passed in the constructor. The default value is
      `BufferMultisample.Disabled`. For smoother rendering we can construct it like this:
      `NoClear(BufferMultisample.SampleCount(8))`. Different graphic cards may accept higher 
      or lower values.
    - Use `backdrop` to specify the initial state of the buffer. You could clear the background to a specific color, 
      display a loaded image, or draw anything else.
    - Use `colorType` to change the default `UINT8` color type to something else, for instance
      `FLOAT32` for finer color precision.
        
    Find [the orx-no-clear source code and examples featuring the available properties](https://github.com/openrndr/orx/tree/master/orx-no-clear) in GitHub.         
    """
}