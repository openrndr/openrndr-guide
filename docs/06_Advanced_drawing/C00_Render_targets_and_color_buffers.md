 
 # Render targets and color buffers 
 
 A `RenderTarget` models a place to draw to. A `RenderTarget` has two kind of buffer attachments
`ColorBuffer` attachments and `DepthBuffer` attachments. At least a single `ColorBuffer` attachment is needed to draw on be able to draw on a `RenderTarget`.

A `ColorBuffer` is a buffer that can hold up to 4 channel color. A `ColorBuffer` can hold 8 bit integer, 16 bit float or 32 bit float channels.

A `DepthBuffer` is a buffer that can hold depth and stencil values. 
 
 ## Creating a render target 
 
 The advised method of creating `RenderTarget` instances is to use the `renderTarget {}` builder 
 
 ```kotlin
val rt = renderTarget(640, 480) {}
``` 
 
 This creates a render target, but the render target does not have attachments that can hold the actual
image data. In the following snippet a render target with a single color buffer attachment is created using the
builder. 
 
 ```kotlin
val rt = renderTarget(640, 480) {
    colorBuffer()
}
``` 
 
 ## Drawing on a render target 
 
 In the following code snippet you will find an example showing how to draw on an off-screen buffer followed
by drawing that offscreen buffer on screen.  
 
 ```kotlin
program {
    // -- build a render target with a single color buffer attachment
    val rt = renderTarget(width, height) {
        colorBuffer()
    }
    
    extend {
        drawer.isolatedWithTarget(rt) {
            drawer.background(ColorRGBa.BLACK)
            drawer.fill = ColorRGBa.WHITE
            drawer.stroke = null
            drawer.rectangle(40.0, 40.0, 80.0, 80.0)
        }
        
        // draw the backing color buffer to the screen
        drawer.image(rt.colorBuffer(0))
    }
}
``` 
 
 ## Render targets and projection transformations 
 
 Keep in mind that projection transform has to be set to fit the render target, this becomes apparent
specifically when the used render target has dimensions that differ from those of the window. In case of orthographic
(2D) projections one can use the following: 
 
 ```kotlin
program {
    // -- build a render target with a single color buffer attachment
    val rt = renderTarget(400, 400) {
        colorBuffer()
    }
    
    extend {
        drawer.isolatedWithTarget(rt) {
            drawer.background(ColorRGBa.BLACK)
            
            // -- set the orthographic transform that matches with the render target
            ortho(rt)
            
            drawer.fill = ColorRGBa.WHITE
            drawer.stroke = null
            drawer.rectangle(40.0, 40.0, 80.0, 80.0)
        }
        
        // -- draw the backing color buffer to the screen
        drawer.image(rt.colorBuffer(0))
    }
}
``` 
 
 ## Compositing using render targets and alpha channels  
 
 OPENRNDR allows for compositing using `RenderTargets` through the use of transparency encoded in alpha
channels. The following code snippet uses two `RenderTarget` instances and clears them using `ColorRGBa.TRANSPARENT`. 
 
 ```kotlin
program {
    val rt0 = renderTarget(width, height) {
        colorBuffer()
    }
    val rt1 = renderTarget(width, height) {
        colorBuffer()
    }
    
    extend {
        drawer.stroke = null
        
        // -- bind our first render target, clear it, draw on it, unbind it
        drawer.isolatedWithTarget(rt0) {
            drawer.background(ColorRGBa.TRANSPARENT)
            drawer.fill = ColorRGBa.WHITE
            drawer.rectangle(40.0, 40.0, 80.0, 80.0)
        }
        
        // -- bind our second render target, clear it, draw on it, unbind it
        drawer.isolatedWithTarget(rt1) {
            drawer.background(ColorRGBa.TRANSPARENT)
            drawer.fill = ColorRGBa.PINK
            drawer.rectangle(140.0, 140.0, 80.0, 80.0)
        }
        // -- draw the backing color buffer to the screen
        drawer.image(rt0.colorBuffer(0))
        drawer.image(rt1.colorBuffer(0))
    }
}
``` 
 
 ## Creating high precision floating point render targets 
 
 The default color buffer format is unsigned 8 bit RGBa. There is support for floating point render targets. 
 
 ```kotlin
val rt = renderTarget(640, 480) {
    colorBuffer(ColorFormat.RGBa, ColorType.FLOAT16)
    colorBuffer(ColorFormat.RGBa, ColorType.FLOAT32)
}
``` 
 
 ## Named attachments 
 
 ```kotlin
val rt = renderTarget(640, 480) {
    colorBuffer("albedo", ColorFormat.RGBa, ColorType.FLOAT16)
    colorBuffer("position", ColorFormat.RGBa, ColorType.FLOAT32)
}
``` 
