package docs.`06_Advanced_drawing`

import org.openrndr.dokgen.annotations.Text

private val openrndr_version = ""
fun main(args: Array<String>) {
    @Text """
# Filters and Post-processing

Since OPENRNDR has extensive support for rendering to off-screen buffers it is easy to apply effects and filters
to the off-screen buffers.

The formula for applying filters is roughly

```kotlin
    val filtered: ColorBuffer
    val rt: RenderTarget
    val filter: BoxBlur

    // -- [...]

    drawer.withTarget(rt) {
        // -- draw something
    }
    filter.apply(rt.colorBuffer(0), filtered)
    drawer.image(filtered)
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/filters-001/src/main/kotlin/Example.kt)

## The openrndr-filter library

The `openrndr-filter` library holds numerous filters that can be applied easily.

First off: add a dependency to the library to your build.gradle dependencies list.
```groovy
compile "org.openrndr:openrndr-filter:$openrndr_version"
```

The library contains the following filters

Blur filters

Class name                | Description
--------------------------|-------------------------------------------------------
`BoxBlur`                 | Simple box blur, implemented as separable convolutions
`GaussianBlur`            | Gaussian blur, exact by slow implementation
`ApproximateGuassianBlur` | Gaussian, approximate but faster implementation
`HashBlur`                | Fuzzy blur


Color management filters

 Class name          | Description
---------------------|--------------------------------------------------
 `Linearize`         | convert colors from sRGB to linear RGB
 `Delinearize`       | convert colors from linear RGB to sRGB
 `TonemapUncharted2` | convert colors from linear RGB to tonemapped sRGB

Blending filters

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

Dither filters

 Class name | Description
------------|-------------------
 `ADither`  | A Dithering filter

Advanced filters

Class name      | Description
----------------|-------------------------------------------------------------------
`SSAO`          | Screen space ambient occlusion
`SSLR`          | Screen space local reflections
`HexDof`        | Depth of field with hexagonal bokeh
`PositionToCoc` | Converts color and positions to premultiplied circle of confusion
`VelocityBlur`  | Blurs premultiplied coc map according to velocity map

## Writing custom filters

### Defining the filter class
```kotlin
class Noise : Filter(filterShaderFromUrl("file:data/shaders/noise.frag")) {
    var gain: Double by parameters
    var time: Double by parameters

    init {
        gain = 1.0
    }
}
```

### Writing the filter shader

```glsl
#version 330

// -- part of the filter interface
in vec2 v_texCoord0;
uniform sampler2D tex0;


// -- user parameters
uniform float gain;
uniform float time;

// -- default output
out vec4 o_color;

#define HASHSCALE 443.8975
vec2 hash22(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * HASHSCALE);
    p3 += dot(p3, p3.yzx+19.19);
    return fract(vec2((p3.x + p3.y)*p3.z, (p3.x+p3.z)*p3.y));
}

void main() {
    float n = hash22(v_texCoord0+vec2(time)).x;
    vec4 color = texture(tex0, v_texCoord0) + vec4(vec3(n), 0.0) * gain;
    o_color = color;
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/filters-002/src/main/kotlin/Example.kt)


    """.trimIndent()
}