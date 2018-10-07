# Drawing Primitives #

In this topic we introduce OPENRNDR's basic drawing primitives. We show how to draw lines, rectangles and circles. 

##### Relevant APIs

```kotlin
// -- circle drawing
Drawer.circle(x:Double, y:Double, radius:Double)
Drawer.circle(center: Vector2, radius:Double)
Drawer.circle(circle: Circle)

// -- rectangle drawing
Drawer.rectangle(x:Double, y:Double, width:Double, height:Double)
Drawer.rectangle(corner: Vector2, width:Double, height:Double)
Drawer.rectangle(rectangle: Rectangle)

// -- line segment drawing
Drawer.lineSegment(x0: Double, y0: Double, x1: Double, y1: Double)
Drawer.lineSegment(start:Vector2, end:Vector2)
Drawer.lineSegment(lineSegment: LineSegment)

// -- appearance attributes
Drawer.fill
Drawer.stroke
Drawer.strokeWeight

Drawer.background(color: ColorRGBa)
```

## Drawing circles ##

In the following example we use `Drawer.circle(x: Double, y: Double, radius: Double)` to draw a single circle. 

A circle is drawn around coordinates `x`, `y`, i.e. `x` and `y` specify the center of the circle.  

Circles are filled with the color set in `Drawer.fill` and their stroke is set to `Drawer.stroke`. The width of the stroke follows `Drawer.strokeWeight`.

```kotlin
// -- setup appearance
drawer.fill = ColorRGBa.RED
drawer.stroke = ColorRGBa.BLUE
drawer.strokeWeight = 1.0

// -- draw a circle with its center at (140.0, 140.0) with radius 130.0
drawer.circle(140.0, 140.0, 130.0)
}
```

Now, to draw a circle without fill we should set `Drawer.fill` to `null`:

```kotlin
drawer.fill = null
drawer.stroke = ColorRGBa.BLUE
drawer.strokeWeight = 5.0
drawer.circle(140.0, 140.0, 130.0)
```

Or to draw a circle without stroke we set `Drawer.stroke` to `null`:
```kotlin
drawer.stroke = null
drawer.fill = ColorRGBa.RED
drawer.strokeWeight = 5.0
drawer.circle(140.0, 140.0, 130.0)
```
You may have spotted the two other APIs for drawing circles; `Drawer.circle(center: Vector2, radius: Double)` and `Drawer.circle(circle: Circle)` and wonder what those are for. They are for drawing the exact same circle, but using arguments that may be more convenient in  scenarios in which values are provided by `Vector2` or `Circle` types.

For example the mouse position given by `mouse.position` is encoded in a `Vector2` type. By using the `Drawer.circle(center: Vector2)` overload we can easily draw a circle around the mouse cursor.
```
drawer.circle(mouse.position, 130.0)
```

## Drawing rectangles ##

```kotlin
void draw() {
    drawer.background(ColorRGBa.WHITE);

    // -- setup rectangle appearance
    drawer.fill = ColorRGBa.RED
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 1.0

    // -- draw a rectangle at (10,10) with width 120 and height 140
    drawer.rectangle(10.0, 10.0, 120.0, 140.0)
}
```

Similarly to circles the appearance of rectangles is affected by the properties `fill`, `stroke` and `strokeWeight`.


## Drawing lines

```kotlin
void draw() {
    drawer.background(ColorRGBa.WHITE)

    // -- setup line appearance
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 5.0

    // -- draw a line from (10,10) to (120, 140)
    drawer.lineSegment(10.0, 10.0, 120.0, 140.0)
}
```

## Drawing many primitives at once

If you're having performance issues drawing many primitives simultaneously, try switching to the batch versions of primitive drawing functions (`Drawer.rectangles`, `Drawer.circles`, `Drawer.lines`, etc.). These functions can draw many shapes at once much faster than individual draw calls. Example:

```kotlin
void draw() {
    drawer.background(ColorRGBa.WHITE)

    // -- setup rectangle appearance
    drawer.fill = ColorRGBa.RED
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 1.0
    drawer.rectangles((0 until 1000).map { Rectangle(Math.random() * width, Math.random() * height, 100.0, 100.0) })
    drawer.rectangles(rectangles);
}
```
