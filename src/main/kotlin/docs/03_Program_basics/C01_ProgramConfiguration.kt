@file:Suppress("UNUSED_EXPRESSION", "UNREACHABLE_CODE")

package docs.`03_Program_basics`

import org.openrndr.*
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.math.IntVector2


fun main(args: Array<String>) {

    @Text
    """
    # Program Configuration
    Starting your program with a custom configuration looks roughly like this.
    """

    @Code.Block
    run {
        fun main(args: Array<String>) = application {
            configure {
                // -- settings go here
            }
        }
    }

    @Text
    """
    ## Window size
    Setting the window size is done through the `width` and `height` properties.
    """

    @Code.Block
    run {
        fun main(args: Array<String>) = application {
            configure {
                width = 640
                height = 480
            }
        }
    }

    @Text
    """
    ## Window position
    The default value for `position` is `null` for which the default behaviour is to place the window at the center of the primary display
    """

    @Code.Block
    run {
        fun main(args: Array<String>) = application {
            configure {
                position = IntVector2(100, 400)
            }
        }
    }

    @Text
    """
    ## Fullscreen window

    Setting the window size is done through the `width` and `height` properties.

    """

    @Code.Block
    run {
        fun main(args: Array<String>) = application {
            configure {
                width = 1920
                height = 1080
                fullscreen = Fullscreen.SET_DISPLAY_MODE
            }
        }
    }

    @Text
    """
    or if no mode change is desired use `Fullscreen.CURRENT_DISPLAY_MODE`
    """


    @Code.Block
    run {
        fun main(args: Array<String>) = application {
            configure {
                fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
            }
        }
    }

    @Text
    """
    # Window Title
    """

    @Code.Block
    run {
        fun main(args: Array<String>) = application {
            configure {
                title = "Lo and behold!"
            }
        }
    }


    @Text
    """
    # Window unfocus behaviour
    Two window unfocus behaviours are available. In `NORMAL` behaviour the program continues running at full speed, in contrast the `THROTTLE` behaviour throttles the program to 10Hz.
    """

    run {
        var b = UnfocusBehaviour.NORMAL
        val a = when (b) {
            UnfocusBehaviour.NORMAL -> TODO()
            UnfocusBehaviour.THROTTLE -> TODO()
        }
    }

    @Code
    fun main(args: Array<String>) = application {
        configure {
            unfocusBehaviour = UnfocusBehaviour.THROTTLE
        }
    }

}
