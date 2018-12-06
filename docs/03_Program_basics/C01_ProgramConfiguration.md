
# Program Configuration
Starting your program with a custom configuration looks roughly like this.

```kotlin
fun main(args: Array<String>) = application {
    configure {// -- settings go here
    }
}
```

## Window size
Setting the window size is done through the `width` and `height` properties.

```kotlin
fun main(args: Array<String>) = application {
    configure {
        width = 640
        height = 480
    }
}
```

## Window position
The default value for `position` is `null` for which the default behaviour is to place the window at the center of the primary display

```kotlin
fun main(args: Array<String>) = application {
    configure {
        position = IntVector2(100, 400)
    }
}
```

## Fullscreen window

Setting the window size is done through the `width` and `height` properties.


```kotlin
fun main(args: Array<String>) = application {
    configure {
        width = 1920
        height = 1080
        fullscreen = true
    }
}
```

or if no mode change is desired set `width` and `height` to `-1`

```kotlin
fun main(args: Array<String>) = application {
    configure {
        width = -1
        height = -1
        fullscreen = true
    }
}
```

# Window Title

```kotlin
fun main(args: Array<String>) = application {
    configure {
        title = "Lo and behold!"
    }
}
```

# Window unfocus behaviour
Two window unfocus behaviours are available. In `NORMAL` behaviour the program continues running at full speed, in contrast the `THROTTLE` behaviour throttles the program to 10Hz.

```kotlin
fun main(args: Array<String>) = application {
    configure {
        unfocusBehaviour = UnfocusBehaviour.THROTTLE
    }
}
```
