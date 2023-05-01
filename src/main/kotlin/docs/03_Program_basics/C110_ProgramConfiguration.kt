@file:Suppress("UNUSED_EXPRESSION", "UNREACHABLE_CODE")
@file:Title("Program configuration")
@file:ParentTitle("Program basics")
@file:Order("110")
@file:URL("programBasics/programConfiguration")

package docs.`03_Program_basics`

import org.openrndr.Fullscreen
import org.openrndr.UnfocusBehaviour
import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.noise.Random
import org.openrndr.extra.noise.uniform
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2

fun main() {

    @Text
    """
    # Program Configuration
    
    Starting your program with a custom configuration looks roughly like this.
    """

    @Code
    application {
        configure {

            // settings go here
        }
        program {
            // -- one time set up code goes here
            extend {

                // -- drawing code goes here
            }
        }
    }

    @Text
    """
    ## Window size
    
    Setting the window size is done through the `width` and `height` properties.
    """

    @Code
    application {
        configure {
            width = 640
            height = 480
        }
    }

    @Text
    """
    To create a resizable window, set `windowResizable` to `true`.
    """

    @Text
    """
    ## Window position
    
    The `position` property represents the position of the top-left corner of the window. 
    Its default value is `null` for which the default behaviour 
    is to place the window at the center of the primary display.
 
    """

    @Code
    application {
        configure {
            position = IntVector2(100, 400)
        }
    }

    @Text
    """
    ## Display
    
    All currently detected displays can be found in the `displays` list.
    """

    @Code
    application {
        configure {
            display = displays[1]
        }
    }


    @Text
    """
    ## Fullscreen window

    By default, the window is not fullscreen. Fullscreen mode can be set using the `fullscreen` property.

    """

    @Code
    application {
        configure {
            width = 1920
            height = 1080
            fullscreen = Fullscreen.SET_DISPLAY_MODE
        }
    }

    @Text
    """
    If no mode change is desired use `Fullscreen.CURRENT_DISPLAY_MODE`.
    The effect of this setting is that the full width and height of the 
    current display mode are used, overwriting the `width` and `height` properties.
    """


    @Code
    application {
        configure {
            fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
        }
    }

    @Text
    """
    ## Window Title
    """

    @Code
    application {
        configure {
            title = "Lo and behold!"
        }
    }

    @Text 
    """
    ## Window unfocus behaviour
    
    Two window unfocus behaviours are available. In `NORMAL` behaviour the 
    program continues running at full speed. In contrast, the `THROTTLE` 
    behaviour throttles the program to 10Hz.
    """

    @Code
    application {
        configure {
            unfocusBehaviour = UnfocusBehaviour.THROTTLE
        }
    }

    @Text
    """
    To keep the window floating above other windows, even when it is unfocused,
    set `windowAlwaysOnTop` to `true`.
    """

    @Text
    """
    ## Mouse visibility
    
    It is possible to hide the mouse cursor via `hideCursor`.
    """

    @Code
    application {
        configure {
            hideCursor = true
        }
    }

    @Text
    """
    ## Changing the configuration while the program runs
    
    To modify the configuration after the program has started we can set 
    [various properties](https://github.com/openrndr/openrndr/blob/master/openrndr-application/src/commonMain/kotlin/org/openrndr/Application.kt) 
    via `application`.
    """

    @Code
    application {
        program {
            extend {
                if(frameCount % 60 == 0) {
                    application.cursorVisible = Random.bool()
                    application.windowPosition = Vector2.uniform(0.0, 200.0)
                }
            }
        }
    }


}
