# Interactive Animations

This section explains how to use OPENRNDR's `Animatable` class as a tool to create interactive animations.
In order to use `Animatable` your project needs to depend on the `rndr-animatable`.

```gradle
dependencies {
    compile "org.openrndr:openrndr-animatable:$openrndr_version"
}
```

## Animatable

Anything that should be animated inherits the Animatable class. The Animatable class provides animation logic.

Displayed below is a very simple animation setup.

```kotlin
class MyAnimatable() : Animatable() {
    var x: Double = 50.0
    var y: Double = 50.0
}

class MyProgram : Program() {

    val myAnimatable = MyAnimatable()

    override fun setup() {
        // sequence 1: animate x then y
        myAnimatable.apply {
            animate("x", 200.0, 3000)
            complete()
            animate("y", 200.0, 3000)
            complete()
        }

        // sequence 2: animate x and y simultaneously
        myAnimatable.apply {
            animate("x", 50.0, 3000)
            animate("y", 50.0, 3000)
            complete()
        }

        // wait 2 seconds
        myAnimatable.delay(2000);

        // sequence 3, animate x, then animate y a bit later
        myAnimatable.apply {
            animate("x", 200.0, 3000)
            delay(1000)
            animate("y", 200.0, 3000)
            complete();
        }
    }

    override fun draw() {
        myAnimatable.updateAnimation()
        drawer.circle(myAnimatable.x, myAnimatable.y, 20.0)
    }
}
```

## Animation concepts

### Animating

```kotlin
fun animate(variable: String, target: Double, duration: Long)
fun animate(variable: String, target: Double, duration: Long, easing: Easing)
```

`animate()` queues a single animation for a single value. An animation consists of a target value and a duration and optionally a method of easing the animation.

In cases in which the target value is unknown but the increment (or decrement) is known, one can use `add()`

```kotlin
fun add(variable: String, increment: Double, duration: Long)
fun add(variable: String, increment: Double, duration: Long, easing: Easing)
```

It is also possible to animate elements in an array or List:

```kotlin
class MyAnimatable : Animatable() {
    val values = DoubleArray(4)
}

...

val myAnimatable = MyAnimatable()
myAnimatable.animate("values[0]", 100.0, 1000)
myAnimatable.animate("values[1]", 200.0, 1000)

```


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

```kotlin
myAnimatable.apply {
    animate("x", 400, 2000)
    animate("y", 500, 1200)
    complete("x")
}
```

### Waiting for an animation to almost finish

```kotlin
Animatable.beforeComplete(time: Long)
// equivalent:
animatable.complete().delay(-time);
```
Instruct the animator to wait until the given amount of time before the previously queued animation ends.

## Stopping

In interactive applications it may be needed to stop animations to accommodate user actions. Animatable has three options to stop animations.

```kotlin
fun cancel()
```

`cancel()` cancels all running and queued animations. Animated values remain at their current value. `cancel()` is the most commonly used method for stopping animations.

```kotlin
fun cancelQueued()
```

`cancelQueued()` cancels all queued animations. Running animations will continue to run.

```kotlin
fun end()
```

`end()` cancels all running and queued animations. Animated values of running animations will be set to the target value, this will introduce animation pops.
