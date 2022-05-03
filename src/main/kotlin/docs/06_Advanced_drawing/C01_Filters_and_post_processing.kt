@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Filters and post processing")
@file:ParentTitle("Advanced drawing")
@file:Order("110")
@file:URL("advancedDrawing/filtersAndPostProcessing")

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*
import org.openrndr.extra.fx.blur.BoxBlur
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text 
    """
    # Filters and Post-processing
    
    Since OPENRNDR has extensive support for rendering to off-screen buffers it is easy to apply effects and filters
    to the off-screen buffers.
    
    ## Basic usage
    
    To demonstrate the ease of using filters we show an example of applying a blur filter to a drawing on a render target.
    """

    @Media.Video "../media/filters-001.mp4"

    @Application
    @ProduceVideo("media/filters-001.mp4", 6.28318, 60)
    @Code
    application {
        configure {
            width = 770
            height = 578
        }
        program {
            // -- create offscreen render target
            val offscreen = renderTarget(width, height) {
                colorBuffer()
                depthBuffer()
            }
            // -- create blur filter
            val blur = BoxBlur()

            // -- create colorbuffer to hold blur results
            val blurred = colorBuffer(width, height)


            extend {
                // -- draw to offscreen buffer
                drawer.isolatedWithTarget(offscreen) {
                    clear(ColorRGBa.BLACK)
                    fill = ColorRGBa.PINK
                    stroke = null
                    circle(
                        cos(seconds) * 100.0 + width / 2,
                        sin(seconds) * 100.0 + height / 2.0,
                        100.0 + 100.0 * cos(seconds * 2.0)
                    )
                }
                // -- set blur parameters
                blur.window = 30

                // -- blur offscreen's color buffer into blurred
                blur.apply(offscreen.colorBuffer(0), blurred)
                drawer.image(blurred)
            }
        }
    }

    @Text 
    """
    ## Writing your own filters
    
    You may be wondering how to create your own filters. If so, good news, 
    it is fairly easy to write your own filter if you are familiar with 
    fragment shaders in GLSL. The easiest way to write your own filter is 
    to use the `Filter` class by extending it. The `Filter` class takes care 
    of setting up render state, geometry and projections so all you have
    to do is write a shader.

    What follows is an example of how to create a Filter from a shader whose 
    code is stored as a String. The filter we will be making is a simple
    noise filter.
    """

    @Media.Image "../media/filters-002.jpg"

    @Application
    @ProduceScreenshot("media/filters-002.jpg")
    @Code
    application {
        val noiseShader = """
        #version 330
        // -- part of the filter interface, every filter has these
        in vec2 v_texCoord0;
        uniform sampler2D tex0;
        out vec4 o_color;

        // -- user parameters
        uniform float gain;
        uniform float time;

        #define HASHSCALE 443.8975
        vec2 hash22(vec2 p) {
            vec3 p3 = fract(vec3(p.xyx) * HASHSCALE);
            p3 += dot(p3, p3.yzx+19.19);
            return fract(vec2((p3.x + p3.y)*p3.z, (p3.x+p3.z)*p3.y));
        }

        void main() {
            float n = hash22(v_texCoord0+vec2(time)).x;
            // here we read from the input image and add noise
            vec4 color = texture(tex0, v_texCoord0) + vec4(vec3(n), 0.0) * gain;
            o_color = color;
        }
        """

        class Noise : Filter(filterShaderFromCode(noiseShader, "noise-shader")) {
            // -- note the 'by parameters' here, this is what wires the fields up to the uniforms
            var gain: Double by parameters
            var time: Double by parameters

            init {
                gain = 1.0
                time = 0.0
            }
        }
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
            // -- create the noise filter
            val noise = Noise()
            val offscreen = renderTarget(width, height) {
                colorBuffer()
                depthBuffer()
            }

            extend {
                // -- draw to offscreen buffer
                drawer.isolatedWithTarget(offscreen) {
                    clear(ColorRGBa.BLACK)
                    fill = ColorRGBa.PINK
                    stroke = null
                    circle(
                        cos(seconds) * 100.0 + width / 2,
                        sin(seconds) * 100.0 + height / 2.0,
                        100.0 + 100.0 * cos(seconds * 2.0)
                    )
                }
                // apply the noise on and to offscreen.colorBuffer(0),
                // this only works for filters that only read from
                // the current fragment.
                noise.time = seconds
                noise.gain = 1.0
                noise.apply(
                    offscreen.colorBuffer(0),
                    offscreen.colorBuffer(0)
                )

                drawer.image(offscreen.colorBuffer(0))
            }
        }
    }

    @Text 
    """
    ## The orx-fx library
    
    A repository of ready-to-use filters for OPENRNDR can be found in 
    the [ORX repository](https://github.com/openrndr/orx/tree/master/orx-fx),
    a partial index of the filters can be found in the 
    [orx-fx chapter](https://guide.openrndr.org/OPENRNDRExtras/imageFilters.html)
    """
}