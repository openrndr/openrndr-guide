# Clipping

OPENRNDR's drawer supports a single rectangular clip mask.

![clipping-1](_media/clipping-01.png)

In the image above we see a circle clipped against a rectangle.

##### Setting the clip rectangle

```kotlin
drawer.drawStyle.clip = Rectangle(0.0, 0.0, 400.0, 400.0)
```

The default clip value is `null`, the clip rectangle can be un-set as follows:

```kotlin
drawer.drawStyle.clip = null
```

##### Example

```
override fun draw() {
        drawer.background(ColorRGBa.BLACK)
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = null
        drawer.drawStyle.clip = Rectangle(mouse.position - Vector2(200.0, 200.0), 400.0, 400.0)
        drawer.circle(Vector2(width / 2.0, height / 2.0), 200.0)
    }
```
[full source](https://github.com/openrndr/openrndr-tutorials/blob/master/clip-001/src/main/kotlin/Example.kt)

