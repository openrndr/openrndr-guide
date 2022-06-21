@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Application Flow")
@file:ParentTitle("Advanced topics")
@file:Order("110")
@file:URL("advancedTopics/applicationFlow")

package docs.`11_Advanced_Topics`

import org.openrndr.Program
import org.openrndr.application
import org.openrndr.configuration
import org.openrndr.dokgen.annotations.*

fun main() {

    @Text 
    """
    # Application Flow
    
    This section covers default and alternate application flow.
    
    ## Default application flow
    
    The default application flow aims at single window application. 
    For clarity we list the skeleton for an OPENRNDR program below.
    """

    @Code.Block
    run {
        fun main() {
            // -- define an application
            application {
                // -- at this point there is no window or graphical context
                // -- attempting to work with graphics resources will lead to errors
                // -- configure application window
                configure {
                    width = 770
                    height = 578
                }
                // -- define the program
                program {
                    // -- at this point there is a graphical context

                    // -- extend the program with drawing logic
                    extend {
                    }
                }
            }
        }
    }

    @Text
    """
    ## Applications without application{} builder
    
    There may be scenarios in which a more traditional way of writing 
    applications is preferred.   
    """

    @Code.Block
    run {
        fun main() {
            class Main : Program() {
                override suspend fun setup() {
                    // -- setup program here
                }

                override fun draw() {
                    // -- draw here
                }
            }
            application(Main(), configuration {
                // ...
            })
        }
    }

    @Text
    """
    ## Start-up or configuration dialogs

    In some scenarios it is desirable to present a simple dialog before 
    the main program commences, for example in the case you
    want the user to to configure resolution and fullscreen settings. 
    While OPENRNDR natively doesn't offer the tools to create user interfaces 
    it does offer the functionality to create a window to host a 
    configuration dialog.
    """

    @Code.Block
    run {
        fun main() {
            val settings = object {
                var width: Int = 640
            }

            // -- configuration
            application {
                program {
                    // -- somehow get values in the settings object
                }
            }

            // -- application blocks until window is closed
            application {
                // -- configure using the settings object
                configure {
                    width = settings.width
                }
                program {

                }
            }
        }
    }
}