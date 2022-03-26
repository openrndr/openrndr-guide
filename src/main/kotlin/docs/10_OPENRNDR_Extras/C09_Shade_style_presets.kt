@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Shade style presets")
@file:ParentTitle("OPENRNDR Extras")
@file:Order("190")
@file:URL("OPENRNDRExtras/shadeStylePresets")

package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extra.shadestyles.angularGradient
import org.openrndr.extra.shadestyles.halfAngularGradient
import org.openrndr.extra.shadestyles.linearGradient
import org.openrndr.extra.shadestyles.radialGradient
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text "# Shade style presets"
    @Text """The `orx-shade-styles` library provides a number of preset [shade styles](06_Advanced_drawing/C04_Shade_styles)"""

    @Text "## Prerequisites"
    @Text """Assuming you are working on an [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
project, all you have to do is enable `orx-shade-styles` in the `orxFeatures`
 set in `build.gradle.kts` and reimport the gradle project."""

    @Text "## linearGradient"
    @Media.Video """media/shade-style-presets-001.mp4"""

    @Application
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            @Exclude
            extend(ScreenRecorder()) {
                outputFile = "media/shade-style-presets-001.mp4"
                maximumDuration = 10.0
                quitAfterMaximum
            }
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/IBMPlexMono-Bold.ttf", 144.0)
            extend {
                drawer.shadeStyle = linearGradient(ColorRGBa.PINK, ColorRGBa.RED, rotation = seconds * 18.0)
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0,90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200/480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

    @Text "## radialGradient"
    @Media.Video """media/shade-style-presets-002.mp4"""

    @Application
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            @Exclude
            extend(ScreenRecorder()) {
                outputFile = "media/shade-style-presets-002.mp4"
                maximumDuration = 10.0
                quitAfterMaximum
            }
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/IBMPlexMono-Bold.ttf", 144.0)
            extend {
                drawer.shadeStyle = radialGradient(ColorRGBa.RED, ColorRGBa.PINK, length = 0.5, offset = Vector2(cos(seconds*PI), sin(seconds*PI*0.5)))
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0,90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200/480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

    @Text "## angularGradient"
    @Media.Video """media/shade-style-presets-003.mp4"""

    @Application
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            @Exclude
            extend(ScreenRecorder()) {
                outputFile = "media/shade-style-presets-003.mp4"
                maximumDuration = 10.0
                quitAfterMaximum
            }
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/IBMPlexMono-Bold.ttf", 144.0)
            extend {
                drawer.shadeStyle = angularGradient(ColorRGBa.RED, ColorRGBa.PINK, rotation = seconds * 18.0)
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0,90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200/480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

    @Text "## halfAngularGradient"
    @Media.Video """media/shade-style-presets-004.mp4"""

    @Application
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            @Exclude
            extend(ScreenRecorder()) {
                outputFile = "media/shade-style-presets-004.mp4"
                maximumDuration = 10.0
                quitAfterMaximum
            }
            val image = loadImage("data/images/cheeta.jpg")
            val font = loadFont("data/IBMPlexMono-Bold.ttf", 144.0)
            extend {
                drawer.shadeStyle = halfAngularGradient(ColorRGBa.RED, ColorRGBa.PINK, rotation = seconds * 18.0)
                drawer.rectangle(80.0, 40.0, 200.0, 200.0)
                drawer.circle(180.0, 340.0,90.0)
                drawer.image(image, 300.0, 40.0, 640 * (200/480.0), 200.0)
                drawer.fontMap = font
                drawer.text("OPEN", 300.0, 340.0)
                drawer.text("RNDR", 300.0, 420.0)
            }
        }
    }

}