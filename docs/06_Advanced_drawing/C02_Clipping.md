 
 # Clipping 
 
 OPENRNDR's drawer supports a single rectangular clip mask. 
 
 <video controls>
    <source src="media/clipping-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
program {
    extend {
        drawer.stroke = null
        drawer.fill = ColorRGBa.PINK
        
        // -- set the rectangular clipping mask
        drawer.drawStyle.clip = Rectangle(100.0, 100.0, width - 200.0, height - 200.00)
        
        drawer.circle(Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(seconds) * height / 2.0 + height / 2.0, 200.0)
        
        // -- restore clipping
        drawer.drawStyle.clip = null
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/06_Advanced_drawing/C02_Clipping000.kt) 
