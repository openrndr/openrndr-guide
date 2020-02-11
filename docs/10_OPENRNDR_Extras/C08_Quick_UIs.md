 
 # Quick UIs 
 
 [orx-gui](https://github.com/openrndr/orx/tree/master/orx-gui) provides a simple mechanism to create near zero-effort user interfaces. 
 
 ## Prerequisites 
 
 Assuming you are working on an [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
project, all you have to do is add "orx-gui" to the `orxFeatures` set in `build.gradle.kts`. Make sure to 
reimport the gradle project after adding the feature. 
 
 `orx-gui` relies on annotated classes and properties using the annotations in [`orx-parameters`](https://github.com/openrndr/orx/tree/master/orx-parameters) 
 
 `orx-gui` is incredibly powerful in combination with the live coding environment [`orx-olive`](https://github.com/openrndr/orx/tree/master/orx-gui), the guide covers that in the [live coding section](C03_Live_coding). 
That said, it is not a required combination. 
 
 ## Basic workflow 
 
 Using orx-gui starts with the OPENRNDR program skeleton and extension through `Gui`. It is somewhat uncommon, but this time we want to keep a reference to the extension. 
 
 ```kotlin
application {
    program {
        val gui = GUI()
        extend(gui)
        
        extend {}
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C08_Quick_UIs000.kt) 
 
 <img src="media/quick-ui-001.png"/> 
 
 This shows a side panel with 3 buttons: The randomize button can be pressed to randomize the parameters in the sidebar. The load and save button can be used to save the parameter settings to a .json file. Note that the sidebar can be hidden by pressing 
the F11 button. Compartments can be collapsed by clicking on the compartment header. 
 
 Now we want the sidebar put to good use. We can populate the sidebar by adding [`orx-parameters`](https://github.com/openrndr/orx/tree/master/orx-parameters) annotated objects. In the following example we create such an annotated `object` and add it. 
 
 ```kotlin
application {
    program {
        val gui = GUI()
        

        val settings = object {
            @DoubleParameter("x", 0.0, 770.0)
            var x: Double = 385.0
            
            @DoubleParameter("y", 0.0, 500.0)
            var y: Double = 250.0
        }
        
        // -- this is why we wanted to keep a reference to gui
        gui.add(settings, "Settings")
        
        // -- pitfall: the extend has to take place after gui is populated
        extend(gui)
        extend {
            
            // -- use our settings
            drawer.circle(settings.x, settings.y, 100.0)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C08_Quick_UIs001.kt) 
 
 <video controls>
    <source src="media/quick-ui-003.mp4" type="video/mp4"></source>
</video>
 
 
 We now see that the sidebar is populated with a _settings_ compartment that contains the _x_ and _y_ 
            parameters. Now whenever we move one of the sliders we will also move our circle that we draw using `settings.x` and `settings.y` 
 
 ## Filter workflow 
 
 Not only can user objects added to the sidebar, also most of the Filters in `orx-fx` have `orx-parameters` annotations. That means that
we can generate quick user interfaces for any of the filters that `orx-fx` provides. 

The guide covers filters in the [Filter and Post-processing chapter](06_Advanced_drawing/C01_Filters_and_post_processing.md) and an index of
provided filters can be found in [`orx-filter` index](10_OPENRNDR_Extras/C06_Filters.md) 
 
 ```kotlin
application {
    program {
        val gui = GUI()
        val settings = object {
            @DoubleParameter("x", 0.0, 770.0)
            var x: Double = 385.0
            
            @DoubleParameter("y", 0.0, 500.0)
            var y: Double = 250.0
        }
        val rt = renderTarget(width, height) {
            colorBuffer()
        }
        
        val filtered = colorBuffer(width, height)
        val blur = ApproximateGaussianBlur()
        
        gui.add(blur)
        gui.add(settings, "Settings")
        // -- pitfall: the extend has to take place after gui is populated
        extend(gui)
        extend {
            drawer.isolatedWithTarget(rt) {
                drawer.background(ColorRGBa.BLACK)
                // -- use our settings
                drawer.circle(settings.x, settings.y, 100.0)
            }
            blur.apply(rt.colorBuffer(0), filtered)
            drawer.image(filtered)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C08_Quick_UIs002.kt) 
 
 <video controls>
    <source src="media/quick-ui-004.mp4" type="video/mp4"></source>
</video>
 
 
 We now see that the sidebar is populated with a _settings_ and an _Approximate gaussian blur_ compartment, 
which contains parameters for the blur filter that we are using. 
 
 ## Compositor workflow 
 
 `orx-gui` and `orx-compositor` work nicely together. We still need one or more objects for our settings and 
we can insert any of the blend and post filters in the sidebar as we please. The guide covers `orx-compositor` in the [Compositor chapter](10_OPENRNDR_Extras/C07_Compositor) 
 
 ```kotlin
application {
    program {
        val gui = GUI()
        val settings = object {
            @DoubleParameter("x", 0.0, 770.0)
            var x: Double = 385.0
            
            @DoubleParameter("y", 0.0, 500.0)
            var y: Double = 385.0
            
            @DoubleParameter("separation", -150.0, 150.0)
            var separation: Double = 0.0
            
            @ColorParameter("background")
            var background = ColorRGBa.PINK
        }
        gui.add(settings, "Settings")
        
        // -- create a composite
        val composite = compose {
            layer {
                draw {
                    drawer.background(settings.background)
                }
            }
            layer {
                layer {
                    draw {
                        drawer.fill = ColorRGBa.RED
                        drawer.circle(settings.x - settings.separation, settings.y, 200.0)
                    }
                }
                layer {
                    draw {
                        drawer.fill = ColorRGBa.BLUE
                        drawer.circle(settings.x + settings.separation, settings.y, 200.0)
                    }
                    // -- add blend to layer and side
                    blend(gui.add(Multiply(), "Multiply blend"))
                }
                // -- add post to layer and sidebar
                post(gui.add(ApproximateGaussianBlur())) {
                    sigma = sin(seconds * PI) * 10.0 + 10.01
                    window = 25
                }
            }
        }
        extend(gui)
        extend {
            composite.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C08_Quick_UIs003.kt) 
 
 <video controls>
    <source src="media/quick-ui-005.mp4" type="video/mp4"></source>
</video>
 
 
 We now see that the sidebar is populated with a _settings_, _Multiply blend_, and an _Approximate gaussian blur_ compartment. 
This creates composites that are easy to tweak. 
 
 ## Live-coding workflow 
 
 `orx-gui` is built with the `orx-olive` environment in mind. Its use is similar to the workflows described prior, however, in live mode the ui comes with some extra features to make live-coding more fun.
Compartments can be added and removed from the .kts script. The best part is that `orx-gui` can retain parameter settings between script changes by default, so nothing jumps around.  
