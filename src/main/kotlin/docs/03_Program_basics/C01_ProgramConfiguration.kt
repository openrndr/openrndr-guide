package docs.`03_Program_basics`

import org.openrndr.Program
import org.openrndr.UnfocusBehaviour
import org.openrndr.application
import org.openrndr.configuration
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.math.IntVector2


fun main(args: Array<String>) {

    @Text
    """
# Program Configuration
Starting your program with a custom configuration looks roughly like this.
"""

    @Code.Block("")
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

    @Code.Block("")
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

    @Code.Block("")
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

    @Code.Block("")
    run {
        fun main(args: Array<String>) = application {
            configure {
                width = 1920
                height = 1080
                fullscreen = true
            }
        }
    }

    @Text
    """
or if no mode change is desired set `width` and `height` to `-1`
    """.trimIndent()


    @Code.Block("")
    run {
        fun main(args: Array<String>) = application {
            configure {
                width = -1
                height = -1
                fullscreen = true
            }
        }
    }

    @Text
"""
# Window Title
""".trimIndent()

    @Code.Block("")
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
""".trimIndent()

    run {
        var b = UnfocusBehaviour.NORMAL
        val a = when(b) {
            UnfocusBehaviour.NORMAL -> TODO()
            UnfocusBehaviour.THROTTLE -> TODO()
        }
    }

@Code.Block("")
run {
    fun main(args: Array<String>) = application {
        configure {
            unfocusBehaviour = UnfocusBehaviour.THROTTLE
        }
    }
}
}
