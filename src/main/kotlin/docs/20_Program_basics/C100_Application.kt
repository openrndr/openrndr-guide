@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Application")
@file:ParentTitle("Program basics")
@file:Order("100")
@file:URL("programBasics/application")

package docs.`20_Program_basics`

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
    ## application
    
    The `application` block is used to setup the run-time environment 
    of the software we are writing. This block houses two other blocks:
    `configure` and `program`. Think of it as an `OPENRNDR` application.
    """
}
