 
 # Presentation Control

OPENRNDR programs can use any of the two presentation modes.

The default mode is automatic presentation, the `draw` method is called as often as possible. The other mode is manual presentation, in which
it is the developer's responsibility to request `draw` to be called.

##### Setting the presentation mode

The presentation mode can be set and changed at run-time. 
 
 ```kotlin
application {
    program {
        window.presentationMode = PresentationMode.AUTOMATIC
    }
}
``` 
 
 ##### Using the manual presentation mode

The presentation mode is set to manual, a request to draw can be made using `window.requestDraw`.

In the following example `draw()` is only called after a mouse click. 
 
 ```kotlin
application {
    program {
        window.presentationMode = PresentationMode.MANUAL
        mouse.clicked.listen {
            window.requestDraw()
        }
        
        extend {
            drawer.background(ColorRGBa.PINK.shade(Math.random()))
        }
    }
}
``` 
 
 Note that in manual presentation mode `draw()` is still called when the window is resized. 
