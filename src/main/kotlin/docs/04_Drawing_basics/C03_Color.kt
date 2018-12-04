package docs.`04_Drawing_basics`

import org.openrndr.application
import org.openrndr.color.ColorHSLa
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.dokgen.annotations.*
import org.openrndr.extensions.SingleScreenshot


fun main(args: Array<String>) {

    @Text """
# Color
    """.trimIndent()

    @Text """
In this chapter we discuss and demonstrate OPENRNDR's color functionality.
    """.trimIndent()


    @Text """
## Basic color

OPENRNDR primarily uses red-green-blue(-alpha) color stored in `ColorRGBa` instances. `ColorRGBa`'s channels store values in the range [0, 1].
"""

    @Text """### Predefined colors"""

    @Code.Block
    run {
        ColorRGBa.BLACK
        ColorRGBa.WHITE
        ColorRGBa.RED
        ColorRGBa.GREEN
        ColorRGBa.BLUE
        ColorRGBa.YELLOW
        ColorRGBa.GRAY
        ColorRGBa.PINK
    }

    @Text """### Conversion from hex color
RGB color is commonly communicated in hexadecimal codes. `ColorRGBa` provides simple tools to construct color from such
hexadecimal codes.
    """.trimMargin()
    @Code.Block
    run {
        // -- construct the OPENRNDR pink from hexadecimal code
        val color = ColorRGBa.fromHex(0xffc0cb)
    }

    @Text """### Color operations
The `ColorRGBa` class offers a number of tools to create variations of colors. For example `ColorRGBa.shade` can be
used to create lighter or darker shades of a base color.
""".trimMargin()


    @Media.Image """
media/color-001.png
    """

    @Application

    application {
        @Exclude
        configure {
            width = 770
            height = 196
        }

        program {

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/color-001.png"
            }
            @Code
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
        }
    }

    @Text """
Using `ColorRGBa.opacify` colors can be made more or less opaque.
    """.trimIndent()


    @Media.Image """
media/color-002.png
    """

    @Application

    application {
        @Exclude
        configure {
            width = 770
            height = 160
        }

        program {

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/color-002.png"
            }
            @Code
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
        }
    }

    @Text """
Using `mix(ColorRGBa, ColorRGBa, Double)` colors can be mixed.
    """.trimIndent()


    @Media.Image """
media/color-003.png
    """

    @Application

    application {
        @Exclude
        configure {
            width = 770
            height = 128
        }

        program {

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/color-003.png"
            }
            @Code
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
        }
    }

    @Text """## Alternative color models
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
"""



    @Text """## HSV, HSL, XSV and XSL color"""


    @Media.Image """
media/color-004.png
    """


    @Application
    application {


        configure {
            width = 770
            height = 800
        }

        program {

            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/color-004.png"
            }

            @Code
            extend {
                drawer.stroke = null

                // -- draw hsv swatches
                for (j in 0..7) {
                    for (i in 0..15) {
                        drawer.fill = ColorHSVa( 360 * (i/15.0), 0.7, 0.125 + j/8.0).toRGBa()
                        drawer.rectangle(35.0 + (700 / 16.0) * i, 32.0 + j * 16.0, (700 / 16.0), 16.0)
                    }
                }

                // -- draw hsl swatches
                drawer.translate(0.0, 160.0)
                for (j in 0..7) {
                    for (i in 0..15) {
                        drawer.fill = ColorHSLa( 360 * (i/15.0), 0.7, 0.125 + j/8.0).toRGBa()
                        drawer.rectangle(35.0 + (700 / 16.0) * i, 32.0 + j * 16.0, (700 / 16.0), 16.0)
                    }
                }

                // -- draw xsv (Kuler) swatches
                drawer.translate(0.0, 160.0)
                for (j in 0..7) {
                    for (i in 0..15) {
                        drawer.fill = ColorHSLa( 360 * (i/15.0), 0.7, 0.125 + j/8.0).toRGBa()
                        drawer.rectangle(35.0 + (700 / 16.0) * i, 32.0 + j * 16.0, (700 / 16.0), 16.0)
                    }
                }

                // -- draw xsl (Kuler) swatches
                drawer.translate(0.0, 160.0)
                for (j in 0..7) {
                    for (i in 0..15) {
                        drawer.fill = ColorHSLa( 360 * (i/15.0), 0.7, 0.125 + j/8.0).toRGBa()
                        drawer.rectangle(35.0 + (700 / 16.0) * i, 32.0 + j * 16.0, (700 / 16.0), 16.0)
                    }
                }

            }
        }
    }
}