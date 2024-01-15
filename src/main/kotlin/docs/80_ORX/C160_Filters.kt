@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Image post-processing with filters")
@file:ParentTitle("ORX")
@file:Order("160")
@file:URL("ORX/imageFilters")

package docs.`10_ORX`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blur.*
import org.openrndr.extra.fx.color.*
import org.openrndr.extra.fx.distort.*
import org.openrndr.extra.fx.dither.ADither
import org.openrndr.extra.fx.dither.CMYKHalftone
import org.openrndr.extra.fx.dither.Crosshatch
import org.openrndr.extra.fx.edges.Contour
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.extra.fx.edges.LumaSobel
import org.openrndr.extra.fx.patterns.Checkers
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.shadestyles.linearGradient
import org.openrndr.extra.imageFit.imageFit

import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun main() {
    @Text 
    """
    # orx-fx Filters
    
    The [orx-fx](https://github.com/openrndr/orx/tree/master/orx-fx) library 
    contains many filters that can be readily used. See the chapter 
    [Filters and post-processing](https://guide.openrndr.org/drawing/filtersAndPostProcessing.html) 
    for instructions on using them.

    A (more-or-less) complete listing of the effects in orx-fx is maintained 
    in the repository's 
    [README.md](https://github.com/openrndr/orx/blob/master/orx-fx/README.md)

    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-fx` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    ## Effect Index
    
    In this index we demonstrate selected filters, this is in no way a 
    complete overview of what orx-fx offers.
    
    ## Blur effects
    
    #### BoxBlur
    """

    @Media.Video "../media/filters-001.mp4"

    @Application
    @ProduceVideo("media/filters-001.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            // -- load a source image
            val image = loadImage("data/images/cheeta.jpg")

            // -- create a filter
            val blur = BoxBlur()

            // -- create a colorBuffer where to store the result
            val blurred = colorBuffer(image.width, image.height)

            extend {
                // -- configure the filter
                blur.window = (cos(seconds * 2) * 4.0 + 5.0).toInt()

                // -- filter.apply(source, target)
                blur.apply(image, blurred)

                // -- draw the result
                drawer.image(blurred)
            }
        }
    }

    @Text """
    #### ApproximateGaussianBlur
    """

    @Media.Video "../media/filters-002.mp4"

    @Application
    @ProduceVideo("media/filters-002.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val blurred = colorBuffer(image.width, image.height)
            val blur = ApproximateGaussianBlur()
            extend {
                blur.window = 25
                blur.sigma = cos(seconds * 2) * 10.0 + 10.1
                blur.apply(image, blurred)
                drawer.image(blurred)
            }
        }
    }

    @Text """
    #### GaussianBloom
    """

    @Media.Video "../media/filters-003.mp4"

    @Application
    @ProduceVideo("media/filters-003.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val blurred = colorBuffer(image.width, image.height)
            val bloom = GaussianBloom()
            extend {
                bloom.window = 5
                bloom.sigma = 3.0
                bloom.gain = cos(seconds * 2) * 2.0 + 2.0
                bloom.apply(image, blurred)
                drawer.image(blurred)
            }
        }
    }

    @Text """
    #### HashBlur
    """

    @Media.Video "../media/filters-004.mp4"

    @Application
    @ProduceVideo("media/filters-004.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val blurred = colorBuffer(image.width, image.height)
            val blur = HashBlur()
            extend {
                blur.samples = 50
                blur.radius = cos(seconds * 2) * 25.0 + 25.0
                blur.apply(image, blurred)
                drawer.image(blurred)
            }
        }
    }

    @Text """
    #### FrameBlur
    """

    @Media.Video "../media/filters-005.mp4"

    @Application
    @ProduceVideo("media/filters-005.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val blurred = colorBuffer(image.width, image.height)
            val blur = FrameBlur()
            val rt = renderTarget(width, height) {
                colorBuffer()
            }

            extend {
                drawer.isolatedWithTarget(rt) {
                    drawer.clear(ColorRGBa.BLACK)
                    drawer.image(image, cos(seconds * 2) * 40.0, sin(seconds * 2) * 40.0)
                }

                blur.blend = 0.01
                blur.apply(rt.colorBuffer(0), blurred)
                drawer.image(blurred)
            }
        }
    }

    @Text """
    #### ZoomBlur
    """

    @Media.Video "../media/filters-006.mp4"

    @Application
    @ProduceVideo("media/filters-006.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val blurred = colorBuffer(image.width, image.height)
            val blur = ZoomBlur()

            extend {
                blur.center = Vector2(
                    cos(seconds) * 0.5 + 0.5,
                    sin(seconds * 2) * 0.5 + 0.5
                )
                blur.strength = cos(seconds * 2) * 0.5 + 0.5
                blur.apply(image, blurred)
                drawer.image(blurred)
            }
        }
    }

    @Text 
    """
    ## Color filters

    #### ChromaticAberration
    """

    @Media.Video "../media/filters-100.mp4"

    @Application
    @ProduceVideo("media/filters-100.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = ChromaticAberration()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.aberrationFactor = cos(seconds * 2) * 10.0
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### ColorCorrection
    """

    @Media.Video "../media/filters-101.mp4"

    @Application
    @ProduceVideo("media/filters-101.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = ColorCorrection()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.hueShift = cos(seconds * 1) * 180.0
                filter.saturation = cos(seconds * 2)
                filter.brightness = sin(seconds * 3) * 0.1
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### Sepia
    """

    @Media.Video "../media/filters-102.mp4"

    @Application
    @ProduceVideo("media/filters-102.mp4", 3.14159)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = Sepia()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.amount = cos(seconds * 2) * 0.5 + 0.5
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### LumaOpacity
    """

    @Media.Video "../media/filters-103.mp4"

    @Application
    @ProduceVideo("media/filters-103.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = LumaOpacity()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                // -- a pink background to demonstrate the introduced transparencies
                drawer.clear(ColorRGBa.PINK)
                filter.backgroundOpacity = 0.0
                filter.foregroundOpacity = 1.0
                filter.backgroundLuma = cos(seconds) * 0.25 + 0.25
                filter.foregroundLuma = 1.0 - (cos(seconds) * 0.25 + 0.25)

                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    ## Edge-detection filters

    #### LumaSobel
    """
    @Media.Video "../media/filters-200.mp4"

    @Application
    @ProduceVideo("media/filters-200.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = LumaSobel()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.backgroundColor = ColorRGBa.PINK.toHSVa()
                    .shiftHue(cos(seconds) * 180).toRGBa()
                    .shade(0.25)
                filter.edgeColor = ColorRGBa.PINK
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### Contour
    """

    @Media.Video "../media/filters-201.mp4"

    @Application
    @ProduceVideo("media/filters-201.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = Contour()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.backgroundOpacity = 1.0
                filter.contourColor = ColorRGBa.BLACK
                filter.contourWidth = 0.4
                filter.levels = cos(seconds) * 3.0 + 5.1
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }


    @Text """
    #### EdgesWork
    """

    @Media.Video "../media/filters-202.mp4"

    @Application
    @ProduceVideo("media/filters-202.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = EdgesWork()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.radius = (cos(seconds) * 5 + 5).toInt()
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    ## Distortion filters

    #### BlockRepeat
    """

    @Media.Video "../media/filters-300.mp4"

    @Application
    @ProduceVideo("media/filters-300.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = BlockRepeat()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.sourceScale = seconds / 5.0
                filter.blockWidth = cos(seconds) * 0.3 + 0.4
                filter.blockHeight = sin(seconds) * 0.3 + 0.4

                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### StackRepeat
    """

    @Media.Video "../media/filters-301.mp4"

    @Application
    @ProduceVideo("media/filters-301.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = StackRepeat()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.repeats = 4
                filter.zoom = (cos(seconds) * 0.1 + 0.11)

                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### HorizontalWave
    """

    @Media.Video "../media/filters-302.mp4"

    @Application
    @ProduceVideo("media/filters-302.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = HorizontalWave()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.amplitude = cos(seconds) * 0.1
                filter.frequency = sin(seconds) * 4.0
                if (seconds > 2.5) {
                    filter.segments = 10
                }

                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### VerticalWave
    """

    @Media.Video "../media/filters-303.mp4"

    @Application
    @ProduceVideo("media/filters-303.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = VerticalWave()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.amplitude = cos(seconds) * 0.1
                filter.frequency = sin(seconds) * 4.0
                if (seconds > 2.5) {
                    filter.segments = 10
                }

                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### Perturb
    """

    @Media.Video "../media/filters-304.mp4"

    @Application
    @ProduceVideo("media/filters-304.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = Perturb()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.phase = seconds * 0.1
                filter.decay = 0.168
                filter.gain = cos(seconds) * 0.5 + 0.5

                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### Tiles
    """

    @Media.Video "../media/filters-305.mp4"

    @Application
    @ProduceVideo("media/filters-305.mp4", 6.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = Tiles()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.rotation = seconds * 60.0
                filter.xSegments = (10 + cos(seconds * PI / 3) * 5.0).toInt()
                filter.ySegments = 30
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### Fisheye
    """

    @Media.Video "../media/filters-306.mp4"

    @Application
    @ProduceVideo("media/filters-306.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = Fisheye()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.strength = cos(seconds) * 0.125
                filter.scale = 1.1
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### DisplaceBlend
    """

    @Media.Video "../media/filters-307.mp4"

    @Application
    @ProduceVideo("media/filters-307.mp4", 6.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val composite = compose {
                colorType = ColorType.FLOAT16
                layer {
                    val image = loadImage("data/images/cheeta.jpg")
                    draw {
                        drawer.imageFit(image, 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                }
                layer {
                    draw {
                        drawer.shadeStyle = linearGradient(ColorRGBa.BLACK, ColorRGBa.WHITE)
                        drawer.stroke = null
                        val size = cos(seconds * PI / 3) * 100.0 + 200.0
                        drawer.rectangle(width / 2.0 - size / 2, height / 2.0 - size / 2, size, size)
                    }
                    blend(DisplaceBlend()) {
                        gain = cos(seconds * PI / 3) * 0.5 + 0.5
                        rotation = seconds * 60.0
                    }
                }
            }

            extend {
                composite.draw(drawer)
            }
        }
    }


    @Text """
    #### StretchWaves
    """

    @Media.Video "../media/filters-308.mp4"

    @Application
    @ProduceVideo("media/filters-308.mp4", 6.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val composite = compose {
                colorType = ColorType.FLOAT16
                layer {
                    val image = loadImage("data/images/cheeta.jpg")
                    draw {
                        drawer.imageFit(image, 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                    post(StretchWaves()) {
                        distortion = 0.25
                        rotation = seconds * 60.0
                        phase = seconds * 0.25
                        frequency = 5.0
                    }
                }
            }
            extend {
                composite.draw(drawer)
            }
        }
    }

    @Text """
    #### PerspectivePlane
    """

    @Media.Video "../media/filters-309.mp4"

    @Application
    @ProduceVideo("media/filters-309.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val composite = compose {
                layer {
                    post(Checkers())
                }

                layer {
                    val image = loadImage("data/images/cheeta.jpg")
                    draw {
                        drawer.imageFit(image, 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                    post(PerspectivePlane()) {
                        planePitch = cos(seconds) * 22.5
                        planeYaw = sin(seconds) * 22.5
                    }
                }
            }
            extend {
                composite.draw(drawer)
            }
        }
    }

    @Text """
    ## Dithering filters

    #### ADither
    """
    @Media.Video "../media/filters-400.mp4"

    @Application
    @ProduceVideo("media/filters-400.mp4", 5.0)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = ADither()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                filter.pattern = ((seconds / 5.0) * 4).toInt().coerceAtMost(3)
                filter.levels = ((seconds % 1.0) * 3).toInt() + 1
                filter.apply(image, filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### CMYKHalftone
    """

    @Media.Video "../media/filters-401.mp4"

    @Application
    @ProduceVideo("media/filters-401.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = CMYKHalftone()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                // -- need a white background because the filter introduces transparent areas
                drawer.clear(ColorRGBa.WHITE)
                filter.dotSize = 1.2
                filter.scale = cos(seconds) * 2.0 + 6.0
                filter.apply(image, filtered)

                drawer.image(filtered)
            }
        }
    }

    @Text """
    #### Crosshatch
    """

    @Media.Video "../media/filters-402.mp4"

    @Application
    @ProduceVideo("media/filters-402.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = Crosshatch()
            val filtered = colorBuffer(image.width, image.height)

            extend {
                // -- need a white background because the filter introduces transparent areas
                drawer.clear(ColorRGBa.WHITE)
                filter.t1 = cos(seconds * PI) * 0.25 + 0.25
                filter.t2 = filter.t1 + cos(seconds * 3) * 0.25 + 0.25
                filter.t3 = filter.t2 + cos(seconds * 2) * 0.25 + 0.25
                filter.t4 = filter.t3 + cos(seconds * 1) * 0.25 + 0.25

                filter.apply(image, filtered)

                drawer.image(filtered)
            }
        }
    }

    @Text """
    ## Shadow filters

    #### DropShadow
    """
    @Media.Video "../media/filters-500.mp4"

    @Application
    @ProduceVideo("media/filters-500.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            val filter = DropShadow()
            val filtered = colorBuffer(image.width, image.height)

            val rt = renderTarget(width, height) {
                colorBuffer()
            }

            extend {
                drawer.isolatedWithTarget(rt) {
                    drawer.clear(ColorRGBa.TRANSPARENT)
                    drawer.image(image, (image.width - image.width * 0.8) / 2, (image.height - image.height * 0.8) / 2, image.width * 0.8, image.height * 0.8)
                }
                // -- need a pink background because the filter introduces transparent areas
                drawer.clear(ColorRGBa.PINK)
                filter.window = (cos(seconds) * 16 + 16).toInt()
                filter.xShift = cos(seconds * 2) * 16.0
                filter.yShift = sin(seconds * 2) * 16.0
                filter.apply(rt.colorBuffer(0), filtered)
                drawer.image(filtered)
            }
        }
    }

    @Text """
    ## Pattern filters

    #### Checkers
    
    `Checkers` is a simple checker generator filter.
    """

    @Media.Video "../media/filters-600.mp4"

    @Application
    @ProduceVideo("media/filters-600.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 640
            height = 480
        }
        program {
            val composite = compose {
                layer {
                    post(Checkers()) {
                        size = cos(seconds) * 0.6 + 0.4
                    }
                }
            }
            extend {
                composite.draw(drawer)
            }
        }
    }
}
