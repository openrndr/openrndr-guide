 
 # Image fit 
 
 `orx-image-fit` provides functionality to making the drawing and placement of images somewhat easier. 
`orx-image` Fits images in frames with two options, contain and cover, similar to CSS object-fit. 
 
 ## Contain mode 
 
 <video controls>
    <source src="media/image-fit-001.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        extend {
            val margin = cos(seconds * PI) * 50.0 + 50.0
            drawer.imageFit(image, 20.0, 20.0 + margin / 2, width - 40.0, height - 40.0 - margin, fitMethod = FitMethod.Contain)
            // -- illustrate the placement rectangle
            drawer.fill = null
            drawer.stroke = ColorRGBa.WHITE
            drawer.rectangle(20.0, 20.0 + margin / 2.0, width - 40.0, height - 40.0 - margin)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C10_Image_fit000.kt) 
 
 Additionally the placement of the image in the rectangle can be adjusted 
 
 <video controls>
    <source src="media/image-fit-002.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        extend {
            val margin = 100.0
            drawer.imageFit(image, 20.0, 20.0 + margin / 2, width - 40.0, height - 40.0 - margin, horizontalPosition = cos(seconds) * 1.0, fitMethod = FitMethod.Contain)
            // -- illustrate the placement rectangle
            drawer.fill = null
            drawer.stroke = ColorRGBa.WHITE
            drawer.rectangle(20.0, 20.0 + margin / 2.0, width - 40.0, height - 40.0 - margin)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C10_Image_fit001.kt) 
 
 ## Cover mode 
 
 <video controls>
    <source src="media/image-fit-101.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        extend {
            // -- calculate dynamic margins
            val xm = cos(seconds * PI) * 50.0 + 50.0
            val ym = sin(seconds * PI) * 50.0 + 50.0
            
            drawer.imageFit(image, 20.0 + xm / 2.0, 20.0 + ym / 2, width - 40.0 - xm, height - 40.0 - ym)
            
            // -- illustrate the placement rectangle
            drawer.fill = null
            drawer.stroke = ColorRGBa.WHITE
            drawer.rectangle(20.0 + xm / 2.0, 20.0 + ym / 2.0, width - 40.0 - xm, height - 40.0 - ym)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C10_Image_fit002.kt) 
 
 <video controls>
    <source src="media/image-fit-102.mp4" type="video/mp4"></source>
</video>
 
 
 Additionally the placement of the image in the rectangle can be adjusted 
 
 ```kotlin
application {
    program {
        val image = loadImage("data/images/cheeta.jpg")
        extend {
            val margin = 100.0
            drawer.imageFit(image, 20.0, 20.0 + margin / 2, width - 40.0, height - 40.0 - margin, verticalPosition = cos(seconds) * 1.0)
            
            // -- illustrate the placement rectangle
            drawer.fill = null
            drawer.stroke = ColorRGBa.WHITE
            drawer.rectangle(20.0, 20.0 + margin / 2.0, width - 40.0, height - 40.0 - margin)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C10_Image_fit003.kt) 
