@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Low-level drawing")
@file:ParentTitle("Advanced topics")
@file:Order("150")
@file:URL("advancedTopics/lowLevelDrawing")

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.DrawPrimitive
import org.openrndr.draw.Shader
import org.openrndr.draw.vertexBuffer
import org.openrndr.draw.vertexFormat
import org.openrndr.internal.Driver
import org.openrndr.math.Vector3

fun main() {
    @Text 
    """
    # Low-level Drawing
    
    This text is intended for developers looking to provide drawing 
    functionality that is fully independent of 
    the `Drawer` class. Developers that desire to do so will essentially 
    use the same building blocks that OPENRNDR
    uses internally.
        
    A good reference for low-level drawing is the `Filter` class and it is 
    encouraged to look at its 
    [source code](https://github.com/openrndr/openrndr/blob/master/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/Filter.kt)
    as it provides a good idea of how all the low-level components interact 
    and its implementation is fully independent from `Drawer`.
        
    All low-level drawing is performed using OPENRNDR's `Driver` class. 
    This class provides a handful of functions for
    drawing. Below you will find a list of the essential ones.
     * `Driver.setState()`    
     * `Driver.drawVertexBuffer()`
     * `Driver.drawIndexedVertexBuffer()`        
     * `Driver.drawInstances()`
     * `Driver.drawIndexedInstances()`
     
    Classes that belong to the low-level drawing API:
     * [`Shader`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/Shader.kt)
     * [`IndexBuffer`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/IndexBuffer.kt)
     * [`VertexBuffer`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/VertexBuffer.kt)
     * [`VertexFormat`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/VertexFormat.kt)
     * [`ColorBuffer`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/ColorBuffer.kt)
     * [`DepthBuffer`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/DepthBuffer.kt)
     * [`ArrayTexture`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/ArrayTexture.kt)
     * [`CubeMap`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/Cubemap.kt)
     * [`DrawStyle`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/DrawStyle.kt)
     * [`RenderTarget`](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-draw/src/commonMain/kotlin/org/openrndr/draw/RenderTarget.kt)
    
    We discourage writing code that uses OpenGL directly; even though currently 
    only an OpenGL 3.3 implementation exists 
    for `Driver`, in the future we may add implementations that 
    are not based on OpenGL.      
    """

    @Text 
    """
    ## Example
        
    In the following example we show the minimum steps required for drawing 
    a single triangle.        
    """

    @Media.Image "../media/lowlevel-drawing-001.jpg"

    @Application
    @ProduceScreenshot("media/lowlevel-drawing-001.jpg", 8)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
            val geometry = vertexBuffer(
                vertexFormat {
                    position(3) // -- this attribute is named "position"
                }, 3
            )

            geometry.put {
                for (i in 0 until geometry.vertexCount) {
                    write(
                        Vector3(
                            2.0 * Math.random() - 1.0,
                            2.0 * Math.random() - 1.0,
                            0.0
                        )
                    )
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

            val shader = Shader.createFromCode(
                vsCode = vs,
                fsCode = fs,
                name = "custom-shader"
            )

            extend {
                shader.begin()
                Driver.instance.drawVertexBuffer(
                    shader, listOf(geometry), DrawPrimitive.TRIANGLES, 0, 3
                )
                shader.end()
            }
        }
    }
}
