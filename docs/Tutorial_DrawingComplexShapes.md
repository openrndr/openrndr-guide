# Drawing complex shapes #

FIX ME: Add a little sentence here. Question: should Relevant APIs be on top, or be under the other topics, or explain how to use the APIs??

## Shapes

OPENRNDR uses `Shape` to represent planar shapes of which the contours are described using piece wise bezier curves.

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

## Modifying shapes and contours

Note that `Shape`, `ShapeContour` and `Segment` are immutable; their state cannot be changed after creation. As such new objects with updated values have to be constructed.

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

## Querying shapes and contours


##### Shape queries
 * `Shape.outline` - the outer contour of the shape
 * `Shape.hole(index: Int)` - the holes in the shape

##### Contour queries
Contours are parameterized over `t` in `[0, 1]`
 * `ShapeContour.position(t: Double)` the position at `t` value
 * `ShapeContour.normal(t: Double)` the contour normal at `t`