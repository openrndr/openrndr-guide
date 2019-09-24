 
 # Filters and Post-processing

Since OPENRNDR has extensive support for rendering to off-screen buffers it is easy to apply effects and filters
to the off-screen buffers.


## Basic usage
To demonstrate the ease of using filters we show an example of applying a blur filter to a drawing on a render target. 
 
 <video controls>
    <source src="media/filters-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
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
            background(ColorRGBa.BLACK)
            fill = ColorRGBa.PINK
            stroke = null
            circle(Math.cos(seconds) * 100.0 + width / 2, Math.sin(seconds) * 100.0 + height / 2.0, 100.0 + 100.0 * Math.cos(seconds * 2.0))
        }
        // -- set blur parameters
        blur.window = 30
        
        // -- blur offscreen's color buffer into blurred
        blur.apply(offscreen.colorBuffer(0), blurred)
        drawer.image(blurred)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/06_Advanced_drawing/C01_Filters_and_post_processing000.kt) 
 
 ## Writing your own filters 
 
 You may be wondering how to create your own filters. If so, good news, it is fairly easy to write your own
filter if you are familiar with fragment shaders in GLSL. The easiest way to write your own filter is to use the `Filter`
class by extending it. The `Filter` class takes care of setting up render state, geometry and projections so all you have
to do is write a shader.

What follows is an example of how to create a Filter from a shader whose code is stored as a String. The filter
we will be making is a simple noise filter. 
 
 <img src="media/filters-002.png"/> 
 
 ```kotlin
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
    
    class Noise : Filter(filterShaderFromCode(noiseShader)) {
        // -- note the 'by parameters' here, this is what wires the fields up to the uniforms
        var gain: Double by parameters
        var time: Double by parameters
        init {
            gain = 1.0
            time = 0.0
        }
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
                background(ColorRGBa.BLACK)
                fill = ColorRGBa.PINK
                stroke = null
                circle(Math.cos(seconds) * 100.0 + width / 2, Math.sin(seconds) * 100.0 + height / 2.0, 100.0 + 100.0 * Math.cos(seconds * 2.00))
            }
            // apply the noise on and to offscreen.colorBuffer(0),
            // this only works for filters that only read from
            // the current fragment.
            noise.time = seconds
            noise.gain = 1.0
            noise.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            
            drawer.image(offscreen.colorBuffer(0))
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/06_Advanced_drawing/C01_Filters_and_post_processing001.kt) 
 
 ## The openrndr-filter library

The `openrndr-filter` library holds numerous filters that can be applied easily.

First off: add a dependency to the library to your build.gradle dependencies list.
```groovy
compile "org.openrndr:openrndr-filter:$openrndr_version"
```

The library contains the following filters

##### Blur filters

Class name                | Description
--------------------------|-------------------------------------------------------
`BoxBlur`                 | Simple box blur, implemented as separable convolutions
`GaussianBlur`            | Gaussian blur, exact by slow implementation
`ApproximateGuassianBlur` | Gaussian, approximate but faster implementation
`HashBlur`                | Fuzzy blur


##### Color management filters

 Class name          | Description
---------------------|--------------------------------------------------
 `Linearize`         | convert colors from sRGB to linear RGB
 `Delinearize`       | convert colors from linear RGB to sRGB
 `TonemapUncharted2` | convert colors from linear RGB to tonemapped sRGB

##### Blending filters

Class name   | Description
-------------|------------------------------------------------------
`ColorBurn`  | implements blending similar to color burn in Photoshop
`ColorDodge` | implements blending similar to color dodge in Photoshop
`Darken`     | implements blending similar to darken Photoshop
`Hardlight`  | implements blending similar to hard light in Photoshop
`Lighten`    | implements blending similar to lighten in Photoshop
`Multiply`   | implements multiply blending
`Normal`     | implements normal alpha blending
`Overlay`    | implements blending similar to overlay in Photoshop
`Screen`     | implements blending similar to screen in Photoshop
`Add`        | implements additive blur

##### Dither filters

 Class name | Description
------------|-------------------
 `ADither`  | A Dithering filter
 
