package docs.`06_Advanced_drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.shadeStyle
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.ffmpeg.ScreenRecorder

fun main(args: Array<String>) {

    @Text """# Shade styles"""
    @Text """
Shade styles are used to change the drawing behaviour of the `Drawer`, the appearance of all drawing primitives by
applying shade styles.

Shade styles are composed of two types of transforms: vertex transforms and fragment transforms. The two transforms are
applied in separate stages of the rendering process.

In the vertex transform it is possible to change the geometry of what is drawn.

In the fragment transform it is possible to change the appearance of the geometry.

For those interested in authoring shade styles it is helpful to have some based understanding of shaders and GLSL."""

    @Text """## Basic usage"""
    @Text """In essence shade styles are fragments of GLSL code that are inserted into OPENRNDRs templated shaders.

As a quick first step we override the output to red in the following snippet"""

    @Media.Image """media/shadestyles-001.png"""
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
                outputFile = "media/shadestyles-001.png"
            }
            extend {
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = "x_fill.rgb = vec3(1.0, 0.0, 0.0);"
                }
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.rectangle(width / 2.0 - 200.0, height / 2.0 - 200.0, 400.0, 400.00)
            }
        }
    }


    @Text """The idea of shade styles is to allow more complex changes in the appearance. In the next snippet we create
a wavy pattern by using cosines and the screen position."""

    @Media.Image """media/shadestyles-002.png"""
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
                outputFile = "media/shadestyles-002.png"
            }
            extend {
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = """
                    | float c = cos(c_screenPosition.x * 0.1) * 0.5 + 0.5;
                    | x_fill.rgb *= vec3(c, c, c);
                    """.trimMargin()
                }
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.rectangle(width / 2.0 - 200.0, height / 2.0 - 200.0, 400.0, 400.00)
            }
        }
    }

    @Text """In the next step we introduce animation by adding an external clock signal to the shade style. Shade styles
have _parameters_ that can be used for this.
    """.trimMargin()

    @Media.Video """media/shadestyles-003.mp4"""
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
            extend(ScreenRecorder()) {
                quitAfterMaximum = true
                maximumDuration = 10.0
                outputFile = "media/shadestyles-003.mp4"
                frameRate = 60
            }
            extend {
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = """
                    | float c = cos(c_screenPosition.x * 0.1 + p_time) * 0.5 + 0.5;
                    | x_fill.rgb *= vec3(c, c, c);
                    """.trimMargin()
                    parameter("time", seconds)
                }
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.rectangle(width / 2.0 - 200.0, height / 2.0 - 200.0, 400.0, 400.0)
            }
        }
    }


    @Text """## The shade style language"""
    @Text """### Prefix overview
Listed below is an overview of all the prefixes used in the shade style language.

prefix   | Scope              | Description
---------|--------------------|-----------------
`u_`     | all                | system uniforms passed in from Drawer
`a_`     | vertex transform   | vertex attribute
`va_`    | fragment transform | varying attribute, interpolation passed from vertex to fragment shader
`i_`     | vertex transform   | instance attribute
`vi_`    | fragment transform | varying instance attribute
`x_`     | all                | transformable value
`p_`     | all                | user provided value
`o_`     | fragment transform | output value (always `vec4`)
`d_`     | all                | shader definitions
"""

    @Text """### Standard uniforms
Listed below is an overview of all the prefixes used in the shade style language.

prefix   | Scope              | Description
---------|--------------------|-----------------
`u_`     | all                | system uniforms passed in from Drawer
`a_`     | vertex transform   | vertex attribute
`va_`    | fragment transform | varying attribute, interpolation passed from vertex to fragment shader
`i_`     | vertex transform   | instance attribute
`vi_`    | fragment transform | varying instance attribute
`x_`     | all                | transformable value
`p_`     | all                | user provided value
`o_`     | fragment transform | output value (always `vec4`)
`d_`     | all                | shader definitions
"""

    @Text """### Standard Attributes

Attributes are only directly accessible in the vertex transform. However interpolated forms of the
the attributes are passed to the fragment transform.

Attribute name | GLSL type | Description
---------------|-----------|------------
`a_position`   | vec3      | the position
`a_normal`     | vec3      | the normal

In this table we see the interpolated versions that are accessible in the fragment transform only.

Attribute name | GLSL type | Description
---------------|-----------|------------
`va_position`  | vec3     | the interpolated position
`va_normal`    | vec3     | the interpolated normal
"""
    @Text """### Transformable values

These are values that can be transformed using shade styles.

#### Vertex transform

Variable name        | GLSL type | Description
---------------------|-----------|------------
`x_position`         | `vec3`    | vertex position, initialized with value `a_position`
`x_normal`           | `vec3`    | vertex normal, initialized with value `a_normal`
`x_viewMatrix`       | `mat4`    | view matrix
`x_normalMatrix`     | `mat4`    | normal matrix, initialized with `normalMatrix`
`x_projectionMatrix` | `mat4`    | projection matrix, initialized with `projectionMatrix`

#### Fragment transform

Variable name | GLSL type | Description
--------------|-----------|------------
`x_fill`      | `vec4`    | The fill color written to the fragment
`x_stroke`    | `vec4`    | The stroke color written to the fragment
"""

    @Text """### Constants

Constant name       | Scope               | GLSL type | Description
--------------------|---------------------|-----------|------------
`c_element`         | all                 | int       | the element index in batched rendering
`c_instance`        | all                 | int       | the instance index in instanced rendering
`c_screenPosition`  | fragment transform  | vec2      | the position on screen in device coordinates
`c_contourPosition` | fragment transform  | vec3      | the on the contour, only non-zero when drawing line segments and contours

"""

    @Text """### Parameters

Parameters can be used to supply external data to transforms. Parameters are translated to shader uniforms and are exposed
by uniforms with the `p_` prefix.

#### ColorBuffer parameters

Can be used to map images.

#### BufferTexture parameters

Can be used to map custom values.

#### Supported parameter types:

 JVM type        | GLSL type
-----------------|-------------
 `float`         | `float`
 `Vector2`       | `vec2`
 `Vector3`       | `vec3`
 `Vector4`       | `vec4`
 `ColorRGBa`     | `vec4`
 `Matrix44`      | `mat4`
 `DepthBuffer`   | `sampler2D`
 `ColorBuffer`   | `sampler2D`
 `BufferTexture` | `samplerBuffer`
"""

}