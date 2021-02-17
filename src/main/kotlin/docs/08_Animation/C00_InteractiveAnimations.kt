@file:Suppress("UNUSED_EXPRESSION")

package docs.`08_Animation`

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.ffmpeg.ScreenRecorder


fun main(args: Array<String>) {

    @Text
    """
    # Interactive animations

    ## Animatable

    Anything that should be animated inherits the `Animatable` class. The Animatable class provides animation logic.

    Displayed below is a very simple animation setup in which we animate a circle from left to right. We do this by
    animating the `x` property of our animation object.
    """

    @Media.Video """media/animations-001.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 5.0
                outputFile = "media/animations-001.mp4"
            }
            // -- create an animation object
            val animation = object : Animatable() {
                var x = 0.0
                var y = 360.0
            }

            animation.apply {
                ::x.animate(width.toDouble(), 5000)
            }

            extend {
                animation.updateAnimation()
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(animation.x,  animation.y, 100.0)
            }
        }
    }


    @Text """
    By using `.complete()` we can create sequences of property animations.
    """
    @Media.Video """media/animations-002.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/animations-002.mp4"
            }
            val animation = object : Animatable() {
                var x = 0.0
                var y = 0.0
            }

            animation.apply {
                ::x.animate(width.toDouble(), 5000)
                ::x.complete()
                ::y.animate(height.toDouble(), 5000)
            }

            extend {
                animation.updateAnimation()
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(animation.x,  animation.y, 100.0)
            }
        }
    }

    @Text """
    If we leave out that `::x.complete()` line we will see that animations for `x` and `y` run simultaneously. 
    """
    @Media.Video """media/animations-003.mp4"""

    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/animations-003.mp4"
            }
            val animation = object : Animatable() {
                var x = 0.0
                var y = 0.0
            }

            animation.apply {
                ::x.animate(width.toDouble(), 5000)
                ::y.animate(height.toDouble(), 5000)
            }

            extend {
                animation.updateAnimation()
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(animation.x,  animation.y, 100.0)
            }
        }
    }


    @Text """
    For those wondering where that `::x.animate()` notation comes from, those are Kotlin's [property references](https://kotlinlang.org/docs/reflection.html#property-references).
    """

    @Text """ ## Easing
    A simple trick for making animations less stiff is to specify an easing. 

    To demonstrate we take one of the previously shown animations and add easings.
    """
    @Media.Video """media/animations-101.mp4"""
    @Application
    @Code
    application {
        program {
            @Exclude
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/animations-101.mp4"
            }
            val animation = object : Animatable() {
                var x = 0.0
                var y = 0.0
            }

            animation.apply {
                ::x.animate(width.toDouble(), 5000, Easing.CubicInOut)
                ::y.animate(height.toDouble(), 5000, Easing.CubicInOut)
            }

            extend {
                animation.updateAnimation()
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(animation.x,  animation.y, 100.0)
            }
        }
    }


    @Text "## Looping animations"
    @Text """While `Animatable` doesn't provide explicit support for looping animations. They can be achieved through the following pattern:"""

    @Code
    application {
        val animation = object : Animatable() {
            var x: Double = 0.0
        }

        program {
            extend {
                animation.updateAnimation()
                if (!animation.hasAnimations()) {
                    animation.apply {
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