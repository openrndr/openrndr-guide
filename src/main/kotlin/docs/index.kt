@file:Suppress("UNUSED_EXPRESSION")
@file:Title("What is OPENRNDR?")
@file:Order("0")
@file:URL("/index")

package docs

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import kotlin.math.abs
import kotlin.math.cos

fun main() {
    @Text
    """
    # What is OPENRNDR?

    OPENRNDR is an application framework and a library for creative coding written in [Kotlin](https://kotlinlang.org).

    OPENRNDR offers APIs for easy and flexible programming of accelerated graphics.

    OPENRNDR is intended for prototyping as well as building production quality software.

    OPENRNDR is free and open source software. The source code can be found on [Github](https://github.com/openrndr/openrndr).

    OPENRNDR is an initiative of the [RNDR](http://rndr.studio) studio for interactive and interaction design based in The Netherlands.


    # A simple OPENRNDR program
    Here we show a very simple program written using OPENRNDR.
    """

    @Media.Video "media/what-is-001.mp4"

    @Application
    @ProduceVideo("media/what-is-001.mp4", 6.28318)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 568
        }
        program {
            extend {
                drawer.clear(ColorRGBa.PINK)
                drawer.fill = ColorRGBa.WHITE
                drawer.circle(
                    drawer.bounds.center,
                    abs(cos(seconds)) * height * 0.51
                )
            }
        }
    }
}