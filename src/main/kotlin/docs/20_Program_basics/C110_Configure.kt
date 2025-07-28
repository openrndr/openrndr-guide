@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Configure")
@file:ParentTitle("Program basics")
@file:Order("110")
@file:URL("programBasics/configure")

package docs.`20_Program_basics`

import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.noise.Random
import org.openrndr.extra.noise.uniform
import org.openrndr.math.Vector2

fun main() {
    @Text
    """
    ## Configure

    The `configure` block is an optional block that is used to configure
    the run-time environment. Most commonly it is used to configure the
    size of the window.

    An example configuration that sets the window size, window resizability
    and title is as follows:
    """

    @Code
    application {
        configure {
            width = 1280
            height = 720
            windowResizable = true
            title = "OPENRNDR Example"
        }
        program {
        }
    }
    
    @Text
    """
    An example for a full screen window on your second monitor
    with the mouse pointer hidden:
    """

    @Code
    application {
        configure {
            fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
            display = displays[1]
            hideCursor = true
        }
        program {
        }
    }

    @Text
    """   
    Starting your program with a custom configuration looks roughly like this.
    """

    @Code
    application {
        configure {

            // settings go here
        }
        program {
            // -- one time set-up code goes here
            extend {

                // -- drawing code goes here
            }
        }
    }

    @Text
    """
    The table below lists a selection of configuration options. See 
    [the API](https://api.openrndr.org/openrndr-application/org.openrndr/-configuration/index.html) 
    for the complete list.
    
    Property                | Type               | Default value                          | Description
    ------------------------|--------------------|----------------------------------------|---------------------------------
    `width`                 | `Int`              | `640`                                  | initial window width
    `height`                | `Int`              | `480`                                  | initial window height
    `windowResizable`       | `Boolean`          | `false`                                | allow resizing of window?
    `fullscreen`            | `Fullscreen`       | `Fullscreen.DISABLED`                  | When specified, either `Fullscreen.CURRENT_DISPLAY_MODE` to make the window match the current display resolution, or `Fullscreen.SET_DISPLAY_MODE` to change the display resolution to match `width` and `height`.
    `position`              | `IntVector2?`      | `null` (center of the primary display) | initial window position (top-left corner)
    `display`               | `Display?`         | `null` (primary display)               | The display on which to create the window. All detected displays are present in the `displays` list within the `application {}` block.
    `windowAlwaysOnTop`     | `Boolean`          | `false`                                | keep the window floating above other windows?
    `unfocusBehaviour`      | `UnfocusBehaviour` | `UnfocusBehaviour.NORMAL`              | The value `UnfocusBehaviour.THROTTLE` can be specified to throttle the program to 10Hz when unfocused.
    `hideCursor`            | `Boolean`          | `false`                                | hide the cursor?
    `title`                 | `String`           | `"OPENRNDR"`                           | window title
    `hideWindowDecorations` | `Boolean`          | `false`                                | hide window decorations?
    """

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
