package docs.`10_OPENRNDR_Extras`


import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.grayscale
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.extra.noise.uniform
import org.openrndr.extra.noise.uniformRing
import org.openrndr.extra.noise.uniforms
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

fun main(args:Array<String>) {

//    @Text "# orx-noise"
//
//    @Text """A collection of noisy functions. Source and extra documentation can be found in the [orx-noise sourcetree](https://github.com/openrndr/orx/tree/master/orx-noise)."""
//
//    @Text "## Uniform noise"
//
//    @Text """The library provides extension methods for `Double`, `Vector2`, `Vector3`, `Vector4` to create random vectors easily. To create
//scalars and vectors with uniformly distributed noise you use the `uniform` extension function.
//    """.trimMargin()
//    @Code.Block
//    run {
//        val d1 = Double.uniform(0.0, 640.0)
//        val v2 = Vector2.uniform(0.0, 640.0)
//        val v3 = Vector3.uniform(0.0, 640.0)
//        val v4 = Vector4.uniform(0.0, 640.0)
//    }
//
//    @Text """To create multiple samples of noise one uses the `uniforms` function."""
//    @Code.Block
//    run {
//        val v2 = Vector2.uniforms(100, Vector2(0.0, 0.0), Vector2(640.0, 640.0))
//        val v3 = Vector3.uniforms(100, Vector3(0.0, 0.0, 0.0), Vector3(640.0, 640.0, 640.0))
//    }
//
//    @Text "## Uniform ring noise"
//    @Code.Block
//    run {
//        val v2 = Vector2.uniformRing(0.0, 300.0)
//        val v3 = Vector3.uniformRing(0.0, 300.0)
//        val v4 = Vector4.uniformRing(0.0, 300.0)
//    }

    @Media.Image """media/orx-noise-001.png"""
    @Application
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/orx-noise-001.png"
            }


            extend {
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.translate(width/2.0, height/2.0)

                for (i in 0 until 1000) {
                    drawer.circle(Vector2.uniformRing(200.0, 300.0), 20.0)
                }


            }
        }
    }


    @Media.Image
    """
    media/orx-noise-image-007.png
    """
    @Application
    application {

        @Code
        program {
            val image = loadImage("data/cheeta.jpg")

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/orx-noise-image-005.png"
            }

            extend {
                // -- here we concatenate the transforms using the multiplication operator.
                drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK) * grayscale()
                drawer.image(image)
            }
        }
    }


}