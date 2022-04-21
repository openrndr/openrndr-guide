@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Poisson fills")
@file:ParentTitle("OPENRNDR Extras")
@file:Order("210")
@file:URL("OPENRNDRExtras/poissonFills")

package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.color.LumaOpacity

import org.openrndr.poissonfill.PoissonBlend
import org.openrndr.poissonfill.PoissonFill
import org.openrndr.shape.Rectangle
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    @Text """
    # Poisson fills
    
    orx-poisson-fills offers tool for GPU-based Poisson operations. 
    
    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-poisson-fills` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    # Filling
    
    The `PoissonFill` filter can be used to fill in transparent parts of 
    an image. In the following example we use `orx-compose` to simplify the 
    code a bit. The same results can be achieved using render targets.
    """

    @Media.Video "media/poisson-fills-001.mp4"

    @Application
    @ProduceVideo("media/poisson-fills-001.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 770
        }
        program {
            val c = compose {
                layer {
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.RED
                        drawer.circle(
                            (cos(seconds) * 0.5 + 0.5) * width,
                            (sin(seconds * 0.5) * 0.5 + 0.5) * height,
                            20.0
                        )
                        drawer.fill = ColorRGBa.PINK
                        drawer.circle(
                            (sin(seconds * 2.0) * 0.5 + 0.5) * width,
                            (cos(seconds) * 0.5 + 0.5) * height,
                            20.0
                        )

                        drawer.fill = ColorRGBa.BLACK
                        drawer.circle(
                            (sin(seconds * 1.0) * 0.5 + 0.5) * width,
                            (cos(seconds * 2.0) * 0.5 + 0.5) * height,
                            20.0
                        )
                    }
                    post(PoissonFill())
                }
                layer {
                    // -- an extra layer just to demonstrate where the original data points are drawn
                    draw {
                        drawer.stroke = ColorRGBa.WHITE
                        drawer.strokeWeight = 5.0
                        drawer.fill = ColorRGBa.RED
                        drawer.circle(
                            (cos(seconds) * 0.5 + 0.5) * width,
                            (sin(seconds * 0.5) * 0.5 + 0.5) * height,
                            20.0
                        )
                        drawer.fill = ColorRGBa.PINK
                        drawer.circle(
                            (sin(seconds * 2.0) * 0.5 + 0.5) * width,
                            (cos(seconds) * 0.5 + 0.5) * height,
                            20.0
                        )

                        drawer.fill = ColorRGBa.BLACK
                        drawer.circle(
                            (sin(seconds * 1.0) * 0.5 + 0.5) * width,
                            (cos(seconds * 2.0) * 0.5 + 0.5) * height,
                            20.0
                        )
                    }
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }

    @Media.Video "media/poisson-fills-002.mp4"

    @Application
    @ProduceVideo("media/poisson-fills-002.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 770
        }
        program {
            val c = compose {
                layer {
                    val image = loadImage("data/images/cheeta.jpg")
                    draw {
                        drawer.image(
                            image,
                            Rectangle(
                                (cos(seconds) * 0.5 + 0.5) * 100.0,
                                (sin(seconds) * 0.5 + 0.5) * 100.0,
                                200.0,
                                200.0
                            ),
                            Rectangle(
                                width / 2 - 100.0,
                                height / 2.0 - 100.0,
                                200.0,
                                200.0
                            )
                        )

                    }
                    post(PoissonFill())
                }

            }
            extend {
                c.draw(drawer)
            }
        }
    }

    @Text """
    # Blending
    """

    @Media.Video "media/poisson-fills-101.mp4"

    @Application
    @ProduceVideo("media/poisson-fills-101.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val c = compose {
                layer {
                    val image = loadImage("data/images/cheeta.jpg")
                    draw {
                        drawer.image(image)
                    }
                }
                layer {
                    draw {
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.BLACK
                        drawer.circle(
                            (cos(seconds) * 0.5 + 0.5) * width,
                            (sin(seconds * 0.5) * 0.5 + 0.5) * height,
                            120.0
                        )
                    }
                    blend(PoissonBlend())
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }

    @Media.Video "media/poisson-fills-102.mp4"

    @Application
    @ProduceVideo("media/poisson-fills-102.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")

            val c = compose {
                layer {
                    draw {
                        drawer.image(image)
                    }
                }
                layer {
                    draw {
                        drawer.stroke = ColorRGBa.GRAY
                        drawer.fill = null
                        drawer.strokeWeight = 40.0
                        drawer.circle(
                            (cos(seconds) * 0.5 + 0.5) * width,
                            (sin(seconds * 0.5) * 0.5 + 0.5) * height,
                            120.0
                        )

                    }
                    post(LumaOpacity()) {
                        this.backgroundLuma = 0.25
                    }
                    blend(PoissonBlend())
                }
            }
            extend {
                c.draw(drawer)
            }
        }
    }
}