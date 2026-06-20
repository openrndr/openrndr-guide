@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Drawing")
@file:Order("1030")
@file:URL("drawing/index")

package docs.`30_Drawing`

import org.openrndr.dokgen.annotations.Order
import org.openrndr.dokgen.annotations.Text
import org.openrndr.dokgen.annotations.Title
import org.openrndr.dokgen.annotations.URL

fun main() {
    @Text
    """
    # Drawing
    
    To draw things in OPENRNDR you typically use the `drawer` object.
    
    For example:
    
    - `drawer.clear(ColorRGBa.PINK)` to clear the screen
    - `drawer.fill = ColorRGBa.WHITE` to set the fill color
    - `drawer.stroke = ColorRGBa.BLACK` to set the stroke color
    - `drawer.circle(320.0, 240.0, 50.0)` to draw a circle
    
    Explore the next pages to learn more about drawing.
    """.trimIndent()
}
