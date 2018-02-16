# Program configuration

Starting your program with a custom configuration looks roughly like this.

```kotlin
fun main(Array<String> args) {
    application(MyProgram(), configuration {
        // -- settings here
    })
}
```

## Window size

Setting the window size is done through the `width` and `height` properties.

```kotlin
configuration {
    width = 640
    height = 480
}
```

## Window position

```kotlin
configuration {
    position = IntVector2(100, 400)
}
```

The default value for `position` is `null` for which the default behaviour is to place the window at the center of the primary display

## Fullscreen window

Setting the window size is done through the `width` and `height` properties.

```kotlin
configuration {
    width = 1920
    height = 1080
    fullscreen = true
}
```

or if no mode change is desired

```kotlin
configuration {
    width = -1
    height = -1
    fullscreen = true
}
```


## Window Title

Setting the window title is achieved by setting the `title` property in the configuration
```kotlin
configuration {
    title = "Lo and behold!"
}
```