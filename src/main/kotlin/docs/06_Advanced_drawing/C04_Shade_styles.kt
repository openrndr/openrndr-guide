@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Shade styles")
@file:ParentTitle("Advanced drawing")
@file:Order("140")
@file:URL("advancedDrawing/shadeStyles")

package docs.`06_Advanced_drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.MagnifyingFilter
import org.openrndr.draw.MinifyingFilter
import org.openrndr.draw.loadImage
import org.openrndr.draw.shadeStyle
import org.openrndr.shape.Circle
import kotlin.math.cos
import kotlin.math.sin

fun main() {

    @Text 
    """
    # Shade styles
    
    Shade styles are used to change the drawing behaviour of the `Drawer` 
    affecting the appearance of all drawing primitives.
    
    Shade styles are composed of two types of transforms: vertex transforms 
    and fragment transforms. The two transforms are
    applied in separate stages of the rendering process. In the vertex 
    transform it is possible to change the geometry of what is drawn, 
    and in the fragment transform it is possible to change the appearance of that
    geometry. A shade style can affect vertices, fragments or both.
    
    A selection of preset ready-to-use shade styles is provided by 
    [`orx-shade-styles`](10_OPENRNDR_Extras/C09_Shade_style_presets) 
    
    For those interested in authoring shade styles it is helpful to have 
    some basic understanding of shaders and GLSL.

    ## Basic usage
    
    In essence shade styles are fragments of GLSL code that are inserted into 
    OPENRNDRs templated shaders.

    As a quick first step we override the output to red in the following snippet
    """

    @Media.Image "media/shadestyles-001.png"

    @Application
    @ProduceScreenshot("media/shadestyles-001.png")
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
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


    @Text 
    """
    The idea of shade styles is to allow more complex changes in the appearance. 
    In the next snippet we create
    a wavy pattern by using cosines and the screen position.
    """

    @Media.Image "media/shadestyles-002.png"

    @Application
    @ProduceScreenshot("media/shadestyles-002.png")
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
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

    @Text 
    """
    In the next step we introduce animation by adding an external clock signal 
    to the shade style. Shade styles have _parameters_ that can be used for this.
    """

    @Media.Video "media/shadestyles-003.mp4"

    @Application
    @ProduceVideo("media/shadestyles-003.mp4", 10.0, 60)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
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

    @Text 
    """
    ## Usage examples
    
    Here follow some examples of common problems that are solved using 
    shade styles.
    
    ### Mapping images on shapes
    """

    @Media.Video "media/shadestyles-example-001.mp4"

    @Application
    @ProduceVideo("media/shadestyles-example-001.mp4", 10.0, 60)
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
            val image = loadImage("data/images/cheeta.jpg")
            image.filter(MinifyingFilter.LINEAR_MIPMAP_NEAREST, MagnifyingFilter.LINEAR)
            extend {
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = """
                        vec2 texCoord = c_boundsPosition.xy;
                        texCoord.y = 1.0 - texCoord.y;
                        vec2 size = textureSize(p_image, 0);
                        texCoord.x /= size.x/size.y;
                        x_fill = texture(p_image, texCoord);
                    """
                    parameter("image", image)
                }

                val shape = Circle(width / 2.0, height / 2.0, 110.0).shape
                drawer.translate(cos(seconds) * 100.0, sin(seconds) * 100.0)
                drawer.shape(shape)
            }
        }
    }

    @Text 
    """
    ## The shade style language
    
    ### Prefix overview
    
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

    ### Standard uniforms
    
    Listed below is an overview of all the prefixes used in the shade style language.
    
    Uniform name            | GLSL type             | Description
    ------------------------|-----------------------|-----------------------------------------------------------
    `u_modelNormalMatrix`   | mat4                  | matrix used to transform vertex normals from object to world space
    `u_modelMatrix`         | mat4                  | matrix used to transform vertices from object to world space
    `u_viewNormalMatrix`    | mat4                  | matrix used to transform vertex normals from world space to view space
    `u_viewMatrix`          | mat4                  | matrix used to transform vertex normals from world space to view space
    `u_viewMatrix`          | mat4                  | matrix used to transform vertices from world space to view space
    `u_projectionMatrix`    | mat4                  | matrix used to transform vertices from view space to clip space
    `u_contentScale`        | float                 | the active content scale
    `u_viewDimensions`      | vec2                  | the dimensions of the target viewport
    `u_fill`                | vec4                  | the Drawer fill color
    `u_stroke`              | vec4                  | the Drawer stroke color
    `u_strokeWeight`        | float                 | the Drawer strokeWeight
    `u_colorMatrix`         | float[25]             | the Drawer color matrix
    
    ### Standard Attributes

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
    `va_color`     | vec3     | the interpolated color

    ### Transformable values

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

    ### Constants

    Constant name       | Scope               | GLSL type | Description
    --------------------|---------------------|-----------|------------
    `c_element`         | all                 | int       | the element index in batched rendering
    `c_instance`        | all                 | int       | the instance index in instanced rendering
    `c_screenPosition`  | fragment transform  | vec2      | the position on screen in device coordinates
    `c_contourPosition` | fragment transform  | float     | the position on the contour, between 0.0 and contour.length. Only non-zero when drawing line segments and contours
    `c_boundsPosition`  | fragment transform  | vec3      | the bounding box position of the current shape or contour stored in `.xy`
    `c_boundsSize`      | fragment transform  | vec3      | the bounding box size of the current shape or contour stored in `.xy`

    ### Parameters

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
