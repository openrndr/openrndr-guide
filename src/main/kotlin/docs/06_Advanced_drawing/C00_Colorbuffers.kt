@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Color buffers")
@file:ParentTitle("Advanced drawing")
@file:Order("100")
@file:URL("advancedDrawing/colorbuffers")

package docs.`06_Advanced_drawing`

import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*
import java.io.File
import java.nio.ByteBuffer

fun main() {
    @Text """# Color buffers"""

    @Text """A color buffer is an image stored in GPU memory."""

    @Text """## Creating a color buffer"""
    @Text """Color buffers are created using the `colorBuffer()` function. """

    @Code.Block
    run {
        val cb = colorBuffer(640, 480)
    }

    @Text """### Specifying buffer format"""
    @Text """Color buffers can be created in different formats. The buffer format specifies the number and order of channels in the image. Color buffers can have 1 to 4 channels.
        The `format` argument can be any [`ColorFormat`](https://api.openrndr.org/org.openrndr.draw/-color-format/index.html) value.  
    """
    @Code.Block
    run {
        val cb = colorBuffer(640, 480, format = ColorFormat.R)
    }

    @Text """### Specifying buffer type"""
    @Text """The buffer type specifies which data type is used for storing colors in the buffer. The `type` argument can be any [`ColorType`](https://api.openrndr.org/org.openrndr.draw/-color-type/index.html) value."""

    @Code.Block
    run {
        val cb = colorBuffer(640, 480, type = ColorType.FLOAT16)
    }

    @Text """## Loading color buffers"""
    @Text """Color buffers can be loaded from an image stored on disk. Supported file types are png, jpg
        and exr (OpenEXR).
    """
    @Code.Block
    run {
        val cb = loadImage("data/images/pm5544.png")
    }

    @Text """## Saving color buffers"""
    @Text """Color buffers can be saved to disk using the `saveToFile` member function. Supported file types are png, jpg
        and exr (OpenEXR). 
    """

    @Code.Block
    run {
        val cb = colorBuffer(640, 480)
        cb.saveToFile(File("output.png"))
    }

    @Text """## Copying between color buffers"""
    @Text """Color buffer contents can be copied using the `copyTo` member function. Copying works between color buffers of different formats and types.
    """
    @Code.Block
    run {
        // -- create color buffers
        val cb0 = colorBuffer(640, 480, type = ColorType.FLOAT16)
        val cb1 = colorBuffer(640, 480, type = ColorType.FLOAT32)
        //-- copy contents of cb0 to cb1
        cb0.copyTo(cb1)
    }

    @Text """## Writing into color buffers """
    @Text """To upload data into the color buffer one uses the `write` member function."""
    @Code.Block
    run {
        // -- create a color buffer that uses 8 bits per channel (the default)
        val cb = colorBuffer(640, 480, type = ColorType.UINT8)

        // -- create a buffer (on CPU) that matches size and layout of the color buffer
        val buffer = ByteBuffer.allocateDirect(cb.width * cb.height * cb.format.componentCount * cb.type.componentSize)

        // -- fill buffer with random data
        for (y in 0 until cb.height) {
            for (x in 0 until cb.width) {
                for (c in 0 until cb.format.componentCount) {
                    buffer.put((Math.random() * 255).toInt().toByte())
                }
            }
        }

        // -- rewind the buffer, this is essential as upload will be from the position we left the buffer at
        buffer.rewind()
        // -- write into color buffer
        cb.write(buffer)
    }

    @Text """## Reading from color buffers """
    @Text """To download data from a color buffer one uses the `read` member function."""
    @Code.Block
    run {
        // -- create a color buffer that uses 8 bits per channel (the default)
        val cb = colorBuffer(640, 480, type = ColorType.UINT8)

        // -- create a buffer (on CPU) that matches size and layout of the color buffer
        val buffer = ByteBuffer.allocateDirect(cb.width * cb.height * cb.format.componentCount * cb.type.componentSize)

        // -- download data into buffer
        cb.read(buffer)
    }

    @Text """## Color buffer shadows"""
    @Text """To simplify the process of reading and writing from and to color buffers we added a shadow buffer to
`ColorBuffer`. A shadow buffer offers a simple interface to access the color buffer's contents.

Note that shadow buffers have more overhead than using `read()` and `write()`.
"""
    @Code.Block
    run {
        // -- create a color buffer that uses 8 bits per channel (the default)
        val cb = colorBuffer(640, 480, type = ColorType.UINT8)
        val shadow = cb.shadow

        // -- download cb's contents into shadow
        shadow.download()

        // -- place random data in the shadow buffer
        for (y in 0 until cb.height) {
            for (x in 0 until cb.width) {
                shadow[x, y] = ColorRGBa(Math.random(), Math.random(), Math.random())
            }
        }

        // -- upload shadow to cb
        shadow.upload()
    }

}



