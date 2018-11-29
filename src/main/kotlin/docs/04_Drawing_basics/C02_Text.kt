package docs.`04_Drawing_basics`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.FontImageMap
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.shape.Rectangle
import org.openrndr.text.Writer

fun main(args: Array<String>) {

    @Text """# Drawing text
OPENRNDR comes with support for rendering bitmap text.
"""

    @Text """## Simple text rendering
Here we show how to render simple texts.
""".trimIndent()

    @Media.Image """media/text-001.png"""

    @Application
    application {
        @Code
        program {
            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/text-001.png"
            }
            val font = FontImageMap.fromUrl("file:data/IBMPlexMono-Bold.ttf", 48.0)
            extend {
                drawer.background(ColorRGBa.PINK)
                drawer.fontMap = font
                drawer.fill = ColorRGBa.BLACK
                drawer.text("HELLO WORLD", width / 2.0 - 100.0, height / 2.0)

            }
        }
    }


    @Text """## Advanced text rendering
OPENRNDR comes with a `Writer` class that allows for basic typesetting. The `Writer` tool is based on the concept of text box and a cursor.

Its use is easiest demonstrated through an example:
""".trimIndent()

    @Media.Image """media/text-002.png"""

    @Application
    application {
        configure {
            width = 770
            height = 578
        }

        @Code
        program {
            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/text-002.png"
            }
            val font = FontImageMap.fromUrl("file:data/IBMPlexMono-Bold.ttf", 24.0)
            extend {
                drawer.background(ColorRGBa.PINK)
                drawer.fontMap = font
                drawer.fill = ColorRGBa.BLACK

                val writer = Writer(drawer)
                writer.apply {
                    newLine()
                    text("Here is a line of text..")
                    newLine()
                    text("Here is another line of text..")
                }
            }
        }
    }

    @Text """### Specifying the text area
The `box` field of `Writer` is used to specify where text should be written. Let's set the text area
to a 300 by 300 pixel rectangle starting at (40, 40).

We see that the text is now drawn with margins above and left of the text, and that the second line of
text is set on two rows.
    """.trimMargin()

    @Media.Image """media/text-003.png"""

    @Application
    application {
        configure {
            width = 770
            height = 578
        }

        @Code
        program {
            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/text-003.png"
            }
            val font = FontImageMap.fromUrl("file:data/IBMPlexMono-Bold.ttf", 24.0)
            extend {
                drawer.background(ColorRGBa.PINK)
                drawer.fontMap = font
                drawer.fill = ColorRGBa.BLACK

                val writer = Writer(drawer)
                writer.apply {
                    writer.box = Rectangle(40.0, 40.0, 300.0, 300.0)
                    newLine()
                    text("Here is a line of text..")
                    newLine()
                    text("Here is another line of text..")
                }
            }
        }
    }

    @Text """### Text properties
Text tracking and leading can be
    """.trimMargin()

    @Media.Image """media/text-004.png"""

    @Application
    application {
        configure {
            width = 770
            height = 578
        }

        @Code
        program {
            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/text-004.png"
            }
            val font = FontImageMap.fromUrl("file:data/IBMPlexMono-Bold.ttf", 24.0)
            extend {
                drawer.background(ColorRGBa.PINK)
                drawer.fontMap = font
                drawer.fill = ColorRGBa.BLACK

                val writer = Writer(drawer)
                writer.style.leading = Math.cos(seconds) * 20.0 + 0.0
                writer.style.tracking = Math.sin(seconds) * 20.0
                writer.apply {
                    writer.box = Rectangle(40.0, 40.0, width - 80.0, height - 80.0)
                    newLine()
                    text("Here is a line of text..")
                    newLine()
                    text("Here is another line of text..")
                    newLine()
                    text("Let's even throw another line of text in, for good measure!")
                }
            }
        }
    }

}

