# Drawing Primitives #

In this topic we introduce OPENRNDR's basic drawing primitives.

##### Relevant APIs

```kotlin

Drawer.circle(double x, double y, double radius)
Drawer.rectangle(double x, double y, double width, double height)
Drawer.line(double x0, double y0, double x1, double y1)

Drawer.fill
Drawer.stroke
Drawer.strokeWeight
```

## Drawing circles ##

```kotlin
void draw() {
    background(ColorRGBa.GRAY)

    // -- setup appearance
    drawer.fill = ColorRGBa.RED
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 1.0

    // -- draw a circle with its center at (140,140) with radius 130
    drawer.circle(Vector2(140.0, 140.0), 130.0)
}
```

The appearance of circles is affected by the properties `fill`, `stroke` and `strokeWeight`.

## Drawing rectangles ##

```java
void draw() {
    background(ColorRGBa.WHITE);

    // -- setup rectangle appearance
    drawer.fill(ColorRGBa.RED);
    drawer.stroke(ColorRGBa.BLACK);
    drawer.strokeWeight(1.0);

    // -- draw a rectangle at (10,10) with width 120 and height 140
    drawer.rectangle(10.0, 10.0, 120.0, 140.0)
}
```

The appearance of rectangles is affected by the properties `fill`, `stroke` and `strokeWeight`.

## Drawing lines

```kotlin
void draw() {
    drawer.background(ColorRGBa.WHITE)

    // -- setup line appearance
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 5.0

    // -- draw a line from (10,10) to (120, 140)
    drawer.lineSegment(10,10, 120, 140)
}
```

## Drawing many primitives at once

If you're having performance issues drawing many primitives simultaneously, try switching to the batch versions of primitive drawing functions (`Drawer.rectangles`, `Drawer.circles`, `Drawer.lines`, etc.). These functions can draw many shapes at once much faster than individual draw calls. Example:

```kotlin
void draw() {
    background(ColorRGBa.WHITE)

    // -- setup rectangle appearance
    drawer.fill = ColorRGBa.RED
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 1.0
    drawer.rectangles((0 until 1000).map { Rectangle(Math.random() * width, Math.random() * height, 100.0, 100.0) })
    drawer.rectangles(rectangles);
}
```
