 
 # What is OPENRNDR?

OPENRNDR is an application framework and a library for creative coding written in [Kotlin](https://kotlinlang.org).

OPENRNDR offers APIs with which hardware accelerated graphics can be programmed flexibly and easily.

OPENRNDR is intended for prototyping as well as building production quality software.

OPENRNDR is free and open source software. The source code can be found on [Github](https://github.com/openrndr/openrndr).

OPENRNDR is an initiative of [RNDR](http://rndr.studio) a studio for interactive and interaction design based in The Netherlands.


# A simple OPENRNDR program
Here we show a very simple program written using OPENRNDR. 
 
 <video controls>
    <source src="media/what-is-001a.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        extend {
            drawer.clear(ColorRGBa.PINK)
            drawer.fill = ColorRGBa.WHITE
            drawer.circle(drawer.bounds.center, Math.abs(Math.cos(seconds)) * height * 0.5)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/01_Before_you_start/C03WhatIsOPENRNDR2000.kt) 
