 
 # Headless Applications

OPENRNDR can be ran in headless mode on machines that have EGL support. Using the EGL backed headless mode Programs can be ran without active graphical environment. This makes it for example possible to use OPENRNDR to create command line utilities that can be run in a SSH session or as a background service.

To enable headless mode all that needs to be done is setting the `headless` value in the `Configuration` to true. 
 
 ```kotlin
application {
    configure {
        headless = true
    }
    program {// ....
    }
}
``` 
 
 ## Limitations

#### Platforms

Only supported on platforms that support EGL for context creation; which is  Linux

#### Backbuffer

Headless applications cannot draw on the backbuffer, because there is no backbuffer. In order to draw you need to create a [RenderTarget](Tutorial_RenderTargets.md) and draw on there. Render target contents can easily be saved to file, or rendered to [video](Tutorial_VideoWriter.md)

#### Mouse and keyboard events
Headless applications cannot handle mouse or keyboard input.

#### ColorBufferLoader
Headless applications (currently) cannot create secondary/shared contexts and as such ColorBufferLoader does not work. 
