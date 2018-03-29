# Filters and Post-processing

Since OPENRNDR has extensive support for rendering to off-screen buffers it is easy to apply effecs and filters
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

Using the `Filter` class
