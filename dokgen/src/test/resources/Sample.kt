@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Sample Page")
@file:ParentTitle("Samples")
@file:Order("100")
@file:URL("samples/samplePage")

package docs.`99_Samples`

import org.intellij.lang.annotations.Language
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.shape.Rectangle

fun main() {
    @Text
    """
    # Heading

    Intro paragraph.
    """

    @Media.Image "media/image-001.png"

    @Application
    @ProduceScreenshot("media/image-001.png")
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            extend {
                drawer.rectangle(
                    Rectangle(
                        0.0,
                        0.0,
                        100.0,
                        100.0
                    )
                )
            }
        }
    }

    @Text
    """
    ## Code block
    """

    @Code.Block
    run {
        // a comment
        val r = Rectangle(
            0.0,
            0.0,
            50.0,
            50.0
        )
    }

    @Media.Video "media/video-001.mp4"

    @Application
    @ProduceVideo("media/video-001.mp4", 5.0)
    application {
        program {
            extend {
                drawer.clear(ColorRGBa.PINK)
            }
        }
    }

    application {
        program {
            @Code
            extend {
                drawer.clear(ColorRGBa.BLACK)
            }
        }
    }

    // The @Language("markdown") val ... = scaffolding only exists so the IDE
    // syntax-highlights the string while editing; dokgen must drop it and keep
    // the @Text content.
    @Language("markdown") val ideHighlighted =
    @Text
    """
    ## IDE highlighted heading
    """.trimIndent()
}
