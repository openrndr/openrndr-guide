@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Logging and debugging")
@file:Order("1085")
@file:URL("debugging/index")

package docs.`85_Debugging`

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    # Logging

    OPENRNDR uses kotlin-logging, which is a Kotlin flavoured wrapper 
    around slf4j, to log its internal workings. Log messages are displayed
    in the IDE's console and saved to the `application.log` file.

    ##### Configure logging

    In [`build.gradle.kts`](https://github.com/openrndr/openrndr-template/blob/ff1942d2a821c540d7b7d804e201c6462890f7e2/build.gradle.kts#L84) 
    we can modify the `applicationLogging` variable.
    - `Logging.NONE` to disable logging.
    - `Logging.SIMPLE` to display monochrome log messages in the console.
    - `Logging.FULL` to display coloured log messages in the console to 
    distinguish their types.
    
    In [`src/main/resources/log4j2.yaml`](https://github.com/openrndr/openrndr-template/blob/ff1942d2a821c540d7b7d804e201c6462890f7e2/src/main/resources/log4j2.yaml#L18) 
    we can change the 
    [logging level](https://logging.apache.org/log4j/2.x/manual/customloglevels.html).
    To make it verbose we can replace `level: info` with `level: all`.
    
    ##### Exception handling
    
    It is possible to change how exception errors are presented by IntelliJ IDEA:
    Open the `Run > Edit Configurations` menu and make sure the `VM Options` text
    field contains `-Dorg.openrndr.exceptions=JVM`. 
    This can sometimes help figure out why a program is crashing.
    
    ##### Crashing shaders

    If a ShadeStyle crashes, a `ShaderError.glsl` file is created at the
    root of the project. The content of the file is the actual shader
    program OPENRNDR tried to use. Studying this program can help figure out
    why shaders fail. A common reason is using incorrect names for methods,
    uniforms or variables.
    
    ##### Debugging video exporting
    
    When a video file is produced, a `ffmpegOutput.txt` is created at the
    root of the project. Studying this file can help diagnose problems
    with video exporting.
    
    ### Enabling OpenGL debug messages

    If your graphics hardware and drivers support OpenGL debug contexts 
    you can open the `Run > Edit Configurations...` menu in
    IntelliJ and make sure the `VM Options` text field contains
    `-Dorg.openrndr.gl3.debug=true` to enable the debug messages.
    
    ### Using RenderDoc
    
    [RenderDoc](https://renderdoc.org/) is 
    a graphics debugger currently available for Vulkan, D3D11, D3D12, 
    OpenGL, and OpenGL ES development on Windows, Linux, Android, Stadia, 
    and Nintendo Switch™.
    
    [This post](https://openrndr.discourse.group/t/using-renderdoc-to-debug-low-level-graphics-advanced/118)
    explains how to use RenderDoc with OPENRNDR.
    
    """.trimIndent()
}
