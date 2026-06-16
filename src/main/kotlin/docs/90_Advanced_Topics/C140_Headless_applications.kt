@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Headless applications")
@file:ParentTitle("Advanced topics")
@file:Order("140")
@file:URL("advancedTopics/headlessApplications")

package docs.`90_Advanced_Topics`

import org.openrndr.dokgen.annotations.*


fun main() {

    @Text
    """
    # Headless Applications

    OPENRNDR can be ran in headless mode on machines that have EGL support. 
    Using the EGL backed headless mode Programs can be ran without active graphical environment. This makes it for example possible to use OPENRNDR to create command line utilities that can be run in a SSH session or as a background service.

    The default backend on the JVM is GLFW, in order to use headless mode you will need to run OPENRNDR 
    with EGL by adding `-Dorg.openrndr.application=EGL` to the VM arguments in the launch configuration.

    ## Limitations

    #### Platforms

    Only supported on platforms that support EGL for context creation; 
    which is Linux.

    #### Backbuffer

    Headless applications cannot draw on the backbuffer, because there 
    is no backbuffer. In order to draw you need to create a 
    [RenderTarget](https://guide.openrndr.org/drawing/renderTargets.html) and draw on it. 
    Render target contents can easily be saved to file, or rendered to 
    [video](https://guide.openrndr.org/extensions/screenRecorder.html#writing-to-video-using-render-targets).

    #### Mouse and keyboard events
    
    Headless applications cannot handle mouse or keyboard input.

    #### ColorBufferLoader
    
    Headless applications (currently) cannot create secondary/shared 
    contexts and as such ColorBufferLoader does not work.
    """
}