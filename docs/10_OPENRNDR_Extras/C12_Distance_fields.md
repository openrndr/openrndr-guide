 
 # Distance fields using orx-jumpflood  
 
 <video controls>
    <source src="media/distance-fields-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        
        val c = compose {
            layer {
                colorType = ColorType.FLOAT32
                
                draw {
                    drawer.image(image)
                }
                post(DistanceField()) {
                    threshold = cos(seconds * PI * 0.25) * 0.5 + 0.5
                    distanceScale = 0.008
                }
            }
        }
        
        extend {
            c.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C12_Distance_fields000.kt) 
 
 <video controls>
    <source src="media/distance-fields-002.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        
        val c = compose {
            // -- make sure accumulation is done in float32
            colorType = ColorType.FLOAT32
            layer {
                colorType = ColorType.FLOAT32
                draw {
                    drawer.image(image)
                }
                post(DistanceField()) {
                    threshold = cos(seconds * PI * 0.25) * 0.5 + 0.5
                    distanceScale = 1.0
                }
            }
            layer {
                draw {
                    // -- use the accumulation buffer to get the distance field
                    accumulation?.let {
                        val s = it.shadow
                        s.download()
                        drawer.fill = ColorRGBa.PINK
                        drawer.stroke = null
                        for (y in 0 until height step 10) {
                            for (x in 0 until width step 10) {
                                val distance = s[x, y].r
                                drawer.circle(x * 1.0, y * 1.0, distance * 0.05)
                            }
                        }
                        // -- clear the accumulated contents
                        it.fill(ColorRGBa.TRANSPARENT)
                    }
                }
            }
        }
        extend {
            c.draw(drawer)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C12_Distance_fields001.kt) 
