@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Drawing circles, rectangles and lines")
@file:ParentTitle("Drawing")
@file:Order("100")
@file:URL("drawing/circlesRectanglesLines")

package docs.`30_Drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.LineCap
import org.openrndr.math.Vector2

fun main() {
    @Text 
    """
    ## Drawing circles
    
    A circle is drawn around coordinates `x`, `y`, i.e. `x` and `y` specify the center of the circle.
    Circles are filled with the color set in `Drawer.fill` and their stroke is set to `Drawer.stroke`. The width of the stroke follows `Drawer.strokeWeight`.
    """

    @Media.Image "../media/circle-001.jpg"

    @Application
    @ProduceScreenshot("media/circle-001.jpg")
    @Code
    application {
        configure {
        }
        program {
            extend {
                drawer.clear(ColorRGBa.PINK)

                // -- draw circle with white fill and black stroke
                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = ColorRGBa.BLACK
                drawer.strokeWeight = 1.0
                drawer.circle(width / 6.0, height / 2.0, width / 8.0)

                // -- draw circle without fill, but with black stroke
                drawer.fill = null
                drawer.stroke = ColorRGBa.BLACK
                drawer.strokeWeight = 1.0
                drawer.circle(
                    width / 6.0 + width / 3.0,
                    height / 2.0,
                    width / 8.0
                )

                // -- draw circle with white fill, but without stroke
                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = null
                drawer.strokeWeight = 1.0
                drawer.circle(
                    width / 6.0 + 2 * width / 3.0,
                    height / 2.0,
                    width / 8.0
                )
            }
        }
    }

    @Text 
    """
    You may have spotted the two other APIs for drawing circles; `Drawer.circle(center: Vector2, radius: Double)` and `Drawer.circle(circle: Circle)` and wonder what those are for. They are for drawing the exact same circle, but using arguments that may be more convenient in scenarios in which values are provided by `Vector2` or `Circle` types.
    """

    run {
        application {
            program {
                @Code.Block
                run {
                    drawer.circle(mouse.position, 50.0)
                }
            }
        }
    }

    @Text
    """## Drawing rectangles"""

    @Media.Image "../media/rectangle-001.jpg"

    @Application
    @ProduceScreenshot("media/rectangle-001.jpg")
    @Code
    application {
        program {
            extend {
                drawer.clear(ColorRGBa.PINK)

                // -- draw rectangle with white fill and black stroke
                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = ColorRGBa.BLACK
                drawer.strokeWeight = 1.0
                drawer.rectangle(
                    width / 6.0 - width / 8.0,
                    height / 2.0 - width / 8.0,
                    width / 4.0,
                    width / 4.0
                )

                // -- draw rectangle without fill, but with black stroke
                drawer.fill = null
                drawer.stroke = ColorRGBa.BLACK
                drawer.strokeWeight = 1.0
                drawer.rectangle(
                    width / 6.0 - width / 8.0 + width / 3.0,
                    height / 2.0 - width / 8.0,
                    width / 4.0,
                    width / 4.0
                )

                // -- draw rectangle with white fill, but without stroke
                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = null
                drawer.strokeWeight = 1.0
                drawer.rectangle(
                    width / 6.0 - width / 8.0 + 2.0 * width / 3.0,
                    height / 2.0 - width / 8.0,
                    width / 4.0,
                    width / 4.0
                )
            }
        }
    }

    @Text
    """
    ## Drawing lines
    Single lines are drawn per segment between two pairs of coordinates using `lineSegment`. Line primitives use `Drawer.stroke` to determine the color drawing color and `Drawer.strokeWeight` to determine the width of the line.

    Line endings can be drawn in three styles by setting `Drawer.lineCap`

    LineCap. | description
    ---------|------------
    BUTT     | butt cap
    ROUND    | round cap
    SQUARE   | square cap
    """

    @Media.Image "../media/line-001.jpg"

    @Application
    @ProduceScreenshot("media/line-001.jpg")
    @Code
    application {
        program {
            extend {
                drawer.clear(ColorRGBa.PINK)
                // -- setup line appearance
                drawer.stroke = ColorRGBa.BLACK
                drawer.strokeWeight = 5.0
                drawer.lineCap = LineCap.ROUND

                drawer.lineSegment(
                    10.0,
                    height / 2.0 - 20.0,
                    width - 10.0,
                    height / 2.0 - 20.0
                )

                drawer.lineCap = LineCap.BUTT
                drawer.lineSegment(
                    10.0,
                    height / 2.0,
                    width - 10.0,
                    height / 2.0
                )

                drawer.lineCap = LineCap.SQUARE
                drawer.lineSegment(
                    10.0,
                    height / 2.0 + 20.0,
                    width - 10.0,
                    height / 2.0 + 20.0
                )
            }
        }
    }

    @Text
    """
    ### Drawing line strips
    A run of connected line segments is called a line strip and is drawn using `lineStrip`.
    To draw a line strip one supplies a list of points between which line segments should be drawn.
    """

    @Media.Image "../media/line-002.jpg"

    @Application
    @ProduceScreenshot("media/line-002.jpg")
    @Code
    application {
        program {
            extend {
                drawer.clear(ColorRGBa.PINK)
                // -- setup line appearance
                drawer.stroke = ColorRGBa.BLACK
                drawer.strokeWeight = 5.0
                drawer.lineCap = LineCap.ROUND

                val points = listOf(
                    Vector2(10.0, height - 10.0),
                    Vector2(width / 2.0, 10.0),
                    Vector2(width - 10.0, height - 10.0)
                )
                drawer.lineStrip(points)
            }
        }
    }
}