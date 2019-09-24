 
 # Drawing Primitives
In this topic we introduce OPENRNDR's basic drawing primitives. We show how to draw lines, rectangles and circles. 
 
 ## Drawing circles
A circle is drawn around coordinates `x`, `y`, i.e. `x` and `y` specify the center of the circle.
Circles are filled with the color set in `Drawer.fill` and their stroke is set to `Drawer.stroke`. The width of the stroke follows `Drawer.strokeWeight`. 
 
 <img src="media/circle-001.png"/> 
 
 ```kotlin
program {
    
    extend {
    
        drawer.background(ColorRGBa.PINK)
        drawer.fill = ColorRGBa.WHITE
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0
        drawer.circle(width / 6.0, height / 2.0, width / 8.0)
        
        // -- draw circle without fill, but with black stroke
        drawer.fill = null
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0
        drawer.circle(width / 6.0 + width / 3.0, height / 2.0, width / 8.0)
        
        // -- draw circle with white fill, but without stroke
        drawer.fill = ColorRGBa.WHITE
        drawer.stroke = null
        drawer.strokeWeight = 1.0
        drawer.circle(width / 6.0 + 2 * width / 3.0, height / 2.0, width / 8.0)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C00_DrawingPrimitives000.kt) 
 
 You may have spotted the two other APIs for drawing circles; `Drawer.circle(center: Vector2, radius: Double)` and `Drawer.circle(circle: Circle)` and wonder what those are for. They are for drawing the exact same circle, but using arguments that may be more convenient in scenarios in which values are provided by `Vector2` or `Circle` types. 
 
 ```kotlin
drawer.circle(mouse.position, 50.0)
``` 
 
 ### Drawing many circles
OPENRNDR provides special draw APIs for drawing many circles at once. Using these APIs is much faster than calling the `circle` API many times. 
 
 <img src="media/circle-002.png"/> 
 
 ```kotlin
program {
    
    extend {
        drawer.background(ColorRGBa.PINK)
        drawer.fill = ColorRGBa.WHITE
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0
        
        val circles = List(50000) {
            Circle(Math.random() * width, Math.random() * height, Math.random() * 10.0 + 10.0)
        }
        drawer.circles(circles)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C00_DrawingPrimitives001.kt) 
 
 ## Drawing rectangles 
 
 <img src="media/rectangle-001.png"/> 
 
 ```kotlin
program {
    
    extend {
        drawer.background(ColorRGBa.PINK)
        drawer.fill = ColorRGBa.WHITE
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0
        drawer.rectangle(width / 6.0 - width / 8.0, height / 2.0 - width / 8.0, width / 4.0, width / 4.0)
        
        // -- draw circle without fill, but with black stroke
        drawer.fill = null
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0
        drawer.rectangle(width / 6.0 - width / 8.0 + width / 3.0, height / 2.0 - width / 8.0, width / 4.0, width / 4.0)
        
        // -- draw circle with white fill, but without stroke
        drawer.fill = ColorRGBa.WHITE
        drawer.stroke = null
        drawer.strokeWeight = 1.0
        drawer.rectangle(width / 6.0 - width / 8.0 + 2.0 * width / 3.0, height / 2.0 - width / 8.0, width / 4.0, width / 4.0)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C00_DrawingPrimitives002.kt) 
 
 ## Drawing lines
Single lines are drawn per segment between two pairs of coordinates using `lineSegment`. Line primitives use `Drawer.stroke` to determine the color drawing color and `Drawer.strokeWeight` to determine the width of the line.

Line endings can be drawn in three styles by setting `Drawer.lineCap`

LineCap. | description
---------|------------
BUTT     | butt cap
ROUND    | round cap
SQUARE   | square cap 
 
 <img src="media/line-001.png"/> 
 
 ```kotlin
program {
    extend {
        drawer.background(ColorRGBa.PINK)
        // -- setup line appearance
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 5.0
        drawer.lineCap = LineCap.ROUND
        
        drawer.lineSegment(10.0, height / 2.0 - 20.0, width - 10.0, height / 2.0 - 20.0)
        
        drawer.lineCap = LineCap.BUTT
        drawer.lineSegment(10.0, height / 2.0, width - 10.0, height / 2.0)
        
        drawer.lineCap = LineCap.SQUARE
        drawer.lineSegment(10.0, height / 2.0 + 20.0, width - 10.0, height / 2.0 + 20.0)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C00_DrawingPrimitives003.kt) 
 
 ### Drawing line strips
A run of connected line segments is called a line strip and is drawn using `lineStrip`.
To draw a line strip one supplies a list of points between which line segments should be drawn. 
 
 <img src="media/line-002.png"/> 
 
 ```kotlin
program {
    
    extend {
        drawer.background(ColorRGBa.PINK)
        // -- setup line appearance
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 5.0
        drawer.lineCap = LineCap.ROUND
        
        val points = listOf(Vector2(10.0, height - 10.0), Vector2(width / 2.0, 10.0), Vector2(width - 10.0, height - 10.0))
        drawer.lineStrip(points)
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C00_DrawingPrimitives004.kt) 
