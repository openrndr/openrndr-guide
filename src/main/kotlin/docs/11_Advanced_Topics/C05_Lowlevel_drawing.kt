package docs.`11_Advanced_Topics`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.internal.Driver
import org.openrndr.math.Vector3

fun main() {
    @Text """
# Lowlevel Drawing

This text is intended for developers looking to provide drawing functionality that is fully independent of 
the `Drawer` class. Developers that desire to do so will essentially use the same building blocks that OPENRNDR
uses internally.
    
A good reference for lowlevel drawing is the `Filter` class and it is encouraged to look at its [source code](https://github.com/openrndr/openrndr/blob/master/openrndr-core/src/main/kotlin/org/openrndr/draw/Filter.kt)
as it provides a good idea of how all the lowlevel components interact and its implementation is fully independent from
`Drawer`.
    
All lowlevel drawing is performed using OPENRNDR's `Driver` class. This class provides a handful of functions for
drawing. Below you will find a list of the essential ones.
 * `Drawer.setState()`    
 * `Drawer.drawVertexBuffer()`
 * `Drawer.drawIndexedVertexBuffer()`        
 * `Drawer.drawInstances()`
 * `Drawer.drawIndexedInstances()`
 
Classes that belong to the lowlevel drawing API:
 * [`Shader`](https://api.openrndr.org/org.openrndr.draw/-shader/index.html)
 * [`IndexBuffer`](https://api.openrndr.org/org.openrndr.draw/-index-buffer/index.html)
 * [`VertexBuffer`](https://api.openrndr.org/org.openrndr.draw/-vertex-buffer/index.html)
 * [`VertexFormat`](https://api.openrndr.org/org.openrndr.draw/-vertex-format/index.html)
 * [`ColorBuffer`](https://api.openrndr.org/org.openrndr.draw/-color-buffer/index.html)
 * [`DepthBuffer`](https://api.openrndr.org/org.openrndr.draw/-depth-buffer/index.html)
 * [`ArrayTexture`](https://api.openrndr.org/org.openrndr.draw/-array-texture/index.html)
 * [`CubeMap`](https://api.openrndr.org/org.openrndr.draw/-cubemap/index.html)
 * [`DrawStyle`](https://api.openrndr.org/org.openrndr.draw/-draw-style/index.html)
 * [`RenderTarget`](https://api.openrndr.org/org.openrndr.draw/-render-target/index.html)

We discourage writing code that uses OpenGL directly; even though currently only an OpenGL 3.3 implementation exists 
for `Driver`, in the future we may add implementations that are not based on OpenGL.      
 """

    @Text """
## Example
        
In the following example we show the minimum steps required for drawing a single triangle.        
    """

    @Media.Image """media/lowlevel-drawing-001.png"""

    @Application
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        @Code
        program {
            @Exclude
            extend(SingleScreenshot()) {
                outputFile = "media/lowlevel-drawing-001.png"
                multisample = BufferMultisample.SampleCount(8)
            }
            val geometry = vertexBuffer(
                    vertexFormat {
                        position(3) // -- this attribute is named "position"
                    }, 3)

            geometry.put {
                for (i in 0 until geometry.vertexCount) {
                    write(Vector3(2.0*Math.random()-1.0,2.0*Math.random()-1.0, 0.0))
                }
            }

            // -- code for the vertex shader
            val vs = """
            #version 330                
            in vec3 a_position;  // -- driver adds a_ prefix (a for attribute)
            void main() {
                gl_Position = vec4(a_position, 1.0);                
            }
            """

            // -- code for the fragment shader
            val fs = """
            #version 330
            out vec4 o_output;
            void main() {
                o_output = vec4(1.0);                                                                                                                
            }                                                                
            """

            val shader = Shader.createFromCode(vs, fs)

            extend {
                shader.begin()
                Driver.instance.drawVertexBuffer(
                        shader, listOf(geometry), DrawPrimitive.TRIANGLES, 0, 3)
                shader.end()
            }
        }
    }


}