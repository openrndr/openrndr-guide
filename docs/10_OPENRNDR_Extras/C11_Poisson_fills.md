 
 # Poisson fills 
 
 orx-poisson-fills offers tool for GPU-based Poisson operations.  
 
 ## Prerequisites 
 
 Make sure that `orx-poisson-fill` is added to the `orxFeatures` line. 
 
 # Filling 
 
 The `PoissonFill` filter can be used to fill in transparent parts of an image. In the following example
we use `orx-compose` to simplify the code a bit. The same results can be achieved using render targets. 
 
 <video controls>
    <source src="media/poisson-fills-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val c = compose {
            layer {
                draw {
                    drawer.stroke = null
                    drawer.fill = ColorRGBa.RED
                    drawer.circle((cos(seconds) * 0.5 + 0.5) * width, (sin(seconds * 0.5) * 0.5 + 0.5) * height, 20.0)
                    drawer.fill = ColorRGBa.PINK
                    drawer.circle((sin(seconds * 2.0) * 0.5 + 0.5) * width, (cos(seconds) * 0.5 + 0.5) * height, 20.0)
                    
                    drawer.fill = ColorRGBa.BLACK
                    drawer.circle((sin(seconds * 1.0) * 0.5 + 0.5) * width, (cos(seconds * 2.0) * 0.5 + 0.5) * height, 20.0)
                }
                post(PoissonFill())
            }
            layer {
                // -- an extra layer just to demonstrate where the original data points are drawn
                draw {
                    drawer.stroke = ColorRGBa.WHITE
                    drawer.strokeWeight = 5.0
                    drawer.fill = ColorRGBa.RED
                    drawer.circle((cos(seconds) * 0.5 + 0.5) * width, (sin(seconds * 0.5) * 0.5 + 0.5) * height, 20.0)
                    drawer.fill = ColorRGBa.PINK
                    drawer.circle((sin(seconds * 2.0) * 0.5 + 0.5) * width, (cos(seconds) * 0.5 + 0.5) * height, 20.0)
                    
                    drawer.fill = ColorRGBa.BLACK
                    drawer.circle((sin(seconds * 1.0) * 0.5 + 0.5) * width, (cos(seconds * 2.0) * 0.5 + 0.5) * height, 20.0)
                }
            }
        }
        extend {
            c.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C11_Poisson_fills000.kt) 
 
 <video controls>
    <source src="media/poisson-fills-002.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val c = compose {
            layer {
                val image = loadImage("data/images/cheeta.jpg")
                draw {
                    drawer.image(image, Rectangle((cos(seconds) * 0.5 + 0.5) * 100.0, (sin(seconds) * 0.5 + 0.5) * 100.0, 200.0, 200.0), Rectangle(width / 2 - 100.0, height / 2.0 - 100.0, 200.0, 200.0))
                }
                post(PoissonFill())
            }
        }
        extend {
            c.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C11_Poisson_fills001.kt) 
 
 # Blending 
 
 <video controls>
    <source src="media/poisson-fills-101.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
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
                    drawer.circle((cos(seconds) * 0.5 + 0.5) * width, (sin(seconds * 0.5) * 0.5 + 0.5) * height, 120.0)
                }
                blend(PoissonBlend())
            }
        }
        extend {
            c.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C11_Poisson_fills002.kt) 
 
 <video controls>
    <source src="media/poisson-fills-102.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
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
                    drawer.circle((cos(seconds) * 0.5 + 0.5) * width, (sin(seconds * 0.5) * 0.5 + 0.5) * height, 120.0)
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
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C11_Poisson_fills003.kt) 
