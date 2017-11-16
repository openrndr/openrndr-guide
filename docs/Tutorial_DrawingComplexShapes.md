# Drawing complex shapes #

FIX ME: Add a little sentence here. Question: should Relevant APIs be on top, or be under the other topics, or explain how to use the APIs??

## Shape builder ##

The `ShapeBuilder` class offers a simple way of producing complex two dimensional shapes. `ShapeBuilder` employs a vocabulary that is familiar to those who have used SVG.

* `moveTo(position)` move the cursor to the given position
* `lineTo(position)` insert a line contour starting from the cursor, ending at the given position
* `curveTo(control, position)` insert a quadratic bezier curve starting from the cursor, ending at position
* `curveTo(controlA, controlB, position)` insert a cubic bezier curve starting from the cursor, ending at position
* `close` close the contour 

```java

void draw() {
    ShapeBuilder sb = new ShapeBuilder();
    sb.moveTo(xy(100,100)).
       curveTo(xy(200,200), xy(100,200)).
       curveTo(xy(300,300), xy(100,100)).
       close();
    drawer.shape(sb.build());
}

```

## Shapes and Polygons ##

`Shapes` have contours that are made of lines and Bézier curves. `Polygons` have contours that are made of lines only. Both shapes and polygons have one or more contours. The first contour describes the outline of the shape or polygon, the contours that follow describe holes.

It is possible to create a polygon that approximates a shape.
```java
// -- construct a polygon approximation of a shape
Polygon approximation = shape.polygon();
```

## Contours and segments ## 

```java

Shape shape = ...;

// -- iterate over all contours in a shape
for (Contour contour: shape) {
   // -- iterate over each segment in the contour
   for (Segment segment: contour) {

       // -- calculate the mid-point of the segment
       Vector2 mid = segment.position(0.5);
       
       // -- draw a circle at the position
       drawer.draw(mid, 5);
   }
}


```



