package docs.`10_OPENRNDR_Extras`


import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.ColorType
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.extra.jumpfill.DistanceField
import org.openrndr.extra.jumpfill.fx.InnerGlow
import org.openrndr.extra.jumpfill.fx.OuterGlow
import org.openrndr.ffmpeg.ScreenRecorder
import kotlin.math.PI
import kotlin.math.cos


fun main() {
    @Text """# Distance fields using orx-jumpflood """

    @Text "## Prerequisites"
    @Text """Assuming you are working on an [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
project, all you have to do is add "orx-jumpflood" to the `orxFeatures` set in `build.gradle.kts`. Make sure to 
reimport the gradle project after adding the feature."""


    @Text "## Distance field visualization"

    @Media.Video """media/distance-fields-001.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            configure {
                width = 640
                height = 480
            }

            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.00
                outputFile = "media/distance-fields-001.mp4"
            }
            val image = loadImage("data/images/cheeta.jpg")

            val c = compose {
                layer {
                    colorType = ColorType.FLOAT32

                    draw {
                        drawer.image(image)
                    }
                    post(DistanceField()) {
                        threshold = cos(seconds * PI * 0.25) * 0.5 + 0.5
                        distanceScale = 0.008
                    }
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }

    @Text "## Outer glow"

    @Text """orx-jumpflood comes with a filter that creates Photoshop-style outer glow effect."""

    @Media.Video """media/distance-fields-101.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            configure {
                width = 640
                height = 480
            }
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/distance-fields-101.mp4"
            }
            val c = compose {
                layer {
                    draw {
                        drawer.fill = ColorRGBa.PINK
                        drawer.circle(width / 2.0, height / 2.0, 200.0)
                    }
                    post(OuterGlow()) {
                        this.width = (cos(seconds * PI) * 0.5 + 0.5) * 100.0
                    }
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }

    @Text "## Inner glow"

    @Text """Similar to the outer glow effect, but the glow is placed in the inside of the shape."""

    @Media.Video """media/distance-fields-102.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            configure {
                width = 640
                height = 480
            }
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.00
                outputFile = "media/distance-fields-102.mp4"
            }
            val c = compose {
                layer {
                    draw {
                        drawer.fill = ColorRGBa.PINK
                        drawer.circle(width / 2.0, height / 2.0, 200.0)
                    }
                    post(InnerGlow()) {
                        this.width = (cos(seconds * PI) * 0.5 + 0.5) * 100.0
                    }
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }

    @Text "## Sampling distance"

    @Media.Video """media/distance-fields-002.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            configure {
                width = 640
                height = 480
            }

            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/distance-fields-002.mp4"
            }
            val image = loadImage("data/images/cheeta.jpg")

            val c = compose {
                // -- make sure accumulation is done in float32
                colorType = ColorType.FLOAT32
                layer {
                    colorType = ColorType.FLOAT32
                    draw {
                        drawer.image(image)
                    }
                    post(DistanceField()) {
                        threshold = cos(seconds * PI * 0.25) * 0.5 + 0.5
                        distanceScale = 1.0
                    }
                }
                layer {
                    draw {
                        // -- use the accumulation buffer to get the distance field
                        accumulation?.let {
                            val s = it.shadow
                            s.download()
                            drawer.fill = ColorRGBa.PINK
                            drawer.stroke = null
                            for (y in 0 until height step 10) {
                                for (x in 0 until width step 10) {
                                    val distance = s[x, y].r
                                    drawer.circle(x * 1.0, y * 1.0, distance * 0.05)
                                }
                            }
                            // -- clear the accumulated contents
                            it.fill(ColorRGBa.TRANSPARENT)
                        }
                    }
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }


}