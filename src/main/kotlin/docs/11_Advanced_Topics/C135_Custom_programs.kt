@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Custom Programs")
@file:ParentTitle("Advanced topics")
@file:Order("135")
@file:URL("advancedTopics/customPrograms")

package docs.`11_Advanced_Topics`

import org.openrndr.KEY_ESCAPE
import org.openrndr.ProgramImplementation
import org.openrndr.dokgen.annotations.*

fun main() {

    @Text
    """
    # Creating Custom Programs
    
    You can write custom programs that replace the `program { ... }` block with your own `ProgramImplementation`.
    
    If you have your own implementation, you can reuse code across applications.
    
    ## Understanding the minimum application
    
    The minimum running program looks like this:
    
    ```kotlin
    fun main() = application {
        configure {
            // ...
        }
        program {
            // ...
        }
    }
    ```
    
    The invocation of `application { ... }` creates an `ApplicationBuilder` that has a default `program = ProgramImplementation()`.
    
    By calling `program { ... }`, a new `ProgramImplementation` is created and the application sets it as its program.
    
    ## Inheriting from ProgramImplementation
    
    Write your own class that inherits from `ProgramImplementation`:
    """

    @Code
    class MyProgram(
        // -- REQUIRED
        private val init: suspend MyProgram.() -> Unit,
    ): ProgramImplementation(suspend = false) {

        // -- Custom property
        val screenRatio by lazy { width * 1.0 / height }

        // -- Custom method
        fun listenToExitOnPressingEscape() {
            keyboard.keyDown.listen {
                if (it.key == KEY_ESCAPE) application.exit()
            }
        }

        // -- You can overwrite existing implementations. Responsible overwrites use `super`.
        override fun updateFrameSecondsFromClock() {
            println("Now updating the frameSeconds value")
            super.updateFrameSecondsFromClock()
        }

        // -- REQUIRED
        override suspend fun setup() {
            super.setup()
            init()
        }
    }

    @Text
    """
    Note: During program creation values like `width` and `height` don't exist yet.
    Therefore, this will not work:
    
    ```kotlin 
    val screenRatio = width * 1.0 / height
    ```
    
    Instead you have to write it as a `lazy` property like above. 
    Alternatives are declarations as `lateinit var` or as nullables (like `Double?`) and then setting them during `setup()`.
    
    ## Using your ProgramImplementation
    
    To mimic the style of the default program, you can define the following function:
    
    ```kotlin
    fun ApplicationBuilder.myProgram(
        init: suspend MyProgram.() -> Unit
    ): MyProgram {
        program = MyProgram(init)
        return program as MyProgram
    }
    ```
    
    Now, you can write the application like so:
    
    ```kotlin
    fun main() = application {
        configure {
            // ...
        }
        myProgram {
            val center = drawer.bounds.center
            // -- Now available in scope
            val rectWidth = 100.0 * screenRatio
            val rectHeight = 100.0 
            
            
            // -- Now available in scope
            listenToExitOnPressingEscape()
            
            extend {
                drawer.fill = ColorRGBa.PINK
                drawer.rectangle(center.x - rectWidth/2.0, center.y - rectHeight/2.0, rectWidth, rectHeight)
            }
        }
    }
    ```
    
    ## Compile time benefit
    
    Should you have multiple OPENRNDR applications in the same project, and want to run any of them, all files are compiled.
    Depending on the number of applications, this can slow down build times.
    
    If you can refactor common functionality into your implementation, then it is compiled only once across applications.
    """
}