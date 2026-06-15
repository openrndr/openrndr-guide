@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Writing extensions")
@file:ParentTitle("Extensions")
@file:Order("150")
@file:URL("extensions/writingExtensions")

package docs.`45_Extensions`

import org.openrndr.Extension
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Matrix44

fun main() {

    @Text
    """
    # Writing Program extensions

    ## The extension interface

    OPENRNDR provides a simple `Extension` interface with which default 
    `Program` behaviour can be changed.

    ```kotlin
    interface Extension {
        var enabled: Boolean
        fun setup(program: Program) {}
        fun beforeDraw(drawer: Drawer, program: Program) {}
        fun afterDraw(drawer: Drawer, program: Program) {}
    }
    ```

    In the `setup()` function the extension can setup itself and hook into 
    program events.

    The `beforeDraw()` function is called right before the program's `draw()` 
    is executed.

    The `afterDraw()` is called after the program's `draw()` is executed.

    You can enabled and disable an extension by setting the `enabled` boolean 
    on the extension.

    ## A simple extension

    Presented here is the outline of a simple extension that overlays the 
    frames per second on top of the program output.
    """


    @Code
    class FPSDisplay : Extension {
        override var enabled: Boolean = true

        var frames = 0
        var startTime: Double = 0.0

        override fun setup(program: Program) {
            startTime = program.seconds
        }

        override fun afterDraw(drawer: Drawer, program: Program) {
            frames++

            drawer.isolated {
                // -- set view and projections
                drawer.view = Matrix44.IDENTITY
                drawer.ortho()

                drawer.text("fps: ${frames / (program.seconds - startTime)}")
            }
        }
    }


    @Text
    """
    Using the `FPSDisplay` extension from your main program would then 
    look like this:
    """

    @Code
    application {
        program {
            extend(FPSDisplay())
        }
    }


    @Text
    """
    ## Extension application order

    In the scenario in which a program has 3 extensions installed like in 
    the snippet below.

    ```kotlin
    fun setup() {
        extend(ExtensionA())
        extend(ExtensionB())
        extend(ExtensionC())
    }
    ```

    This resulting application order of beforeDraw and afterDraw is then as follows:

    ```kotlin
    extensionA.beforeDraw()
    extensionB.beforeDraw()
    extensionC.beforeDraw()

    program.draw()

    extensionC.afterDraw()
    extensionB.afterDraw()
    extensionA.afterDraw()
    ```

    As you can see, the afterDraw() calls are applied in reverse order, 
    this order was decided on to help with push/pop order of transforms and styles.
    """
}