 
 # Compositing using orx-compositor 
 
 orx-compositor offers a simple DSL for the creation of layered graphics. The compositor manages blending and
post-processing of layers for you. 
 
 ## Prerequisites 
 
 Assuming you are working on an [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
project, all you have to do is add "orx-compositor" to the `orxFeatures` set in `build.gradle.kts`. Make sure to 
reimport the gradle project after adding the feature.
        
`orx-compositor` works well together with `orx-fx`, `orx-gui`, and `orx-olive`, although they are not a required 
combination it is worth checking out what the combination has to offer. 
 
 ## Workflow 
 
 Let us now work through the workflow of `orx-compositor`. One usually starts with an OPENRNDR skeleton
program: 
 
 ```kotlin
application {
    program {
        extend {
        }
    }
}
``` 
 
 Which by itself, of course, does nothing. Let's extend this skeleton a bit and add the basics for layered
graphics. We add a `composite` using `compose {}` and we make sure that our OPENRNDR program draws it on refresh.

Note, if this fails you can fix it by adding `import org.openrndr.extra.compositor.draw`.
 
 
 ```kotlin
application {
    program {
        val composite = compose {
        }
        
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 Now let's draw something. We do this by adding a `draw {}` inside the `compose {}`. Here we see
we use `drawer` like we would use it normally (it is captured in the closure). We also added a `println` to demonstrate that
the code inside `compose {}` is executed once, however, code inside `draw {}` is executed every time the composite is
drawn. 
 
 ```kotlin
application {
    program {
        val composite = compose {
            println("this is only executed once")
            
            draw {
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(width / 2.0, height / 2.0, 175.0)
            }
        }
        
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 Let's get to what `orx-compositor` promises: layered graphics. We do this by adding a `layer {}` 
inside our composite, and inside this layer we add another `draw`. 
 
Every layer has an isolated draw state to prevent users from leaking draw state.  
 
 ```kotlin
application {
    program {
        
        val composite = compose {
            draw {
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(width / 2.0, height / 2.0, 175.00)
            }
            
            layer {
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = null
                    drawer.circle(width / 2.0, height / 2.0 + 100.0, 100.0)
                }
            }
        }
        
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C07_Compositor000.kt) 
 
 This produces: 
 
 <img src="media/compositor-001.png"/> 
 
 You may be thinking: "yeah great, we added all that extra structure to the code, but it doesn't do a 
single thing that could not be achieved by drawing two circles consecutively". And you're right. However, there are now
two things we can add with ease: _blends_ and _posts_. Here a _blend_ describes how a layer's contents should be combined with
the layer it covers, and a _post_ a filter that is applied after the contents have been drawn.

Let's add a blend and a post to our layer and see what it does:    
 
 ```kotlin
application {
    program {
        
        val composite = compose {
            draw {
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(width / 2.0, height / 2.0, 175.0)
            }
            
            layer {
                blend(Add())
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = null
                    drawer.circle(width / 2.0, height / 2.0 + 100.0, 100.0)
                }
                post(ApproximateGaussianBlur()) {
                    window = 25
                    sigma = 10.00
                }
            }
        }
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C07_Compositor001.kt) 
 
 The output: 
 
 <img src="media/compositor-002.png"/> 
 
 We now see a couple of differences. The smaller circle is blurred while the larger circle is not; The area where the two circles overlap 
is brighter; The smaller circle is clipped against the larger circle.

These are a results that are not as easily replicated without `orx-compositor`. 
 
 Note that the parameters for the _post_ filters (and _blend_) can be animated, just as the layers contents can be animated: 
 
 ```kotlin
application {
    program {
        
        val composite = compose {
            draw {
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(width / 2.0 + sin(seconds * PI * 0.5) * 100.0, height / 2.0, 175.0)
            }
            
            layer {
                blend(Add())
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = null
                    drawer.circle(width / 2.0, height / 2.0 + cos(seconds * PI * 0.5) * 100.0, 100.0)
                }
                post(ApproximateGaussianBlur()) {
                    // -- this is actually a function that is called for every draw
                    window = 25
                    sigma = cos(seconds * Math.PI * 0.25) * 10.0 + 10.01
                }
            }
        }
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C07_Compositor002.kt) 
 
 <video controls>
    <source src="media/compositor-003.mp4" type="video/mp4"></source>
</video>
 
