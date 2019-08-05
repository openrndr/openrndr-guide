@file:Suppress("UNUSED_EXPRESSION")

package docs.`11_Advanced_Topics`

import org.openrndr.Application
import org.openrndr.Program
import org.openrndr.configuration
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text


fun main(args: Array<String>) {

    @Text
    """
    # Application Flow

    This section covers default and alternate application flows.

    ## Common application flow

    The most common application flow is one in which a single window is created in which your program will run until termination. The call to `Application.run()` below is blocking, it will only return when a termination is requested.
    """

    @Code.Block
    run {
        fun main(args: Array<String>) {
            class Main : Program() {
                override fun setup() {

                }
            }
            Application.run(Main(), configuration {
                // ...
            })
        }
    }


    @Text
    """
    ## Start-up or configuration dialogs

    In some scenarios it is desirable to present a simple dialog before the main program commences, for example in the case you
    want the user to to configure resolution and fullscreen settings. While OPENRNDR natively doesn't offer the tools to create user interfaces it does offer the functionality to create a window to host a configuration dialog.
    """


    @Code.Block
    run {
        fun main(args: Array<String>) {

            class Configuration : Program() {
                override fun setup() {
                    // ....
                }
            }

            class Main : Program() {
                override fun setup() {
                    // ...
                }
            }


            Application.run(Configuration(), configuration { })
            Application.run(Main(), configuration { })
        }
    }


    @Text
    """
     ## Multiple simultaneous application windows

     OPENRNDR comes with the support for using more than one simultaneous application window.
     The windows are backed by resource-sharing OpenGL contexts, as such resources can be used in more than one window.

     Setting up a multi-window application involves using the `Application.runAsync()` function.
    """


    @Code.Block
    run {
        class Left : Program() {
            override fun setup() {
                // ....
            }
        }

        class Right : Program() {
            override fun setup() {
                // ...
            }
        }

        fun main(args: Array<String>) {
            Application.runAsync(Left(), configuration { })
            Application.runAsync(Right(), configuration { })
        }
    }


}