@file:Suppress("UNUSED_EXPRESSION")

package docs.`04_Drawing_basics`.`05_text`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.FontImageMap
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.text.Writer

fun main(args: Array<String>) {
    @Text
    """
    # Drawing Text
    OPENRNDR comes with two text 04_Drawing_basics modes: vector based and image based.
    The vector based mode is advised for large font sizes while the bitmap based mode is advised for smaller font sizes.

    ## Relevant apis
    TODO it would be great to generate links of these to the dokka API doc
    ```kotlin
    Drawer.fontMap
    Drawer.text(text:String, x:Double, y:Double)
    FontImageMap.fromUrl(url:String, size:Double)
    ```
    """.trimIndent()

    @Application
    application {
        configure {
            width = 640
            height = 480
        }

        program {
            extend(ScreenRecorder().apply {
                maximumDuration = 5.0
                outputFile = "media/text-001.mp4"
            })

            @Code("## Drawing bitmap based text")
            extend {
                drawer.background(ColorRGBa.PINK)
                drawer.fill = ColorRGBa.WHITE
                drawer.circle(drawer.bounds.center, Math.abs(Math.cos(seconds)) * 300)
            }

            @Media.Video
            """
            media/text-001.mp4
            """
        }
    }

    @Application
    application {
        configure {
            width = 640
            height = 480
        }
        program {


            extend(ScreenRecorder().apply {
                maximumDuration = 5.0
                outputFile = "media/text-002.mp4"
            })

            @Code("""
                ## Advanced text 04_Drawing_basics
                OPENRNDR comes with a Writer class that allows for basic typesetting. The Writer tool is based on the concept of text box and a cursor.
                Its use is easiest demonstrated through an example:
            """)
            extend {
                drawer.background(ColorRGBa.PINK)

                // -- First create a new Writer object.
                // -- The writer needs a reference to the drawer in order to match style and transforms.
                val writer = Writer(drawer)

                // -- Set a font, this is a required step
                drawer.fontMap = FontImageMap.fromUrl("file:fonts/iosevka-custom-medium.ttf", 20.0)
                drawer.stroke = null
                drawer.fill = ColorRGBa.WHITE

                // -- Setup a 400 by 400 pixel text box at position 100, 100
                writer.box = Rectangle(Vector2(100.0, 100.0), 400.0, 400.0)

                // -- Output some text
                writer.text("Some text")

                // -- Go to the next line and output more text
                writer.newLine()
                writer.text("Some more text man2!")
            }


            @Media.Video
            """
            media/text-002.mp4
            """
        }
    }


    @Application
    application {
        program {

            @Exclude
            extend(SingleScreenshot().apply {
                outputFile = "media/text-003.png"
            })

            @Text
            """
                ## Specifying the text area
            """


            @Media.Image
            """
              media/text-003.png
            """

            extend {
                drawer.background(ColorRGBa.RED)
                val writer = Writer(drawer)
                @Code.Block("""
                    The writer has a box property that determines the area in which it can place text.
                """)
                run {
                    writer.box = Rectangle(Vector2(100.0, 100.0), 400.0, 400.0)
                }

                @Code.Block("""
                     In some cases you may want to have a an infinitely large box, this avoids line breaks altogether.
                """)
                run {
                    writer.box = Rectangle(Vector2(100.0, 100.0), Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
                }

                @Code.Block("""
                     A quick way to set the box to equate the bounds of the screen:
                """)
                run {
                    writer.box = drawer.bounds
                    // -- with margins of 50 pixels
                    writer.box = drawer.bounds.offsetEdges(-50.0)
                }
            }

        }
    }

    @Text
    """
    ### Text properties
    The `Writer` has several properties that affect the appearance of text.

    | Property name      | Description |
    |---------------------|-------------|
    | `tracking` | additional space between the characters |
    | `leading`   | additional space between lines of text |
    | `ellipsis`   | the character to display when text does not fit the designated space|
"""
}

