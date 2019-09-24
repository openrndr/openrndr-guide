@file:Suppress("UNUSED_EXPRESSION")

package docs.`01_Before_you_start`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.ffmpeg.ScreenRecorder

fun main() {
    @Text
    """
    # What is OPENRNDR?

    OPENRNDR is an application framework and a library for creative coding written in [Kotlin](https://kotlinlang.org).

    OPENRNDR offers APIs with which hardware accelerated graphics can be programmed flexibly and easily.

    OPENRNDR is intended for prototyping as well as building production quality software.

    OPENRNDR is free and open source software. The source code can be found on [Github](https://github.com/openrndr/openrndr).

    OPENRNDR is an initiative of [RNDR](http://rndr.studio) a studio for interactive and interaction design based in The Netherlands.


    # A simple OPENRNDR program
    Here we show a very simple program written using OPENRNDR.
    """
    @Media.Video """media/what-is-001.mp4"""

    @Application
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 568
        }
        program {
            @Exclude
            extend(ScreenRecorder()) {
                outputFile = "media/what-is-001.mp4"
                maximumDuration = 10.0
                quitAfterMaximum = true
            }
            extend {
                drawer.background(ColorRGBa.PINK)
                drawer.fill = ColorRGBa.WHITE
                drawer.circle(drawer.bounds.center, Math.abs(Math.cos(seconds)) * height*0.5)
            }
        }
    }


}
