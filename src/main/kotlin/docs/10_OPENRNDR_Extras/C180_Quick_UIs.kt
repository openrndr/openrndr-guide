@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Quick UIs")
@file:ParentTitle("OPENRNDR Extras")
@file:Order("180")
@file:URL("OPENRNDRExtras/quickUIs")

package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*
import org.openrndr.extra.compositor.blend
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.parameters.ColorParameter
import org.openrndr.extra.parameters.DoubleParameter

import org.openrndr.extra.compositor.draw
import org.openrndr.extra.gui.addTo
import kotlin.math.cos
import kotlin.math.sin


fun main() {
    @Text """
    # Quick UIs
    
    [orx-gui](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-gui) 
    provides a simple mechanism to create near zero-effort user 
    interfaces. `orx-gui` is a tool written on top of 
    [OPENRNDR's UI library](https://guide.openrndr.org/interaction/userInterfaces.html) with the intention
    of taking away most mental and work overhead involved in creating 
    simple user interfaces intended for prototyping and hacking purposes. 
    The core principle of `orx-gui` is to generate user interfaces only 
    from annotated classes and properties. 

    `orx-gui` relies on annotated classes and properties using the annotations 
    in [`orx-parameters`](https://github.com/openrndr/orx/tree/master/orx-parameters)
    
    `orx-gui` is incredibly powerful in combination with the live coding environment 
    [`orx-olive`](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-olive), 
    the guide covers that in the 
    [live coding section](https://guide.openrndr.org/OPENRNDRExtras/liveCoding.html). 
    That said, it is not a required combination.
    
    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-gui` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    ## Basic workflow
    
    Using orx-gui starts with the OPENRNDR program skeleton and extension 
    through `Gui`. It is somewhat uncommon, but this time we want to keep a 
    reference to the extension.
    """

    @Application
    @ProduceScreenshot("media/quick-ui-001.jpg")
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 500
        }

        program {
            val gui = GUI()
            extend(gui)
        }
    }

    @Media.Image "../media/quick-ui-001.jpg"

    @Text 
    """
    This shows a side panel with 3 buttons: The randomize button can be 
    pressed to randomize the parameters in the sidebar. The load and save 
    button can be used to save the parameter settings to a .json file. 
    Note that the sidebar can be hidden by pressing the F11 button. 
    Compartments can be collapsed by clicking on the compartment header.

    Now we want the sidebar put to good use. We can populate the sidebar 
    by adding 
    [`orx-parameters`](https://github.com/openrndr/orx/tree/master/orx-parameters) 
    annotated objects. In the following example we create such an annotated 
    `object` and add it.
    """

    @Application
    @ProduceVideo("media/quick-ui-003.mp4", 6.28318, 60, 8)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 500
        }

        program {
            val gui = GUI()
            @Exclude
            run {
                gui.compartmentsCollapsedByDefault = false
                gui.doubleBind = true
            }

            val settings = object {
                @DoubleParameter("x", 0.0, 770.0)
                var x: Double = 385.0

                @DoubleParameter("y", 0.0, 500.0)
                var y: Double = 250.0

                // Use `var` for your annotated variables.
                // `val` will produce no UI element!
                @DoubleParameter("z", -10.0, 10.0)
                val z: Double = 0.0
            }

            // -- this is why we wanted to keep a reference to gui
            gui.add(settings, "Settings")

            // -- pitfall: the extend has to take place after gui is populated
            extend(gui)


            extend {
                @Exclude
                run {
                    settings.x = cos(seconds) * 100.0 + 385.0
                    settings.y = sin(seconds) * 100.0 + 250.0
                }

                // -- use our settings
                drawer.circle(settings.x, settings.y, 100.0)

            }
        }
    }

    @Media.Video "../media/quick-ui-003.mp4"

    @Text 
    """
    We now see that the sidebar is populated with a _settings_ compartment 
    that contains the _x_ and _y_ parameters. Now whenever we move one of 
    the sliders we will also move our circle that we draw using `settings.x` 
    and `settings.y`
    
    ## Filter workflow
    
    Not only can user objects added to the sidebar, also most of the Filters 
    in `orx-fx` have `orx-parameters` annotations. That means that
    we can generate quick user interfaces for any of the filters that 
    `orx-fx` provides. 

    The guide covers filters in the 
    [Filter and Post-processing chapter](https://guide.openrndr.org/advancedDrawing/filtersAndPostProcessing.html) 
    and an index of provided filters can be found in 
    [`orx-filter` index](https://guide.openrndr.org/OPENRNDRExtras/imageFilters.html)
    """

    @Application
    @ProduceVideo("media/quick-ui-004.mp4", 6.28318, 60, 8)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 500
        }

        program {
            val gui = GUI()
            @Exclude
            run {
                gui.compartmentsCollapsedByDefault = false
                gui.doubleBind = true
            }
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
                @Exclude
                run {
                    settings.x = cos(seconds * 2) * 100.0 + 385.0
                    settings.y = sin(seconds * 2) * 100.0 + 250.0
                    blur.window = 25
                    blur.sigma = cos(seconds * 0.5) * 10.0 + 10.1
                }
                drawer.isolatedWithTarget(rt) {
                    drawer.clear(ColorRGBa.BLACK)
                    // -- use our settings
                    drawer.circle(settings.x, settings.y, 100.0)
                }
                blur.apply(rt.colorBuffer(0), filtered)
                drawer.image(filtered)
            }
        }
    }

    @Media.Video "../media/quick-ui-004.mp4"

    @Text 
    """
    We now see that the sidebar is populated with a _settings_ and 
    an _Approximate Gaussian blur_ compartment, 
    which contains parameters for the blur filter that we are using.
    
    ## Compositor workflow
    
    `orx-gui` and `orx-compositor` work nicely together. We still need one 
    or more objects for our settings and 
    we can insert any of the blend and post filters in the sidebar as we please. 
    The guide covers `orx-compositor` in the 
    [Compositor chapter](https://guide.openrndr.org/OPENRNDRExtras/compositor.html)
    """

    @Application
    @ProduceVideo("media/quick-ui-005.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 770
        }
        program {
            val gui = GUI()
            @Exclude
            run {
                gui.compartmentsCollapsedByDefault = false
                gui.doubleBind = true
            }
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
                        drawer.clear(settings.background)
                    }
                }
                layer {
                    layer {
                        draw {
                            drawer.fill = ColorRGBa.RED
                            drawer.circle(
                                settings.x - settings.separation,
                                settings.y,
                                200.0
                            )
                        }
                    }
                    layer {
                        draw {
                            drawer.fill = ColorRGBa.BLUE
                            drawer.circle(
                                settings.x + settings.separation,
                                settings.y,
                                200.0
                            )
                        }
                        // -- add blend to layer and sidebar
                        blend(gui.add(Multiply(), "Multiply blend"))
                        // -- add layer to sidebar to toggle it on / off
                    }.addTo(gui, "Blue layer")
                    // -- add post to layer and sidebar
                    post(gui.add(ApproximateGaussianBlur())) {
                        sigma = sin(seconds) * 10.0 + 10.01
                        window = 25
                    }
                }
            }
            extend(gui)
            extend {
                @Exclude
                run {
                    settings.separation = cos(seconds) * 100.0
                }
                composite.draw(drawer)
            }
        }
    }

    @Media.Video "../media/quick-ui-005.mp4"

    @Text 
    """
    We now see that the sidebar is populated with a _settings_, 
    _Multiply blend_, _Blue layer_, and an _Approximate gaussian blur_ compartment. 
    This creates composites that are easy to tweak.
    
    ## Live-coding workflow
    
    `orx-gui` is built with the `orx-olive` environment in mind. 
    Its use is similar to the workflows described prior, however, in live 
    mode the ui comes with some extra features to make live-coding more fun.
    Compartments can be added and removed from the .kts script. The best 
    part is that `orx-gui` can retain parameter settings between script 
    changes by default, so nothing jumps around. 

    In the case of using `orx-gui` from an olive script (`live.kts`) it looks like this
    
    ```kotlin
    @file:Suppress("UNUSED_LAMBDA_EXPRESSION")
    import org.openrndr.Program
    import org.openrndr.color.ColorRGBa
    import org.openrndr.draw.*
    import org.openrndr.extra.compositor.compose
    import org.openrndr.extra.compositor.draw
    import org.openrndr.extra.compositor.layer
    import org.openrndr.extra.compositor.post
    import org.openrndr.extra.gui.GUI
    import org.openrndr.extra.parameters.*
    
    { program: Program ->
        program.apply {
            val gui = GUI()
            val settings = @Description("User settings") object : Reloadable() {
                @DoubleParameter("x", 0.0, 1000.0)
                var x = 0.0
            }
            val composite = compose {
                draw {
                    drawer.clear(ColorRGBa.PINK)
                    drawer.circle(settings.x, height / 2.0, 100.0)
                }
            }
            extend(gui) {
                add(settings)
            }
            extend() {
                composite.draw(drawer)
            }
        }
    }    
    ```    

    ## Parameter annotations
    
    We have seen some of the annotations in the workflow descriptions 
    and we have linked to the 
    [`orx-parameters` code](https://github.com/openrndr/orx/tree/master/orx-parameters) 
    before. But we haven't really spent time explaining the annotations until now.
    Annotations are a Kotlin language feature that is used to attach metadata 
    to code, if you are unfamiliar with them you are encouraged to read the 
    [annotations chapter](https://kotlinlang.org/docs/reference/annotations.html) 
    in the Kotlin Language Guide. However, to effectively use the `orx-parameters` 
    annotations basically all you have to know is where and when to apply the 
    annotations. The annotations are used in front of mutable class/object 
    properties.
  
    Currently `orx-parameters` has a small set of parameter annotations:
   
    ##### DoubleParameter
    
    `DoubleParameter` is used in combination with `Double` types. It takes a label, minimum-value, maximum value, and optional precision and order arguments. `orx-gui` will generate a slider control for annotated properties.
    ```kotlin
    val settings = object {
        @DoubleParameter("x", 0.0, 100.0, precision = 3, order = 0)
        var x = 0.0                   
    }
    ```              
  
    ##### IntParameter
    
    `IntParameter` is used in combination with `Int` types. It takes a label, minimum-value, maximum value, and an optional order argument. `orx-gui` will generate a slider control for annotated properties.
    ```kotlin
    val settings = object {
        @IntParameter("x", 0, 100, order = 0)
        var x = 0                   
    }
    ```              
    
    ##### ColorParameter
    
    `ColorParameter` is used in combination with `Color` types. It takes an optional order argument. `orx-gui` will generate a color picker control for annotated properties.
    ```kotlin
    val settings = object {
        @ColorParameter("color", order = 0)
        var color = ColorRGBa.PINK                   
    }
    ```

    ##### TextParameter
    
    `TextParameter` is used in combination with `String` types. It takes an optional order argument. `orx-gui` will generate a textfield control for annotated properties.
    ```kotlin
    val settings = object {
        @TextParameter("text", order = 0)
        var text = "default text value"                   
    }
    ```
    
    ##### BooleanParameter
    
    `BooleanParameter` is used in combination with `Boolean` types. It takes an optional order argument. `orx-gui` will generate a checkbox or toggle control for annotated properties.
    
    ```kotlin
    val settings = object {
        @BooleanParameter("option", order = 0)
        var b = false                   
    }
    ```
    
    ##### XYParameter
    
    `XYParameter` is used in combination with `Vector2` types. It takes an optional order argument. `orx-gui` will generate a two dimensional pad control for annotated properties.
    
    ```kotlin
    val settings = object {
        @XYParameter("xy", order = 0)
        var xy = Vector2.ZERO                   
    }
    ```
    
    ##### Vector2Parameter

    `Vector2Parameter` is used in combination with `Vector2` types. It takes an optional order argument. `orx-gui` will generate a vertical slider for annotated properties.
    
    ```kotlin
    val settings = object {
        @Vector2Parameter("vector2", order = 0)
        var v2 = Vector2.ZERO                   
    }
    ```

    ##### Vector3Parameter
    
    `Vector3Parameter` is used in combination with `Vector3` types. It takes an optional order argument. `orx-gui` will generate a vertical slider for annotated properties.
    
    ```kotlin
    val settings = object {
        @Vector3Parameter("vector3", order = 0)
        var v3 = Vector3.ZERO                   
    }
    ```
    
    ##### Vector4Parameter
    
    `Vector4Parameter` is used in combination with `Vector4` types. It takes an optional order argument. `orx-gui` will generate a vertical slider for annotated properties.
    
    ```kotlin
    val settings = object {
        @Vector4Parameter("vector4", order = 0)
        var v4 = Vector4.ZERO                   
    }
    ```

    ##### DoubleListParameter
    
    `DoubleListParameter` is used in combination with a list of `Double`. It takes an optional order argument. `orx-gui` will generate a set of vertical sliders.
    
    ```kotlin
    @DoubleListParameter("Mixer", order = 0)
    var mixer = MutableList(5) { 0.5 }
    ```
    
    ##### OptionParameter
    
    `OptionParameter` is used in combination with an `enum`. It takes an optional order argument. `orx-gui` will generate a dropdown including all options in the enum.
    
    ```kotlin
    enum class Parity { Odd, Even }
    
    @OptionParameter("Parity", order = 0)
    var parity = Parity.Odd
    ```

    ##### ActionParameter
    
    `ActionParameter` is a bit of an odd-one-out, it is not used to annotate properties but to annotate 0-argument functions. `orx-gui` will generate a button control that will call the function when clicked.
    
    ```kotlin
    val settings = object {
        @ActionParameter("save", order = 0)
        fun doSave() {
            println("file saved!")    
        }
    }
    ```
    """
}


