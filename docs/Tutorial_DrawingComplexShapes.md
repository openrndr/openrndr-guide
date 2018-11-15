# Drawing complex shapes #

OPENRNDR offers a lot of tools for the creation and drawing of two dimensional shapes.

## Shapes

OPENRNDR uses `Shape` to represent planar shapes of which the contours are described using piece-wise bezier curves.

A `Shape` is composed of one or multiple `ShapeContour` instances. A `ShapeContour` is composed of multiple `Segment` instances.

The first `ShapeContour` of a  `Shape` represents the outline of a shape, the secondary contours describe holes in the shape.

## Shape and contour builders ##

The `ContourBuilder` class offers a simple way of producing complex two dimensional shapes. `ContourBuilder` employs a vocabulary that is familiar to those who have used SVG.

* `moveTo(position)` move the cursor to the given position
* `lineTo(position)` insert a line contour starting from the cursor, ending at the given position
* `moveOrLineTo(position)` move the cursor if no cursor was previously set or draw a line
* `curveTo(control, position)` insert a quadratic bezier curve starting from the cursor, ending at position
* `curveTo(controlA, controlB, position)` insert a cubic bezier curve starting from the cursor, ending at position
* `continueTo(position)` inside a quadratic bezier curve starting from the cursor and reflecting the tangent of the last control
* `continueTo(controlB, position)` insert a cubic spline
* `arcTo(radiusX, radiusY, largeAngle, sweepFlag, position)`
* `close()` close the contour
* `cursor` a `Vector2` instance representing the current position
* `anchor` a `Vector2` instance representing the current anchor

### Drawing a single contour using the contour builder

In the following snippet a `ShapeContour` is built using the `contour {}` builder. Here we see how `cursor` is used to simulate relative positioning.

```kotlin
fun draw() {
    val c = contour {
        moveTo(Vector2(10.0, 10.0))
        lineTo(cursor + Vector2(100.0, 100.0))
        lineTo(cursor - Vector2(10.0, 10.0))
    }
    drawer.contour(c)
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-001/src/main/kotlin/Example.kt)

### Drawing a shape using the shape builder

In the following snippet a `Shape` is built using the `shape {}` and `contour {}` builders in tandem.

```kotlin
fun draw() {
    val s = shape {
        // -- describe outline of shape
        contour {
            [...]
        }
        // -- describe hole in the shape
        contour {
            [...]
        }
    }
    drawer.shape(s)
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-002/src/main/kotlin/Example.kt)

### Creating contours and shapes using primitives

`Rectangle`, `Circle` and `LineSegment` instances can be used to create shapes and contours easily.

##### Relevant APIs
```kotlin
Rectangle.contour
Rectangle.shape

Circle.contour
Circle.shape

LineSegment.contour
Linesegment.shape
```

```kotlin
drawer.contour(Circle(Vector2(100.0, 100.0), 100.0).contour)
drawer.shape(Rectangle(Vector2(200.0, 200.0), 100.0, 100.0).shape)
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-003/src/main/kotlin/Example.kt)


## Shape Boolean-operations
Boolean-operations can be performed on shapes using the `compound {}` builder.

A union operation would look like this

```kotlin
val shapeUnion = compound {
    union {
        shape(Circle(Vector2(100.0, 100.0), 100.0).shape)
        shape(Circle(Vector2(195.0, 100.0), 10.0).shape)
    }
}
drawer.shapes(sum)
```

And a difference (or subtract) operation like this:

```kotlin
val shapeDifference = compound {
    difference {
        shape(Circle(Vector2(100.0, 100.0), 100.0).shape)
        shape(Circle(Vector2(195.0, 100.0), 10.0).shape)
    }
}
drawer.shapes(sum)
```

And finally the intersection like this:
```kotlin
val shapeIntersection = compound {
    intersection {
        shape(Circle(Vector2(100.0, 100.0), 100.0).shape)
        shape(Circle(Vector2(195.0, 100.0), 10.0).shape)
    }
}
drawer.shapes(sum)
```

The `compound {}` builder can be used to create more complex compounds
in a tree-like fashion:

```kotlin
val shapeIntersection = compound {
    intersection {
        union {
            shape(Circle(Vector2(100.0, 100.0), 100.0).shape)
            shape(Circle(Vector2(195.0, 100.0), 10.0).shape)
        }
        shape(Circle(Vector2(95.0, 100.0), 10.0).shape)
    }
}
```

## Modifying shapes and contours

Note that `Shape`, `ShapeContour` and `Segment` are immutable; their values cannot be changed after creation. As such new objects with updated values have to be constructed.

### Affine/Linear transforms

A linear transform is represented by a `Matrix44`, one can use the `transform {}` builder to construct such a transform.

```
fun draw {
    // -- construct a transform using the transform{} builder
    val t = transform {
        translate(3.0, 4.0)
        rotate(40.0)
    }

    val c = contour {
        moveTo(Vector2(10.0, 10.0))
        lineTo(cursor + Vector2(100.0, 100.0))
        lineTo(cursor - Vector2(10.0, 10.0))
    }

    val ct = c.transform(t)
    drawer.contour(ct)
}
```

### Non-linear transforms

```kotlin
fun draw {
    val c = contour {
        moveTo(Vector2(10.0, 10.0))
        lineTo(cursor + Vector2(100.0, 100.0))
        lineTo(cursor - Vector2(10.0, 10.0))
    }
    val cm = Contour(c.segments.map {
        it
    })
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-004/src/main/kotlin/Example.kt)

## Querying shapes and contours

##### Relevant APIs
```kotlin
Shape.outline: Contour                     // the outer contour of the shape
Shape.hole(index: Int): Contour            // the holes in the shape
Shape.map(mapper:(ShapeContour)->ShapeContour) : Shape 
 
ShapeContour.position(t: Double): Vector2  // the position at t in [0, 1]
ShapeContour.normal(t: Double): Vector2    // the normal at t in [0, 1]
ShapeContour.winding: Window               // the winding of the contour
ShapeContour.bounds: Rectangle             // the bounds of the contour
ShapeContour.adaptivePositions(distanceTolerance: Double = 0.5): List<Vector2> 
ShapeContour.equidistantPositions(pointCount: Int): List<Vector2> 
ShapeContour.on(point: Vector2): Double?
ShapeContour.project(point: Vector2): ContourProjection 
ShapeContour.sub(t0: Double, t1: Double)   // a cut of the shape contour starting at t0 ending at t1
ShapeContour.reversed                      // reversed copy of the contour
ShapeContour.map(closed: Boolean=this.closed, mapper:(Segment)->Segment):ShapeContour 

Segment.position(t: Double): Vector2
```
### Getting a single point on a contour

```kotlin
val position = Circle(Vector2(100.0, 100.0), 100.0).contour.position(Math.cos(seconds) * 0.5 + 0.5)
drawer.circle(position, 10.0)
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-005/src/main/kotlin/Example.kt)

### Getting a number of equidistant points on a contour

```kotlin
val positions = Circle(Vector2(100.0, 100.0), 100.0).contour.equidistantPositions(40)
drawer.circles(positions, 10.0)
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-005/src/main/kotlin/Example.kt)

### Getting a subsection of a contour

```kotlin
drawer.contour( Circle(Vector2(100.0, 100.0), 100.0).contour.sub(0.0, 0.5)
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/complex-shapes-004/src/main/kotlin/Example.kt)
