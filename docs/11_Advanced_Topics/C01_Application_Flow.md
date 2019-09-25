 
 # Application Flow 
 
 This section covers default and alternate application flow. 
 
 ## Default application flow 
 
 The default application flow aims at single window application. For clarity we list the skeleton for an OPENRNDR
program below. 
 
 ```kotlin
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
            extend {}
        }
    }
}
``` 
 
 ## Applications without application{} builder

There may be scenarios in which a more traditional way of writing applications is preferred.    
 
 ```kotlin
fun main() {
    class Main : Program() {
        override fun setup() {}
        
        override fun draw() {}
    }
    Application.run(Main(), configuration {// ...
    })
}
``` 
 
 ## Start-up or configuration dialogs

In some scenarios it is desirable to present a simple dialog before the main program commences, for example in the case you
want the user to to configure resolution and fullscreen settings. While OPENRNDR natively doesn't offer the tools to create user interfaces it does offer the functionality to create a window to host a configuration dialog. 
 
 ```kotlin
fun main(args: Array<String>) {
    val settings = object {
        var width: Int = 640
    }
    
    // -- configuration
    application {
        program {// -- somehow get values in the settings object
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
``` 
