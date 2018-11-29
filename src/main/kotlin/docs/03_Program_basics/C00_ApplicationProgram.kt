package docs.`03_Program_basics`

import org.openrndr.Configuration
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text

fun main(args: Array<String>) {

    @Text
    """
# Application and Program

Your first OPENRNDR touch-point is the `Program` class. A `Program`
essentially describes all program logic.

`Program` comes with two methods that can be used to implement
program logic: `setup()` and `draw()`.

A simple `Program` implementation looks like this:
""".trimIndent()

    @Code.Block
    run {
        class SimpleProgram : Program() {
            override fun setup() {
                // -- setup GPU resources here
            }

            override fun draw() {
                // -- draw things here
            }
        }
    }

    @Text
    """
To run the program we have to create an application that hosts it:
"""

    run {
        class SimpleProgram:Program()
        @Code.Block
        run {
            fun main(args: Array<String>) {
                application(SimpleProgram(), Configuration())
            }
        }

    }


    @Text
    """
## Application builder function

We provide a simpler and cleaner alternative to the
`Application` / `Program` construction. It is the `application` builder
function, with which both Application and Program can be constructed as
simple as:

"""

@Code.Block
run {
    fun main() = application {
        configure {
            // set Configuration options here
        }

        program {
            // -- what is here is executed once
            extend {
                // -- what is here is executed 'as often as possible'
            }
        }
    }
}

@Text
"""
Internally this produces `Application`, `Program` and `Extension`
instances to make this work. The application builder function works
nicely for a programming style that seeks to avoid `lateinit var` as
no class fields are used in the `Program`.

We have only recently introduced this builder, it is our preferred way
of writing applications, but you will find we use the older method
through-out our documentation and example code. We are working on
converting our work to the new style and we advice that you use this new
style too.
"""

}