@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Clipping")
@file:ParentTitle("Advanced drawing")
@file:Order("120")
@file:URL("advancedDrawing/clipping")

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.shape.Rectangle
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text 
    """
    # Clipping
    
    OPENRNDR's drawer supports a single rectangular clip mask.
    """

    @Media.Video "../media/clipping-001.mp4"

    @Application
    @ProduceVideo("media/clipping-001.mp4", 6.28318, 60)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
            extend {
                drawer.stroke = null
                drawer.fill = ColorRGBa.PINK

                // -- set the rectangular clipping mask
                drawer.drawStyle.clip = Rectangle(100.0, 100.0, width -200.0, height - 200.00)

                drawer.circle(
                    cos(seconds) * width / 2.0 + width / 2.0,
                    sin(seconds) * height / 2.0 + height / 2.0,
                    200.0
                )

                // -- restore clipping
                drawer.drawStyle.clip = null
            }
        }
    }
}