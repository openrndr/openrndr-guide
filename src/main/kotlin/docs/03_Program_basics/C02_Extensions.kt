package docs.`03_Program_basics`

import org.openrndr.application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.extensions.Screenshots

fun main(args: Array<String>) {
    @Text
    """
# Extensions

Extensions add functionality to a Program. Extensions can be used to control how a program draws, setup keyboard and
mouse bindings and much more.

"""
    @Text
"""## Basic extension use"""

    @Code.Block("")
    run {
        fun main(args: Array<String>) = application {
            program {
                extend(Screenshots())
            }
        }
    }

    @Text
    """## Extension configuration
Some extensions have configurable options. They can be set using the configuring `extend` function as follows:
    """.trimMargin()

    @Code.Block("")
    run {
        fun main(args: Array<String>) = application {
            program {
                extend(Screenshots()) {
                    scale = 4.0
                }
            }
        }
    }

    @Text
    """## Extension functions
The functional `extend` function allows one to use a single function as an extension.
"""

    @Code.Block("")
    run {
        fun main(args: Array<String>) = application {
            program {
                extend {
                    drawer.circle(width / 2.0, height / 2.0, 50.0)
                }
            }
        }
    }
}