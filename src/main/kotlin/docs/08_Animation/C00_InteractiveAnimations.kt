@file:Suppress("UNUSED_EXPRESSION")

package docs.`08_Animation`

import org.openrndr.animatable.Animatable
import org.openrndr.application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text


fun main(args: Array<String>) {

    @Text
    """
    # Interactive animations

    ## Animatable

    Anything that should be animated inherits the `Animatable` class. The Animatable class provides animation logic.

    Displayed below is a very simple animation setup.
    """

    @Code.Block
    run {
        application {
            program {
                // animation definition
                class MyAnimatable() : Animatable() {
                    var x: Double = 50.0
                    var y: Double = 50.0
                }

                val myAnimatable = MyAnimatable()

                // sequence 1: animate x then y
                myAnimatable.apply {
                    ::x.animate(200.0, 3000)
                    ::x.complete()
                    ::y.animate(200.0, 3000)
                    ::y.complete()
                }

                // sequence 2: animate x and y simultaneously
                myAnimatable.apply {
                    ::x.animate(50.0, 3000)
                    ::y.animate(50.0, 3000)
                    ::y.complete()
                }

                // wait 2 seconds
                myAnimatable.delay(2000);

                // sequence 3, animate x, then animate y a bit later
                myAnimatable.apply {
                    ::x.animate(200.0, 3000)
                    delay(1000)
                    ::y.animate(200.0, 3000)
                    ::y.complete()
                }

                extend {
                    // -- update the animation
                    myAnimatable.updateAnimation()
                    drawer.circle(myAnimatable.x, myAnimatable.y, 20.0)
                }
            }
        }
    }

    @Text """
    For those wondering where that `::x.animate()` notation comes from, those are Kotlin's [property references](https://kotlinlang.org/docs/reflection.html#property-references).
    
    """

    @Text
    """
    ## Animation concepts
    ### Animating
    `animate()` queues a single animation for a single value. An animation consists of a target value and a duration and optionally a method of easing the animation.
    """

    @Text
    """
    ## Waiting

    ### Waiting for a period of time

    ```kotlin
    fun delay(duration: Long)
    ```

    `delay()` instructs the animator to wait for the given amount of time.

    ### Waiting for a moment in time

    ```kotlin
    fun waitUntil(time: Long)
    ```

    ### Waiting for an animation to finish

    ```kotlin
    fun complete(animatable: Animatable = this, callback: (Animatable) -> Unit = null)
    // usage: with no arguments
    myAnimatable.complete()

    fun complete(variable: String)
    fun complete(callback: (Animatable) -> Unit)
    ```

    `complete()` instructs the animator to wait until the previously queued animation has completed.
    `complete(String variable)` instructs the animator to wait until the last queued animation for the given variable has completed.

    This can be used in cases where two animations with different lengths are queued. For example:
    """

    @Code
    application {
        class MyAnimatable() : Animatable() {
            var x: Double = 50.0
            var y: Double = 50.0
        }

        val myAnimatable = MyAnimatable()
        program {

            run {
                myAnimatable.apply {
                    ::x.animate(400.0, 2000)
                    ::y.animate(500.0, 1200)
                    ::x.complete()
                }
            }
        }
    }

    @Text
    """
    ## Stopping animations

    In interactive applications it may be needed to stop animations to accommodate user actions. 
    """

    @Code.Block
    run {
        val animation = object : Animatable() {
            var x: Double = 0.0
            var y: Double = 0.0
        }
        // set up an animation
        animation.apply {
            ::x.animate(1.0, 1000)
            ::y.animate(1.0, 1000)
        }

        // cancel all animations
        animation.cancel()
    }

    @Text """In case you just want to cancel animations on a given property:"""

    @Code.Block
    run {
        val animation = object : Animatable() {
            var x: Double = 0.0
            var y: Double = 0.0
        }
        // set up an animation
        animation.apply {
            ::x.animate(1.0, 1000)
        }

        // cancel only the animations on the x property
        animation.apply {
            ::x.cancel()
        }
    }

    @Text "## Looping animations"
    @Text """While `Animatable` doesn't provide explicit support for looping animations. They can be achieved through the following pattern:"""

    @Code
    application {
        class MyAnimatable() : Animatable() {
            var x: Double = 0.0
        }

        val myAnimatable = MyAnimatable()
        program {
            extend {
                myAnimatable.updateAnimation()
                if (!myAnimatable.hasAnimations()) {
                    myAnimatable.apply {
                        ::x.animate(500.0, 1000)
                        ::x.complete()
                        ::x.animate(0.0, 1000)
                        ::x.complete()
                    }
                }
            }
        }
    }
}