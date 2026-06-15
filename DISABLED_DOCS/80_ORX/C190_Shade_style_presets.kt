@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Shade style presets")
@file:ParentTitle("ORX")
@file:Order("190")
@file:URL("ORX/shadeStylePresets")

package docs.`80_ORX`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extra.shadestyles.angularGradient
import org.openrndr.extra.shadestyles.halfAngularGradient
import org.openrndr.extra.shadestyles.linearGradient
import org.openrndr.extra.shadestyles.radialGradient

import org.openrndr.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text """
    # Shade style presets

    The `orx-shade-styles` library provides a number of preset 
    [shade styles](https://guide.openrndr.org/drawing/shadeStyles.html)
    
    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-shade-styles` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    ## linearGradient
    """

    @Media.Video "../media/shade-style-presets-001.mp4"

    @Application
    @ProduceVideo("media/shade-style-presets-001.mp4", 6.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/fonts/default.otf", 144.0)
            extend {
                drawer.shadeStyle = linearGradient(ColorRGBa.PINK, ColorRGBa.RED, rotation = seconds * 60.0)
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0, 90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200 / 480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

    @Text """
    ## radialGradient
    """

    @Media.Video "../media/shade-style-presets-002.mp4"

    @Application
    @ProduceVideo("media/shade-style-presets-002.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/fonts/default.otf", 144.0)
            extend {
                drawer.shadeStyle = radialGradient(ColorRGBa.RED, ColorRGBa.PINK, length = 0.5, offset = Vector2(cos(seconds), sin(seconds*0.5)))
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0, 90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200 / 480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

    @Text """
    ## angularGradient
    """

    @Media.Video "../media/shade-style-presets-003.mp4"

    @Application
    @ProduceVideo("media/shade-style-presets-003.mp4", 6.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/fonts/default.otf", 144.0)
            extend {
                drawer.shadeStyle = angularGradient(ColorRGBa.RED, ColorRGBa.PINK, rotation = seconds * 60.0)
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0, 90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200 / 480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

    @Text """
    ## halfAngularGradient
    """

    @Media.Video "../media/shade-style-presets-004.mp4"

    @Application
    @ProduceVideo("media/shade-style-presets-004.mp4", 6.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/fonts/default.otf", 144.0)
            extend {
                drawer.shadeStyle = halfAngularGradient(ColorRGBa.RED, ColorRGBa.PINK, rotation = seconds * 60.0)
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0, 90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200 / 480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }
}
