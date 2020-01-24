@file:Suppress("UNUSED_EXPRESSION")

package docs.`04_Drawing_basics`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.grayscale
import org.openrndr.draw.invert
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extensions.Screenshots
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.shape.Rectangle
import java.io.File

fun main() {

    @Text
    """
    # Images

    Images are stored in `ColorBuffer` instances, the image data resides in GPU memory

    ## Loading and drawing images

    Images are loaded using the `loadImage` function and drawn using `Drawer.image`.

    """

    @Media.Image
    """
    media/image-001.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-001.png"
            }

            extend {
                drawer.image(image)
            }
        }
    }

    @Text
    """To change the location of the image one can use `Drawer.image` with extra coordinates provided.
    """
    run {
        application {

            program {
                val image = loadImage("data/cheeta.jpg")
                extend {
                    @Code.Block
                    run {
                        drawer.image(image, 40.0, 40.0)
                    }
                }
            }
        }
    }

    @Text
    """Extra `width` and `height` arguments can be provided to draw a scaled version of the image
    """
    run {
        application {

            program {
                val image = loadImage("data/cheeta.jpg")
                extend {
                    @Code.Block
                    run {
                        drawer.image(image, 40.0, 40.0, 64.0, 48.0)
                    }
                }
            }
        }
    }

    @Text
    """
    ## Drawing parts of images
    It is possible to draw parts of images by specifying _source_ and _target_ rectangles. The source rectangle describes
    the area that should be taken from the image and presented in the target rectangle.
    """

    @Media.Image
    """
    media/image-002.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-002.png"
            }

            extend {
                val source = Rectangle(0.0, 0.0, 320.0, 240.0)
                val target = Rectangle(160.0, 120.0, 320.0, 240.0)
                drawer.image(image, source, target)

            }
        }
    }

    @Text
    """## Drawing many parts of images
    """

    @Media.Image
    """
    media/image-003.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-003.png"
            }

            extend {
                val areas = (0..10).flatMap { y ->
                    (0..10).map { x ->
                        val source = Rectangle(x * (width / 10.0), y * (height / 10.0), width / 5.0, height / 5.0)
                        val target = Rectangle(x * (width / 10.0), y * (height / 10.0), width / 10.0, height / 10.0)
                        source to target
                    }
                }
                drawer.image(image, areas)
            }
        }
    }

    @Text
    """
    ## Changing the appearance of images
    A linear color transform can be applied to images by setting `Drawer.drawStyle.colorMatrix` to a `Matrix55` value.
    ### Tinting
    Tinting multiplies the image color with a _tint color_.
    """

    @Media.Image
    """
    media/image-004.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-004.png"
            }

            extend {
                drawer.drawStyle.colorMatrix = tint(ColorRGBa.RED)
                drawer.image(image, 0.0, 0.0)
            }
        }
    }
    @Text """### Inverting
Drawing an image with inverted colors can be achieved by using the `invert` color matrix.
"""

    @Media.Image
    """
media/image-005.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-005.png"
            }

            extend {
                drawer.drawStyle.colorMatrix = invert
                drawer.image(image, 0.0, 0.0)
            }
        }
    }

    @Text
    """
    ### Grayscale
    Drawing an image with inverted colors can be achieved by using the `grayscale` color matrix.
    """

    @Media.Image
    """
    media/image-006.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-006.png"
            }

            extend {
                // -- the factors below determine the RGB mixing factors
                drawer.drawStyle.colorMatrix = grayscale(1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0)
                drawer.image(image)
            }
        }
    }

    @Text
    """
    ### Concatenating color transforms
    Color transforms can be combined using the multiplication operator. This is called transform concatenation.
    Keep in mind that transform concatenations are read from right to left, and in the following example we first
    apply the `grayscale` transform and then the `tint` transform.
    """

    @Media.Image
    """
    media/image-007.png
    """
    @Application
    application {
        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/image-007.png"
            }

            extend {
                // -- here we concatenate the transforms using the multiplication operator.
                drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK) * grayscale()
                drawer.image(image)
            }
        }
    }
}
