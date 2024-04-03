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

fun main() {

    @Text
    """
    ## The `Camera2D` extension

    This extension allows the user to easily pan, rotate and scale
    the view using a mouse.
    
    * For panning click and drag with the left mouse button.
    * For scaling, use the mouse wheel.
    * For rotating, click and drag with the right mouse button.
    
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
    Find the simple [source code of this extension in the orx repository](https://github.com/openrndr/orx/blob/master/orx-camera/src/commonMain/kotlin/Camera2D.kt). 
    """
}