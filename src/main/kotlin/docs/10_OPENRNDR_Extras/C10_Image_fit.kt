package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.loadImage
import org.openrndr.extras.imageFit.FitMethod
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.ffmpeg.ScreenRecorder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text "# Image fit"
    @Text """`orx-image-fit` provides functionality to making the drawing and placement of images somewhat easier. 
`orx-image` Fits images in frames with two options, contain and cover, similar to CSS object-fit."""

    @Text "## Prerequisites"
    @Text """Assuming you are working on an [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
project, all you have to do is enable `orx-image-fit` in the `orxFeatures`
 set in `build.gradle.kts` and reimport the gradle project."""

    @Text "## Contain mode"

    @Media.Video """media/image-fit-001.mp4"""
    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/image-fit-001.mp4"
            }
            val image = loadImage("data/images/cheeta.jpg")
            extend {
                val margin = cos(seconds * PI) * 50.0 + 50.0
                drawer.imageFit(image, 20.0, 20.0 + margin / 2, width - 40.0, height - 40.0 - margin, fitMethod = FitMethod.Contain)
                // -- illustrate the placement rectangle
                drawer.fill = null
                drawer.stroke = ColorRGBa.WHITE
                drawer.rectangle(20.0, 20.0 + margin / 2.0, width - 40.0, height - 40.0 - margin)
            }
        }
    }

    @Text """Additionally the placement of the image in the rectangle can be adjusted"""

    @Media.Video """media/image-fit-002.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.00
                outputFile = "media/image-fit-002.mp4"
            }
            val image = loadImage("data/images/cheeta.jpg")
            extend {
                val margin = 100.0
                drawer.imageFit(image, 20.0, 20.0 + margin / 2, width - 40.0, height - 40.0 - margin, horizontalPosition = cos(seconds) * 1.0,  fitMethod = FitMethod.Contain)
                // -- illustrate the placement rectangle
                drawer.fill = null
                drawer.stroke = ColorRGBa.WHITE
                drawer.rectangle(20.0, 20.0 + margin / 2.0, width - 40.0, height - 40.0 - margin)
            }
        }
    }


    @Text "## Cover mode"
    @Media.Video """media/image-fit-101.mp4"""
    @Application
    @Code
    application {
        program {

            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/image-fit-101.mp4"
            }
            val image = loadImage("data/images/cheeta.jpg")
            extend {
                // -- calculate dynamic margins
                val xm = cos(seconds * PI) * 50.0 + 50.0
                val ym = sin(seconds * PI) * 50.0 + 50.0

                drawer.imageFit(image, 20.0 + xm / 2.0, 20.0 + ym / 2, width - 40.0 - xm, height - 40.0 - ym)

                // -- illustrate the placement rectangle
                drawer.fill = null
                drawer.stroke = ColorRGBa.WHITE
                drawer.rectangle(20.0 + xm / 2.0, 20.0 + ym / 2.0, width - 40.0 - xm, height - 40.0 - ym)
            }
        }
    }
    @Media.Video """media/image-fit-102.mp4"""

    @Text """Additionally the placement of the image in the rectangle can be adjusted"""
    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.00
                outputFile = "media/image-fit-102.mp4"
            }
            val image = loadImage("data/images/cheeta.jpg")
            extend {
                val margin = 100.0
                drawer.imageFit(image, 20.0, 20.0 + margin / 2, width - 40.0, height - 40.0 - margin, verticalPosition = cos(seconds) * 1.0)

                // -- illustrate the placement rectangle
                drawer.fill = null
                drawer.stroke = ColorRGBa.WHITE
                drawer.rectangle(20.0, 20.0 + margin / 2.0, width - 40.0, height - 40.0 - margin)
            }
        }
    }
}