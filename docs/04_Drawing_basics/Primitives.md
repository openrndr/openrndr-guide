
# Drawing Primitives
In this topic we introduce OPENRNDR's basic 04_Drawing_basics primitives. We show how to draw lines, rectangles and circles.

## Relevant apis
```kotlin
// -- circle 04_Drawing_basics
Drawer.circle(x:Double, y:Double, radius:Double)
Drawer.circle(center: Vector2, radius:Double)
Drawer.circle(circle: Circle)

// -- rectangle 04_Drawing_basics
Drawer.rectangle(x:Double, y:Double, width:Double, height:Double)
Drawer.rectangle(corner: Vector2, width:Double, height:Double)
Drawer.rectangle(rectangle: Rectangle)

// -- line segment 04_Drawing_basics
Drawer.lineSegment(x0: Double, y0: Double, x1: Double, y1: Double)
Drawer.lineSegment(start:Vector2, end:Vector2)
Drawer.lineSegment(lineSegment: LineSegment)

// -- appearance attributes
Drawer.fill
Drawer.stroke
Drawer.strokeWeight

Drawer.background(color: ColorRGBa)
```

## Drawing circles
In the following example we use `Drawer.circle(x: Double, y: Double, radius: Double)` to draw a single circle.
A circle is drawn around coordinates `x`, `y`, i.e. `x` and `y` specify the center of the circle.
Circles are filled with the color set in `Drawer.fill` and their stroke is set to `Drawer.stroke`. The width of the stroke follows `Drawer.strokeWeight`.

```kotlin
extend {
    drawer.background(ColorRGBa.WHITE)
    // -- setup appearance
    drawer.fill = ColorRGBa.RED
    drawer.stroke = ColorRGBa.BLUE
    drawer.strokeWeight = 1.0
    // -- draw a circle with its center at (140.0, 140.0) with radius 130.0
    drawer.circle(140.0, 140.0, 130.0)
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/Primitives000.kt)

<img src="media/04_Drawing_basics-circles-001.png"/>
