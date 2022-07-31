@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Tridimensional graphics")
@file:ParentTitle("Drawing basics")
@file:Order("170")
@file:URL("drawingBasics/tridimensionalGraphics")

package docs.`04_Drawing_basics`

import org.openrndr.WindowMultisample
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.DepthTestPass
import org.openrndr.draw.DrawPrimitive
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.boxMesh
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.*

fun main() {
    @Text """
    # 3D graphics
    """

    @Code
    application {
        configure {
            multisample = WindowMultisample.SampleCount(4)
        }
        program {
            val cube = boxMesh(140.0, 70.0, 10.0)

            extend {
                drawer.perspective(60.0, width * 1.0 / height, 0.01, 1000.0)
                drawer.fill = ColorRGBa.PINK
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = """
                        vec3 lightDir = normalize(vec3(0.3, 1.0, 0.5));
                        float l = dot(va_normal, lightDir) * 0.4 + 0.5;
                        x_fill.rgb *= l; 
                    """.trimIndent()
                }
                drawer.depthWrite = true
                drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL
                drawer.translate(0.0, 0.0, -150.0)
                drawer.rotate(Vector3.UNIT_X, seconds * 15 + 30)
                drawer.rotate(Vector3.UNIT_Y, seconds * 5 + 60)
                drawer.vertexBuffer(cube, DrawPrimitive.TRIANGLES)
            }
        }
    }

    @Text 
    """
    Describe: orx-camera
    Describe: orx-dnk3
    Describe: orx-mesh-generators
    """

    @Code
    application {
        configure {
            multisample = WindowMultisample.SampleCount(4)
        }
        program {
            val cube = boxMesh(140.0, 70.0, 10.0)
            val cam = Orbital()
            cam.eye = -Vector3.UNIT_Z * 150.0

            extend(cam)
            extend {
                drawer.fill = ColorRGBa.PINK
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = """
                        vec3 lightDir = normalize(vec3(0.3, 1.0, 0.5));
                        float l = dot(va_normal, lightDir) * 0.4 + 0.5;
                        x_fill.rgb *= l; 
                    """.trimIndent()
                }
                drawer.vertexBuffer(cube, DrawPrimitive.TRIANGLES)
            }
        }
    }

    @Code
    application {
        configure {
            multisample = WindowMultisample.SampleCount(4)
        }
        program {
            val cam = Orbital()
            cam.eye = Vector3.UNIT_Z * 150.0
            cam.camera.depthTest = false

            extend(cam)
            extend {
                drawer.fill = null
                drawer.stroke = ColorRGBa.PINK
                repeat(10) {
                    drawer.rectangle(Rectangle.fromCenter(Vector2.ZERO, 150.0))
                    drawer.translate(Vector3.UNIT_Z * 10.0)
                }
            }
        }
    }
}