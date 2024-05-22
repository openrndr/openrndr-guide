@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Color buffers")
@file:ParentTitle("Drawing")
@file:Order("300")
@file:URL("drawing/colorbuffers")

package docs.`30_Drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.ColorFormat
import org.openrndr.draw.ColorType
import org.openrndr.draw.colorBuffer
import org.openrndr.draw.loadImage
import org.openrndr.drawImage
import org.openrndr.extra.shadestyles.RadialGradient
import org.openrndr.shape.Rectangle
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.sin

fun main() {
    @Text
    """
    # Color buffers
    
    A color buffer is an image stored in GPU memory.
    
    ## Creating a color buffer
    
    Color buffers are created using the `colorBuffer()` function. 
    """

    @Code.Block
    run {
        val cb = colorBuffer(640, 480)
    }

    @Text
    """
    ### Specifying buffer format
    
    Color buffers can be created in different formats. The buffer format 
    specifies the number and order of channels in the image. Color buffers 
    can have 1 to 4 channels. The `format` argument can be any 
    [`ColorFormat`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/DrawStyle.kt#L108) value.  
    """

    @Code.Block
    run {
        val cb = colorBuffer(640, 480, format = ColorFormat.R)
    }

    @Text
    """
    ### Specifying buffer type
    
    The buffer type specifies which data type is used for storing colors 
    in the buffer. The `type` argument can be any 
    [`ColorType`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/DrawStyle.kt#L153) value.
    """

    @Code.Block
    run {
        val cb = colorBuffer(640, 480, type = ColorType.FLOAT16)
    }

    @Text
    """
    ## Loading color buffers
    
    Color buffers can be loaded from an image stored on disk. 
    Supported file types are png, jpg, dds and exr (OpenEXR).
    """

    @Code.Block
    run {
        val cb = loadImage("data/images/pm5544.jpg")
    }

    @Text
    """
    ## Generating color buffers
    
    Use `drawImage` to create a color buffer and draw into it.
    """

    @Media.Image "../media/colorbuffer-001.jpg"

    @Application
    @ProduceScreenshot("media/colorbuffer-001.jpg")
    @Code
    application {
        program {
            val gradientBackground = drawImage(width, height) {
                // Draw anything here, for example a radial gradient.
                drawer.shadeStyle = RadialGradient(ColorRGBa.WHITE, ColorRGBa.PINK)
                val r = Rectangle.fromCenter(drawer.bounds.center, 800.0, 800.0)
                drawer.rectangle(r)
            }
            extend {
                drawer.image(gradientBackground)
                drawer.circle(drawer.bounds.center, sin(seconds) * 80 + 100)
            }
        }
    }

    @Text
    """
    `drawImage` is convenient for creating static images. If a program requires redrawing 
    a color buffer use a [render target](/drawing/renderTargets.html) instead.
        
    ## Freeing color buffers
    
    If a program creates new buffers while it runs
    it is important to free those buffers when no longer needed 
    to avoid running out of memory.
    """

    run {
        val cb = colorBuffer(640, 480)
        @Code.Block
        run {
            // -- When done using the buffer call destroy() to free its memory.
            cb.destroy()
        }
    }

    @Text
    """
    ## Saving color buffers
    
    Color buffers can be saved to disk using the `saveToFile` member function. 
    Supported file types are png, jpg, dds and exr (OpenEXR). 
    """

    @Code.Block
    run {
        val cb = colorBuffer(640, 480)
        cb.saveToFile(File("output.jpg"))
    }

    @Text
    """
    When repeatedly saving color buffers asynchronously (the default) it is possible to run out
    of memory. This can happen if the software can not save images at the requested frame rate.
    In such situations we can either set `async = false` in `saveToFile()` or avoid `saveToFile` and use the
    [VideoWriter](https://guide.openrndr.org/extensions/writingToVideoFiles.html#writing-to-video-using-render-targets)
    together with [pngSequence](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-video-profiles#png-sequence)
    or [tiffSequence](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-video-profiles#tiff-sequence)
    instead.
     
    Note that some image file types take longer to save than others.

    ## Copying between color buffers
    
    Color buffer contents can be copied using the `copyTo` member function. 
    Copying works between color buffers of different formats and types.
    """

    @Code.Block
    run {
        // -- create color buffers
        val cb0 = colorBuffer(640, 480, type = ColorType.FLOAT16)
        val cb1 = colorBuffer(640, 480, type = ColorType.FLOAT32)
        //-- copy contents of cb0 to cb1
        cb0.copyTo(cb1)
    }

    @Text
    """
    ## Writing into color buffers 
    
    To upload data into the color buffer one uses the `write` member function.
    """

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

    @Text
    """
    ## Reading from color buffers 
    
    To download data from a color buffer one uses the `read` member function.
    """

    @Code.Block
    run {
        // -- create a color buffer that uses 8 bits per channel (the default)
        val cb = colorBuffer(640, 480, type = ColorType.UINT8)

        // -- create a buffer (on CPU) that matches size and layout of the color buffer
        val buffer = ByteBuffer.allocateDirect(cb.width * cb.height * cb.format.componentCount * cb.type.componentSize)

        // -- download data into buffer
        cb.read(buffer)

        // -- read the first UINT8 pixel's color
        val r = buffer[0].toUByte().toDouble() / 255.0
        val g = buffer[1].toUByte().toDouble() / 255.0
        val b = buffer[2].toUByte().toDouble() / 255.0
        val a = buffer[3].toUByte().toDouble() / 255.0
        val c = rgb(r, g, b, a)
    }

    @Text
    """
    ## Color buffer shadows
    
    To simplify the process of reading and writing from and to color buffers 
    we added a shadow buffer to
    `ColorBuffer`. A shadow buffer offers a simple interface to access the 
    color buffer's contents.

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



