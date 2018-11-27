@file:Suppress("UNUSED_EXPRESSION")

package docs.drawing

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.extensions.SingleScreenshot

fun main(args: Array<String>) {
    @Text
    """
    # Drawing Primitives
    In this topic we introduce OPENRNDR's basic drawing primitives. We show how to draw lines, rectangles and circles.

    ## Relevant apis
    ```kotlin
    // -- circle drawing
    Drawer.circle(x:Double, y:Double, radius:Double)
    Drawer.circle(center: Vector2, radius:Double)
    Drawer.circle(circle: Circle)

    // -- rectangle drawing
    Drawer.rectangle(x:Double, y:Double, width:Double, height:Double)
    Drawer.rectangle(corner: Vector2, width:Double, height:Double)
    Drawer.rectangle(rectangle: Rectangle)

    // -- line segment drawing
    Drawer.lineSegment(x0: Double, y0: Double, x1: Double, y1: Double)
    Drawer.lineSegment(start:Vector2, end:Vector2)
    Drawer.lineSegment(lineSegment: LineSegment)

    // -- appearance attributes
    Drawer.fill
    Drawer.stroke
    Drawer.strokeWeight

    Drawer.background(color: ColorRGBa)
    ```
    """

    @Application
    application {
        configure {
            width = 640
            height = 480
        }

        program {
            @Exclude
            extend(SingleScreenshot().apply {
                outputFile = "media/drawing-circles-001.png"
            })


            @Code(
                """
                ## Drawing circles
                In the following example we use `Drawer.circle(x: Double, y: Double, radius: Double)` to draw a single circle.
                A circle is drawn around coordinates `x`, `y`, i.e. `x` and `y` specify the center of the circle.
                Circles are filled with the color set in `Drawer.fill` and their stroke is set to `Drawer.stroke`. The width of the stroke follows `Drawer.strokeWeight`.
                """
            )
            extend {
                drawer.background(ColorRGBa.WHITE)
                // -- setup appearance
                drawer.fill = ColorRGBa.RED
                drawer.stroke = ColorRGBa.BLUE
                drawer.strokeWeight = 1.0
                 // -- draw a circle with its center at (140.0, 140.0) with radius 130.0
                drawer.circle(140.0, 140.0, 130.0)
            }
        }
    }

    @Media.Image
    """
    media/drawing-circles-001.png
    """
}

