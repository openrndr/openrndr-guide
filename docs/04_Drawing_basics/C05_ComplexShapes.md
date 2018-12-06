
# Complex shapes
OPENRNDR offers a lot of tools for the creation and drawing of two dimensional shapes.

## Shapes
OPENRNDR uses `Shape` to represent planar shapes of which the contours are described using piece-wise bezier curves.

A `Shape` is composed of one or multiple `ShapeContour` instances. A `ShapeContour` is composed of multiple `Segment` instances, describing a bezier curve each.

## Shape and ShapeContour builders
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

Let's create a simple `Contour` and draw it. The following program shows how to use the contour builder
        to create a triangular contour.

<img src="media/shapes-001.png"/>

```kotlin
program {
    extend {
        val c = contour {
            moveTo(Vector2(width / 2.0 - 150.0, height / 2.0 - 150.00))
            // -- here `cursor` points to the end point of the previous command
            lineTo(cursor + Vector2(300.0, 0.0))
            lineTo(cursor + Vector2(0.0, 300.0))
            lineTo(anchor)
            close()
        }
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = null
        drawer.contour(c)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C05_ComplexShapes000.kt)

Now let's create a `Shape` using the _shape builder_. The shape is created using two contours, one for
_outline_ of the shape, and one for the _hole_ in the shape

<img src="media/shapes-002.png"/>

```kotlin
program {
    extend {
        val s = shape {
            contour {
                moveTo(Vector2(width / 2.0 - 150.0, height / 2.0 - 150.00))
                lineTo(cursor + Vector2(300.0, 0.0))
                lineTo(cursor + Vector2(0.0, 300.0))
                lineTo(anchor)
                close()
            }
            contour {
                moveTo(Vector2(width / 2.0 - 80.0, height / 2.0 - 100.0))
                lineTo(cursor + Vector2(200.0, 0.0))
                lineTo(cursor + Vector2(0.0, 200.00))
                lineTo(anchor)
                close()
            }
        }
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = null
        drawer.shape(s)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C05_ComplexShapes001.kt)

## Shapes and contours from primitives

Not all shapes need to be created using the builders. Some of the OPENRNDR primitives have `.shape` and
`.contour` properties that help in creating shapes quickly.

 * `LineSegment.contour` and `LineSegment.shape`
 * `Rectangle.contour` and `Rectangle.shape`
 * `Circle.contour` and `Circle.shape`

## Shape Boolean-operations

Boolean-operations can be performed on shapes using the `compound {}` builder. There are three kinds
of compounds: _union_, _difference_ and _intersection_, all three of them are shown in the example below.


<img src="media/shapes-003.png"/>

```kotlin
program {
    extend {
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = null
        // -- shape union
        val su = compound {
            union {
                shape(Circle(185.0, height / 2.0 - 80.0, 100.0).shape)
                shape(Circle(185.0, height / 2.0 + 80.0, 100.0).shape)
            }
        }
        drawer.shapes(su)
        
        // -- shape difference
        val sd = compound {
            difference {
                shape(Circle(385.0, height / 2.0 - 80.0, 100.0).shape)
                shape(Circle(385.0, height / 2.0 + 80.0, 100.0).shape)
            }
        }
        drawer.shapes(sd)
        
        // -- shape intersection
        val si = compound {
            intersection {
                shape(Circle(585.0, height / 2.0 - 80.0, 100.0).shape)
                shape(Circle(585.0, height / 2.0 + 80.0, 100.0).shape)
            }
        }
        drawer.shapes(si)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C05_ComplexShapes002.kt)

The _compound builder_ is actually a bit more clever than what the previous example demonstrated because
it can actually work with an entire tree of compounds. Demonstrated below is the _union_ of two _intersections_.

<img src="media/shapes-004.png"/>

```kotlin
program {
    extend {
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = null
        val cross = compound {
            union {
                intersection {
                    shape(Circle(width / 2.0 - 160.0, height / 2.0, 200.0).shape)
                    shape(Circle(width / 2.0 + 160.0, height / 2.0, 200.0).shape)
                }
                intersection {
                    shape(Circle(width / 2.0, height / 2.0 - 160.0, 200.0).shape)
                    shape(Circle(width / 2.0, height / 2.0 + 160.0, 200.0).shape)
                }
            }
        }
        drawer.shapes(cross)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C05_ComplexShapes003.kt)

## Cutting contours

A contour be cut into a shorter contour using `ShapeContour.sub()`.

<video controls>
    <source src="media/shapes-005.mp4" type="video/mp4"></source>
</video>


```kotlin
program {
    extend {
        drawer.fill = null
        drawer.stroke = ColorRGBa.PINK
        drawer.strokeWeight = 4.0
        
        val sub0 = Circle(185.0, height / 2.0, 100.0).contour.sub(0.0, 0.5 + 0.50 * Math.sin(seconds))
        drawer.contour(sub0)
        
        val sub1 = Circle(385.0, height / 2.0, 100.0).contour.sub(seconds * 0.1, seconds * 0.1 + 0.1)
        drawer.contour(sub1)
        
        val sub2 = Circle(585.0, height / 2.0, 100.0).contour.sub(-seconds * 0.05, seconds * 0.05 + 0.1)
        drawer.contour(sub2)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C05_ComplexShapes004.kt)

## Placing points on contours 

A single point on a contour 

<video controls>
    <source src="media/shapes-006.mp4" type="video/mp4"></source>
</video>


```kotlin
program {
    extend {
    
        drawer.stroke = null
        drawer.fill = ColorRGBa.PINK
        
        val point = Circle(185.0, height / 2.0, 90.0).contour.position((seconds * 0.1) % 1.0)
        drawer.circle(point, 10.0)
        
        val points0 = Circle(385.0, height / 2.0, 90.0).contour.equidistantPositions(20)
        drawer.circles(points0, 10.0)
        

        val points1 = Circle(585.0, height / 2.0, 90.0).contour.equidistantPositions((Math.cos(seconds) * 10.0 + 30.0).toInt())
        drawer.circles(points1, 10.00)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C05_ComplexShapes005.kt)
