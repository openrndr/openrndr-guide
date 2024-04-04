@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Program")
@file:ParentTitle("Program basics")
@file:Order("120")
@file:URL("programBasics/program")

package docs.`20_Program_basics`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    ## Program
        
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
            // This runs only once, and it's a good place to load assets.
            extend {
                // This runs many times per second
                drawer.circle(width / 2.0, height / 2.0, 100.0)
            }
        }
    }

}
