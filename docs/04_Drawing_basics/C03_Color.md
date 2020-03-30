 
 # Color 
 
 In this chapter we discuss and demonstrate OPENRNDR's color functionality. 
 
 ## Basic color

OPENRNDR primarily uses red-green-blue(-alpha) color stored in `ColorRGBa` instances. `ColorRGBa`'s channels store values in the range [0, 1]. 
 
 ### Predefined colors 
 
 ```kotlin
ColorRGBa.BLACK
ColorRGBa.WHITE
ColorRGBa.RED
ColorRGBa.GREEN
ColorRGBa.BLUE
ColorRGBa.YELLOW
ColorRGBa.GRAY
ColorRGBa.PINK
``` 
 
 ### Custom colors 
 
 Custom colors can be made using either the ColorRGBa constructor, or the `rgb` and `rgba` functions. Both use
value ranges between 0.0 and 1.0. 
 
 ```kotlin
// -- using the ColorRGBa constructor
val red = ColorRGBa(1.0, 0.0, 0.0)
val green = ColorRGBa(0.0, 1.0, 0.0)
val blue = ColorRGBa(0.0, 0.0, 1.0)
val blueOpaque = ColorRGBa(0.0, 0.0, 1.0, 0.5)

// -- using the rgb and rgba functions
val magenta = rgb(1.0, 0.0, 1.0)
val magentaOpaque = rgba(1.0, 0.0, 1.0, 0.5)
``` 
 
 ### Conversion from hex color
RGB color is commonly communicated in hexadecimal codes. `ColorRGBa` provides simple tools to construct color from such
hexadecimal codes. 
 
 ```kotlin
// -- construct the OPENRNDR pink from hexadecimal code, using an integer argument
val color1 = ColorRGBa.fromHex(0xffc0cb)
// -- construct the OPENRNDR pink from hexidecimal code, using a string argument, the leading # is optional
val color2 = ColorRGBa.fromHex("#ffc0cb")
``` 
 
 ### Color operations
The `ColorRGBa` class offers a number of tools to create variations of colors. For example `ColorRGBa.shade` can be
used to create lighter or darker shades of a base color. 
 
 <img src="media/color-001.png"/> 
 
 ```kotlin
extend {
    drawer.stroke = null
    val baseColor = ColorRGBa.PINK
    // -- draw 16 darker shades of pink
    for (i in 0..15) {
        drawer.fill = baseColor.shade(i / 15.0)
        drawer.rectangle(35.0 + (700 / 16.0) * i, 32.0, (700 / 16.0), 64.0)
    }
    // -- draw 16 lighter shades of pink
    for (i in 0..15) {
        drawer.fill = baseColor.shade(1.0 + i / 15.0)
        drawer.rectangle(35.0 + (700 / 16.0) * i, 96.0, (700 / 16.0), 64.0)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C03_Color000.kt) 
 
 Using `ColorRGBa.opacify` colors can be made more or less opaque. 
 
 <img src="media/color-002.png"/> 
 
 ```kotlin
extend {
    drawer.stroke = null
    val baseColor = ColorRGBa.PINK
    
    drawer.fill = ColorRGBa.GRAY.shade(0.5)
    drawer.rectangle(35.0, 32.0, 700.0, 64.00)
    
    // -- draw 16 darker shades of pink
    for (i in 0..15) {
        drawer.fill = baseColor.opacify(i / 15.0)
        drawer.rectangle(35.0 + (700 / 16.0) * i, 64.0, (700 / 16.0), 64.0)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C03_Color001.kt) 
 
 Using `mix(ColorRGBa, ColorRGBa, Double)` colors can be mixed. 
 
 <img src="media/color-003.png"/> 
 
 ```kotlin
extend {
    drawer.stroke = null
    val leftColor = ColorRGBa.PINK
    val rightColor = ColorRGBa.fromHex(0xFC3549)
    
    // -- draw 16 color mixes
    for (i in 0..15) {
        drawer.fill = mix(leftColor, rightColor, i / 15.0)
        drawer.rectangle(35.0 + (700 / 16.0) * i, 32.0, (700 / 16.0), 64.0)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C03_Color002.kt) 
 
 ## Alternative color models
OPENRNDR offers a wide range of alternative color models. The alternative models use primaries different from red, green
and blue.

Class name    | Color space description
--------------|---------------------------------------
`ColorRGBa`   | sRGB and linear RGB
`ColorHSVa`   | Hue, saturation, value
`ColorHSLa`   | Hue, saturation, lightness
`ColorXSVa`   | Xue, saturation, value, _Kuler_-like colorspace
`ColorXSLa`   | Xue, saturation, lightness, _Kuler_-like colorspace
`ColorXYZa`   | CIE XYZ colorspace
`ColorYxya`   | CIE Yxy colorspace
`ColorLABa`   | LAB colorspace
`ColorLCHABa` | LCHab colorspace, a cylindrical variant of LAB
`ColorLSHABa` | LSHab colorspace, a cylindrical variant of LAB, chroma replaced with normalized saturation
`ColorLUVa`   | LUV colorspace
`ColorLCHUVa` | LCHuv colorspace, a cylindrical variant of LUV
`ColorLSHUVa` | LSHuv colorspace, a cylindrical variant of LUV, chroma replaced with normalized saturation
`ColorATVa`   | Coloroid color space, partial implementation 
 
 ## HSV, HSL, XSV and XSL color 
 
 HSV (hue-saturation-value) and HSL ("hue-saturation-lightness") are cylindrical color spaces.

XSV and XSL (for lack of a better name) are transformed versions of HSV and HSL in which the hue component has been
stretched and compressed to make the color space better suited for artists. The spaces are better suited for artists
because it has red-green and blue-yellow primaries. The XSV and XSL spaces are based on (if not
the same as) the Adobe Kuler color spaces.

Below is an example of plots of color swatches for (from top to bottom) HSV, HSL, XSV and XSL. The adjusted hue of the
XSV and XSL spaces is clearly visible. 
 
 <img src="media/color-004.png"/> 
 
 ```kotlin
extend {
    drawer.stroke = null
    
    // -- draw hsv swatches
    for (j in 0..7) {
        for (i in 0..31) {
            drawer.fill = ColorHSVa(360 * (i / 31.0), 0.7, 0.125 + j / 8.0).toRGBa()
            drawer.rectangle(35.0 + (700 / 32.0) * i, 32.0 + j * 16.0, (700 / 32.0), 16.0)
        }
    }
    
    // -- draw hsl swatches
    drawer.translate(0.0, 160.0)
    for (j in 0..7) {
        for (i in 0..31) {
            drawer.fill = ColorHSLa(360 * (i / 31.0), 0.7, 0.125 + j / 9.0).toRGBa()
            drawer.rectangle(35.0 + (700 / 32.0) * i, 32.0 + j * 16.0, (700 / 32.0), 16.0)
        }
    }
    
    // -- draw xsv (Kuler) swatches
    drawer.translate(0.0, 160.0)
    for (j in 0..7) {
        for (i in 0..31) {
            drawer.fill = ColorXSVa(360 * (i / 31.0), 0.7, 0.125 + j / 8.0).toRGBa()
            drawer.rectangle(35.0 + (700 / 32.0) * i, 32.0 + j * 16.0, (700 / 32.0), 16.0)
        }
    }
    
    // -- draw xsl (Kuler) swatches
    drawer.translate(0.0, 160.0)
    for (j in 0..7) {
        for (i in 0..31) {
            drawer.fill = ColorXSLa(360 * (i / 31.0), 0.7, 0.125 + j / 9.0, 1.0).toRGBa()
            drawer.rectangle(35.0 + (700 / 32.0) * i, 32.0 + j * 16.0, (700 / 32.0), 16.00)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C03_Color003.kt) 
