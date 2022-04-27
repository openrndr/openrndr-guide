@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Array textures")
@file:ParentTitle("Advanced drawing")
@file:Order("170")
@file:URL("advancedDrawing/arrayTextures")

package docs.`06_Advanced_drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*

import org.openrndr.math.mod
import org.openrndr.shape.Rectangle

import java.nio.ByteBuffer
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text
    """
    # Array textures
    
    Array textures are a special type of texture that make it possible to 
    access 2048 layers of texture data from a single texture sampler.
    
    Array textures are encapsulated by the 
    [`ArrayTexture` interface](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/ArrayTexture.kt)

    ## Creation
    
    Array textures are created using the 
    [`arrayTexture`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/ArrayTexture.kt#L67) 
    function.
    """

    @Code
    application {
        program {
            // -- create an array texture with 100 layers
            val at = arrayTexture(512, 512, 100)
        }
    }

    @Text
    """
    ## Writing to array textures
    
    There are several ways to get texture data into array textures. 
    Let's have a look at them.
    
    One can copy from a ColorBuffer using `.copyTo()`
    """

    @Code
    application {
        program {
            val at = arrayTexture(512, 512, 100)
            val cb = colorBuffer(512, 512)
            // -- copy from the color buffer to the 4th layer of the array texture
            cb.copyTo(at, 4)
        }
    }

    @Text
    """
    or copy from an array texture layer to another layer
    """

    @Code
    application {
        program {
            val at = arrayTexture(512, 512, 100)
            // -- copy from the 2nd array texture layer to the 4th array texture layer
            at.copyTo(2, at, 4)
        }
    }

    @Text
    """
    or write to an array texture layer from a direct ByteBuffer
    """

    @Code
    application {
        program {
            val at = arrayTexture(512, 512, 100)
            val buffer = ByteBuffer.allocateDirect(512 * 512 * 4)

            // fill buffer with random data
            for (y in 0 until 512) {
                for (x in 0 until 512) {
                    for (c in 0 until 4) {
                        buffer.put((Math.random() * 255).toInt().toByte())
                    }
                }
            }
            buffer.rewind()
            // -- write the buffer into the 0th layer
            at.write(0, buffer)
        }
    }

    @Text
    """
    ## Drawing array textures
    
    Array textures can be drawn using the `Drawer.image` functions.
    
    As example we show how to draw the 0th layer of an array texture
    """

    @Code
    application {
        program {
            val at = arrayTexture(512, 512, 100)

            extend {
                drawer.image(at, 0)
                // -- with position and size arguments
                drawer.image(at, 0, 100.0, 100.0, 256.0, 256.0)
            }
        }
    }

    @Text
    """
    We can also render batches of array textures by passing lists of layer 
    indexes and source-target rectangle pairs.
    """

    @Code
    application {
        program {
            val at = arrayTexture(512, 512, 100)

            extend {
                drawer.image(at, 0)

                val layers = mutableListOf<Int>()
                val rectangles = mutableListOf<Pair<Rectangle, Rectangle>>()

                for (i in 0 until 100) {
                    layers.add((Math.random() * 100).toInt())
                    val source = Rectangle(
                        Math.random() * 512.0,
                        Math.random() * 512.0,
                        Math.random() * 512.0,
                        Math.random() * 512.0
                    )
                    val target = Rectangle(
                        Math.random() * 512.0,
                        Math.random() * 512.0,
                        Math.random() * 512.0,
                        Math.random() * 512.0
                    )
                    rectangles.add(source to target)
                }
                drawer.image(at, layers, rectangles)

            }
        }
    }

    @Text
    """
    ## Drawing into array textures
    
    Array textures can be used as attachment for render targets.
    
    Here we show how to use a single layer from an array texture as an 
    attachment for a render target.
    """

    @Code
    application {
        program {
            val at = arrayTexture(512, 512, 100)
            // -- create a render target
            val rt = renderTarget(512, 512) {
                // -- attach the 0th layer of the array texture
                arrayTexture(at, 0)
                depthBuffer()
            }

            extend {
                drawer.isolatedWithTarget(rt) {
                    drawer.ortho(rt)
                    drawer.clear(ColorRGBa.PINK)
                }
                drawer.image(at, 0)
            }
        }
    }

    @Text
    """
    Let's conclude this chapter by means of a small slit scanning demonstration. 
    Here we use a single array texture and a list of render targets, all using 
    different layers of the same array texture.
    """

    @Media.Video "media/array-texture-001.mp4"

    @Application
    @ProduceVideo("media/array-texture-001.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 576
        }

        program {
            val at = arrayTexture(770, 576, 116)
            val renderTargets = List(at.layers) {
                renderTarget(at.width, at.height) {
                    arrayTexture(at, it)
                }
            }
            var index = 0
            extend {
                drawer.clear(ColorRGBa.BLACK)
                drawer.isolatedWithTarget(renderTargets[index % renderTargets.size]) {
                    drawer.clear(ColorRGBa.BLACK)
                    drawer.fill = ColorRGBa.PINK.opacify(0.5)
                    drawer.stroke = null
                    for (i in 0 until 20) {
                        drawer.circle(
                            cos(seconds * 5.0 + i) * 256 + width / 2.0,
                            sin(i + seconds * 6.32) * 256 + height / 2.0,
                            100.0
                        )
                    }
                }

                val layers = (0 until at.layers).map { mod(index - it, at.layers) }
                val rectangles = (0 until at.layers).map {
                    val span = Rectangle(0.0, it * 5.0, at.width * 1.0, 5.0)
                    span to span
                }

                drawer.image(at, layers, rectangles)
                index++
            }
        }
    }
}