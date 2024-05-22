@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Live coding")
@file:ParentTitle("Use cases")
@file:Order("130")
@file:URL("useCases/liveCoding")

package docs.`95_Use_cases`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.persistent
import org.openrndr.extra.camera.Camera2D
import org.openrndr.extra.olive.Once
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.ffmpeg.VideoPlayerFFMPEG

fun main() {
    @Text """
    # Live coding with orx-olive
    
    By using Kotlin's ability to run script files we can build a live coding 
    environment. The `orx-olive` library 
    simplifies the work to be done to set up a live coding environment. Code 
    and additional documentation for the library can be found in the 
    [Github repository](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-olive).
    
    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, `orx-olive` is enabled by default.
    
    ## Basic example
    """

    @Code
    application {
        configure {
            width = 768
            height = 576
        }
        oliveProgram {
            extend {
                // drawer.fill = ColorRGBa.PINK
                drawer.rectangle(0.0, 0.0, 100.0, 200.0)
            }
        }
    }

    @Text 
    """
    Try editing the source code to change the fill color of the rectangle
    (or any other property) and save your changes. 
    The new color should appear instantly without having to re-run the 
    program. 
    
    ## Interaction with extensions
    
    The Olive extension works well together with other extensions. 
    In the following example we see the use of `Camera2D` in 
    combination with `Olive`.
    """

    @Code
    application {
        oliveProgram {
            extend(Camera2D())
            extend {
                drawer.rectangle(0.0, 0.0, 100.0, 200.0)
            }
        }
    }

    @Text
    """
    ## Adding persistent state
    
    Sometimes you want to keep parts of your application persistent, that 
    means its state will survive a script reload.
    
    This is how we can make the `Camera2D` from the previous example
    persistent:
    """

    @Code
    application {
        oliveProgram {
            val camera by Once {
                persistent {
                    Camera2D()
                }
            }
            extend(camera)
            extend {
                drawer.rectangle(0.0, 0.0, 100.0, 200.0)
            }
        }
    }


    @Text
    """
    The same approach can be used to maintain a persistent connection
    to a webcam:
    """

    @Code
    application {
        oliveProgram {
            val webcam by Once {
                persistent {
                    VideoPlayerFFMPEG.fromDevice()
                }
            }
            webcam.play()
            extend {
                webcam.colorBuffer?.let {
                    drawer.image(it,0.0, 0.0, 128.0, 96.0)
                }
            }
        }
    }

    @Text 
    """            
    ## GUI workflow

    `orx-gui` is built with the `orx-olive` environment in mind. 
    Its use is similar to the workflows described prior, however, in live 
    mode the ui comes with some extra features to make live-coding more fun.
    The best part is that `orx-gui` can retain parameter settings between script 
    changes by default, so nothing jumps around. 
    
    Notice the use of `Reloadable()` in the `settings` object.
    
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
    
    fun main() = application {
        configure {
            width = 800
            height = 800
        }
        oliveProgram {
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
            extend {
                composite.draw(drawer)
            }
        }
    }
    ```
    """
}
