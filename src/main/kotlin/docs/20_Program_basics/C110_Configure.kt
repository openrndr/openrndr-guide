@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Configure")
@file:ParentTitle("Program basics")
@file:Order("110")
@file:URL("programBasics/configure")

package docs.`20_Program_basics`

import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.noise.primitives.random
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
    Commonly used configuration options:
    
    Property                | Type               | Default value                          | Description
    ------------------------|--------------------|----------------------------------------|---------------------------------
    `display`               | `Display?`         | `null` (primary display)               | The display on which to create the window. All detected displays are present in the `displays` list within the `application {}` block.
    `fullscreen`            | `Fullscreen`       | `Fullscreen.DISABLED`                  | When specified, either `Fullscreen.CURRENT_DISPLAY_MODE` to make the window match the current display resolution, or `Fullscreen.SET_DISPLAY_MODE` to change the display resolution to match `width` and `height`.
    `height`                | `Int`              | `480`                                  | Initial window height
    `position`              | `IntVector2?`      | `null` (center of the primary display) | Initial window position (top-left corner)
    `title`                 | `String`           | `"OPENRNDR"`                           | Window title
    `width`                 | `Int`              | `640`                                  | Initial window width
    `multisample`           | `WindowMultisample`| `WindowMultisample.Disabled`           | Can be increased to a value like `WindowMultisample.SampleCount(8)` for a smoother rendering

    Other available options:
    
    Property                | Type               | Default value                          | Description
    ------------------------|--------------------|----------------------------------------|---------------------------------
    `hideCursor`            | `Boolean`          | `false`                                | Hide the cursor?
    `hideWindowDecorations` | `Boolean`          | `false`                                | Hide window decorations?
    `maximumHeight`         | `Int`              | `Int.MAX_VALUE / 8`                    | Maximum window height    
    `maximumWidth`          | `Int`              | `Int.MAX_VALUE / 8`                    | Maximum window width    
    `minimumHeight`         | `Int`              | `128`                                  | Minimum window height    
    `minimumWidth`          | `Int`              | `128`                                  | Minimum window width    
    `unfocusBehaviour`      | `UnfocusBehaviour` | `UnfocusBehaviour.NORMAL`              | The value `UnfocusBehaviour.THROTTLE` can be specified to throttle the program to 10Hz when unfocused.
    `vsync`                 | `Boolean`          | `true`                                 | Should the program wait for vertical retrace to avoid tearing? 
    `windowTransparent`     | `Boolean`          | `false`                                | Should the window be transparent?
    `windowResizable`       | `Boolean`          | `false`                                | Allow resizing of window?
    `windowAlwaysOnTop`     | `Boolean`          | `false`                                | Keep the window floating above other windows?

    See [the API](https://api.openrndr.org/openrndr-application/org.openrndr/-configuration/index.html) for an complete list.
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
                if (frameCount % 60 == 0) {
                    application.cursorVisible = Boolean.random()
                    application.windowPosition = Vector2.uniform(0.0, 200.0)
                }
            }
        }
    }
}
