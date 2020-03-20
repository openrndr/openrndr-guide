 
 # orx-fx Filters 
 
 The [orx-fx](https://github.com/openrndr/orx/tree/master/orx-fx) library contains many filters that can 
be readily used. See the chapter [Filters and post-processing](../06_Advanced_drawing/C01_Filters_and_post_processing) 
for instructions on using them.

A (more-or-less) complete listing of the effects in orx-fx is maintained in the repository's [README.md](https://github.com/openrndr/orx/blob/master/orx-fx/README.md)

 
 
 ## Effect Index 
 
 In this index we demonstrate selected filters, this is in no way a complete overview of what orx-fx offers. 
 
 ## Blur effects 
 
 #### BoxBlur 
 
 <video controls>
    <source src="media/filters-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val blurred = colorBuffer(image.width, image.height)
        val blur = BoxBlur()
        extend {
            blur.window = (cos(seconds * Math.PI) * 4.0 + 5.0).toInt()
            blur.apply(image, blurred)
            drawer.image(blurred)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters000.kt) 
 
 #### ApproximateGaussianBlur 
 
 <video controls>
    <source src="media/filters-002.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val blurred = colorBuffer(image.width, image.height)
        val blur = ApproximateGaussianBlur()
        extend {
            blur.window = 25
            blur.sigma = cos(seconds * Math.PI) * 10.0 + 10.1
            blur.apply(image, blurred)
            drawer.image(blurred)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters001.kt) 
 
 #### GaussianBloom 
 
 <video controls>
    <source src="media/filters-003.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val blurred = colorBuffer(image.width, image.height)
        val bloom = GaussianBloom()
        extend {
            bloom.window = 5
            bloom.sigma = 3.0
            bloom.gain = cos(seconds * 0.5 * PI) * 2.0 + 2.0
            bloom.apply(image, blurred)
            drawer.image(blurred)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters002.kt) 
 
 #### HashBlur 
 
 <video controls>
    <source src="media/filters-004.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val blurred = colorBuffer(image.width, image.height)
        val blur = HashBlur()
        extend {
            blur.samples = 50
            blur.radius = cos(seconds * 0.5 * Math.PI) * 25.0 + 25.0
            blur.apply(image, blurred)
            drawer.image(blurred)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters003.kt) 
 
 #### FrameBlur 
 
 <video controls>
    <source src="media/filters-005.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val blurred = colorBuffer(image.width, image.height)
        val blur = FrameBlur()
        val rt = renderTarget(width, height) {
            colorBuffer()
        }
        
        extend {
            drawer.isolatedWithTarget(rt) {
                drawer.background(ColorRGBa.BLACK)
                drawer.image(image, cos(seconds * PI) * 40.0, sin(seconds * PI) * 40.0)
            }
            
            blur.blend = 0.01
            blur.apply(rt.colorBuffer(0), blurred)
            drawer.image(blurred)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters004.kt) 
 
 #### ZoomBlur 
 
 <video controls>
    <source src="media/filters-006.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val blurred = colorBuffer(image.width, image.height)
        val blur = ZoomBlur()
        
        extend {
            blur.center = Vector2(cos(seconds * PI * 0.5) * 0.5 + 0.5, sin(seconds * PI) * 0.5 + 0.5)
            blur.strength = cos(seconds * PI) * 0.5 + 0.5
            blur.apply(image, blurred)
            drawer.image(blurred)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters005.kt) 
 
 ## Color filters 
 
 #### ChromaticAberration 
 
 <video controls>
    <source src="media/filters-100.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = ChromaticAberration()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.aberrationFactor = cos(seconds * 0.5 * PI) * 10.0
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters006.kt) 
 
 #### ColorCorrection 
 
 <video controls>
    <source src="media/filters-101.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = ColorCorrection()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.hueShift = cos(seconds * 0.5 * PI) * 180.0
            filter.saturation = cos(seconds * 1 * PI)
            filter.brightness = sin(seconds * 0.25 * PI) * 0.1
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters007.kt) 
 
 #### Sepia 
 
 <video controls>
    <source src="media/filters-102.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = Sepia()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.amount = cos(seconds) * 0.5 + 0.5
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters008.kt) 
 
 #### LumaOpacity 
 
 <video controls>
    <source src="media/filters-103.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = LumaOpacity()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            // -- a pink background to demonstrate the introduced transparencies
            drawer.background(ColorRGBa.PINK)
            filter.backgroundOpacity = 0.0
            filter.foregroundOpacity = 1.0
            filter.backgroundLuma = cos(seconds * PI) * 0.25 + 0.25
            filter.foregroundLuma = 1.0 - (cos(seconds * PI) * 0.25 + 0.25)
            
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters009.kt) 
 
 ## Edge-detection filters 
 
 #### LumaSobel 
 
 <video controls>
    <source src="media/filters-200.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = LumaSobel()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.backgroundColor = ColorRGBa.PINK.toHSVa().shiftHue(cos(seconds * 0.5 * PI) * 180).toRGBa().shade(0.25)
            filter.edgeColor = ColorRGBa.PINK
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters010.kt) 
 
 #### Contour 
 
 <video controls>
    <source src="media/filters-201.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = Contour()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.backgroundOpacity = 1.0
            filter.contourColor = ColorRGBa.BLACK
            filter.contourWidth = 0.4
            filter.levels = cos(seconds * PI) * 3.0 + 5.1
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters011.kt) 
 
 #### EdgesWork 
 
 <video controls>
    <source src="media/filters-202.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = EdgesWork()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.radius = (cos(seconds * PI) * 5 + 5).toInt()
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters012.kt) 
 
 ## Distortion filters 
 
 #### BlockRepeat 
 
 <video controls>
    <source src="media/filters-300.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = BlockRepeat()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.sourceScale = seconds / 5.0
            filter.blockWidth = cos(seconds * 0.5 * PI) * 0.3 + 0.4
            filter.blockHeight = sin(seconds * 0.5 * PI) * 0.3 + 0.4
            
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters013.kt) 
 
 #### StackRepeat 
 
 <video controls>
    <source src="media/filters-301.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = StackRepeat()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.repeats = 4
            filter.zoom = (cos(seconds * PI) * 0.1 + 0.11)
            
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters014.kt) 
 
 #### HorizontalWave 
 
 <video controls>
    <source src="media/filters-302.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = HorizontalWave()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.amplitude = cos(seconds * PI) * 0.1
            filter.frequency = sin(seconds * PI) * 4.0
            if (seconds > 2.5) {
                filter.segments = 10
            }
            
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters015.kt) 
 
 #### VerticalWave 
 
 <video controls>
    <source src="media/filters-303.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = VerticalWave()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.amplitude = cos(seconds * PI) * 0.1
            filter.frequency = sin(seconds * PI) * 4.0
            if (seconds > 2.5) {
                filter.segments = 10
            }
            
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters016.kt) 
 
 #### Perturb 
 
 <video controls>
    <source src="media/filters-304.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = Perturb()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.phase = seconds * 0.1
            filter.decay = 0.168
            filter.gain = cos(seconds * 0.25 * PI) * 0.5 + 0.5
            
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters017.kt) 
 
 #### Tiles 
 
 <video controls>
    <source src="media/filters-305.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = Tiles()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.rotation = seconds * 45.0
            filter.xSegments = (10 + cos(seconds * PI) * 5.0).toInt()
            filter.ySegments = 30
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters018.kt) 
 
 #### Fisheye 
 
 <video controls>
    <source src="media/filters-306.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = Fisheye()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            filter.strength = cos(seconds * PI) * 0.125
            filter.scale = 1.1
            filter.apply(image, filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters019.kt) 
 
 #### DisplaceBlend 
 
 <video controls>
    <source src="media/filters-307.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
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
                    val size = cos(seconds * PI * 0.5) * 100.0 + 200.0
                    drawer.rectangle(width / 2.0 - size / 2, height / 2.0 - size / 2, size, size)
                }
                blend(DisplaceBlend()) {
                    gain = cos(seconds * PI * 0.5) * 0.5 + 0.5
                    rotation = seconds * 45.0
                }
            }
        }
        
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters020.kt) 
 
 #### StretchWaves 
 
 <video controls>
    <source src="media/filters-308.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
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
                    rotation = seconds * 45.0
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
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters021.kt) 
 
 #### PerspectivePlane 
 
 <video controls>
    <source src="media/filters-309.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
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
                    planePitch = cos(seconds * 0.5 * PI) * 22.5
                    planeYaw = sin(seconds * 0.5 * PI) * 22.5
                }
            }
        }
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters022.kt) 
 
 ## Dithering filters 
 
 #### ADither 
 
 <video controls>
    <source src="media/filters-400.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
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
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters023.kt) 
 
 #### CMYKHalftone 
 
 <video controls>
    <source src="media/filters-401.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = CMYKHalftone()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            // -- need a white background because the filter introduces transparent areas
            drawer.background(ColorRGBa.WHITE)
            filter.dotSize = 1.2
            filter.scale = cos(seconds * 0.25 * PI) * 2.0 + 6.0
            filter.apply(image, filtered)
            
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters024.kt) 
 
 #### Crosshatch 
 
 <video controls>
    <source src="media/filters-402.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = Crosshatch()
        val filtered = colorBuffer(image.width, image.height)
        
        extend {
            // -- need a white background because the filter introduces transparent areas
            drawer.background(ColorRGBa.WHITE)
            filter.t1 = cos(seconds * PI) * 0.25 + 0.25
            filter.t2 = filter.t1 + cos(seconds * PI * 0.5) * 0.25 + 0.25
            filter.t3 = filter.t2 + cos(seconds * PI * 0.25) * 0.25 + 0.25
            filter.t4 = filter.t3 + cos(seconds * PI * 0.125) * 0.25 + 0.25
            
            filter.apply(image, filtered)
            
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters025.kt) 
 
 ## Shadow filters 
 
 #### DropShadow 
 
 <video controls>
    <source src="media/filters-500.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        val filter = DropShadow()
        val filtered = colorBuffer(image.width, image.height)
        
        val rt = renderTarget(width, height) {
            colorBuffer()
        }
        
        extend {
            drawer.isolatedWithTarget(rt) {
                drawer.background(ColorRGBa.TRANSPARENT)
                drawer.image(image, (image.width - image.width * 0.8) / 2, (image.height - image.height * 0.8) / 2, image.width * 0.8, image.height * 0.8)
            }
            // -- need a pink background because the filter introduces transparent areas
            drawer.background(ColorRGBa.PINK)
            filter.window = (cos(seconds * 0.5 * PI) * 16 + 16).toInt()
            filter.xShift = cos(seconds * PI) * 16.0
            filter.yShift = sin(seconds * PI) * 16.0
            filter.apply(rt.colorBuffer(0), filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters026.kt) 
 
 ## Pattern filters 
 
 #### Checkers 
 
 `Checkers` is a simple checker generator filter. 
 
 <video controls>
    <source src="media/filters-600.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val composite = compose {
            layer {
                post(Checkers()) {
                    size = cos(seconds * 0.5 * PI) * 0.6 + 0.4
                }
            }
        }
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C06_Filters027.kt) 
