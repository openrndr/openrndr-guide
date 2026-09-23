@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Camera2D")
@file:ParentTitle("Extensions")
@file:Order("130")
@file:URL("extensions/camera2D")

package docs.`45_Extensions`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.camera.Camera2D
import org.openrndr.extra.camera.Camera2DManual
import org.openrndr.extra.shapes.primitives.regularPolygon

fun main() {

    @Text
    """
    ## The `Camera2D` extension

    This extension allows the user to easily pan, rotate and scale
    the view using a mouse.
    
    * For panning, click and drag with the left mouse button.
    * For scaling, use the mouse wheel.
    * For rotating, click and drag with the right mouse button.
    * To reset the camera to its default state you can click the
      middle mouse button.
    
    Using the `Camera2D` extension can be useful to find the
    right framing for a design before taking a screenshot.
    
    You see, sometimes generative design involve randomness,
    and by using this extension you can choose what is up and what
    is down, and how much space you want between your creation and 
    the edges of the window after the design has been created.
    """

    @Code
    application {
        program {
            backgroundColor = ColorRGBa.PINK
            extend(Camera2D())
            extend {
                drawer.rectangle(drawer.bounds.center, 200.0, 50.0)
                drawer.circle(drawer.bounds.position(0.3, 0.3), 100.0)
            }
        }
    }

    @Text
    """
    For more advanced use cases, you can keep a reference to the camera
    to be able access its properties and methods. In the following example,
    we draw a pentagon that is affected by the camera. We also listen
    to the keyboard to reset the camera by pressind the `r` key and
    let the user enable and disable the camera by pressing 
    the `c` key on the keyboard. Another thing we could do is to call
    the `camera.pan()`, `camera.rotate()` and `camera.zoom()` methods
    to control the camera programatically.
    """

    @Code
    application {
        program {
            val camera = Camera2D()
            extend(camera)
            extend {
                drawer.clear(ColorRGBa.WHITE)
                drawer.contour(regularPolygon(5, drawer.bounds.center, 100.0))
            }
            keyboard.keyDown.listen {
                when (it.name) {
                    "c" -> camera.userInteraction = !camera.userInteraction
                    "r" -> camera.defaults()
                }
            }
        }
    }

    @Text
    """
    ## Camera2DManual
    
    In some cases we may want to let the camera control some elements
    while others should remain static. `Camera2DManual()` makes that simple.
    In the following example we draw two hexagons: a smaller one that
    is affected by the camera, and a larger one that isn't.
    """

    @Code
    application {
        program {
            val camera = Camera2DManual()
            extend {
                drawer.clear(ColorRGBa.WHITE)
                drawer.contour(regularPolygon(6, drawer.bounds.center, 100.0))
                camera.isolated {
                    fill = ColorRGBa.WHITE
                    drawer.contour(regularPolygon(6, drawer.bounds.center, 50.0))
                }
            }
        }
    }

    @Text
    """
    [Find more Camera2D demos here](https://github.com/openrndr/orx/tree/master/orx-camera/). 
    """
}
