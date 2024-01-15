@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Application program")
@file:ParentTitle("Program basics")
@file:Order("100")
@file:URL("programBasics/applicationProgram")

package docs.`03_Program_basics`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*

fun main() {

    @Text 
    """
    # Program basics

    Let's have a look at how an OPENRNDR program is structured. 
    Most programs will share a structure like the one below.
    """

    @Code
    application {
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

    @Text
    """
    ## The application block
    
    The `application` block is used to setup the run-time environment 
    of the software we are writing. This block houses two other blocks:
    `configure` and `program`.
    
    ## The configure block
    
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
    ## The program block
        
    The program block houses the actual programming logic. Note that `program {}` has a 
    [`Program`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-application/src/commonMain/kotlin/org/openrndr/Program.kt#L63) receiver.
    
    The code inside the `program` block is only executed after a window has 
    been created and a graphical context has been set up. This code is only 
    executed once.
    
    From the code block one can install extensions using `extend`. Extensions 
    are by default executed as often as possible. The most important type of 
    extension is the one that holds user code.
    
    A minimal application-program-extend setup would then look like this:
    """

    @Code
    application {
        program {
            extend {
                drawer.circle(width / 2.0, height / 2.0, 100.0)
            }
        }
    }
}
