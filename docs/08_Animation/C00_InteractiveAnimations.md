 
 # Interactive animations

## Animatable

Anything that should be animated inherits the [`Animatable`](https://api.openrndr.org/org.openrndr.animatable/-animatable/index.html) class. 
The Animatable class provides animation logic.

Displayed below is a very simple animation setup in which we animate a circle from left to right. We do this by
animating the `x` property of our animation object. 
 
 <video controls>
    <source src="media/animations-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
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
            drawer.circle(animation.x, animation.y, 100.0)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/08_Animation/C00_InteractiveAnimations000.kt) 
 
 By using `.complete()` we can create sequences of property animations. 
 
 <video controls>
    <source src="media/animations-002.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
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
            drawer.circle(animation.x, animation.y, 100.0)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/08_Animation/C00_InteractiveAnimations001.kt) 
 
 If we leave out that `::x.complete()` line we will see that animations for `x` and `y` run simultaneously.  
 
 <video controls>
    <source src="media/animations-003.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
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
            drawer.circle(animation.x, animation.y, 100.0)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/08_Animation/C00_InteractiveAnimations002.kt) 
 
 For those wondering where that `::x.animate()` notation comes from, those are Kotlin's [property references](https://kotlinlang.org/docs/reflection.html#property-references). 
 
 ## Easing
   A simple trick for making animations less stiff is to specify an easing. 

   To demonstrate we take one of the previously shown animations and add easings.
   
   Available [Easings](https://api.openrndr.org/org.openrndr.animatable.easing/-easing/index.html)  
 
 <video controls>
    <source src="media/animations-101.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
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
            drawer.circle(animation.x, animation.y, 100.0)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/08_Animation/C00_InteractiveAnimations003.kt) 
 
 ## Behavioral animation  
 
 <video controls>
    <source src="media/animations-301.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        
        class AnimatedCircle : Animatable() {
            var x = 0.0
            var y = 0.0
            var radius = 100.0
            var latch = 0.0
            
            fun shrink() {
                // -- first stop any running animations for the radius property
                ::radius.cancel()
                ::radius.animate(10.0, 200)
            }
            
            fun grow() {
                ::radius.cancel()
                ::radius.animate(Double.uniform(100.0, 140.0), 200)
            }
            
            fun jump() {
                ::x.cancel()
                ::y.cancel()
                ::x.animate(Double.uniform(0.0, width.toDouble()), 400)
                ::y.animate(Double.uniform(0.0, height.toDouble()), 400)
            }
            
            fun update() {
                updateAnimation()
                if (!::latch.hasAnimations) {
                    ::latch.animate(1.0, 300).completed.listen {
                        val action = listOf(::shrink, ::grow, ::jump).random()
                        action()
                    }
                }
            }
        }
        
        val animatedCircles = List(5) {
            AnimatedCircle()
        }
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            for (ac in animatedCircles) {
                ac.update()
                drawer.circle(ac.x, ac.y, ac.radius)
            }
        }
    }
}
``` 
 
 ## Looping animations 
 
 While `Animatable` doesn't provide explicit support for looping animations. They can be achieved through the following pattern: 
 
 <video controls>
    <source src="media/animations-401.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
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
``` 
 
 ## Animatable properties 
 
 Thus far we have only worked with `Double` properties in our animations. However, animation is not limited to 
Doubles.

Any property that is a [`LinearType`](https://api.openrndr.org/org.openrndr.math/-linear-type/index.html) can be
animated through Animatable. 
 
 ```kotlin
application {
    program {
        val animation = object : Animatable() {
            var color = ColorRGBa.WHITE
            var position = Vector2.ZERO
        }
        animation.apply {
            ::color.animate(ColorRGBa.PINK, 5000)
            ::position.animate(Vector2(width.toDouble(), height.toDouble()), 5000)
        }
    }
}
``` 
