# Shade styles #

Shade styles are used to change the drawing behaviour of the drawer.

## Basic usage ##

In essence shade styles are fragments of GLSL code that are inserted into OPENRNDRs templated shaders.

```kotlin

fun draw() {
    // -- set shade style
    drawer.shadeStyle = shadeStyle {

        fragmentTransform = """
            x_fill.rgb = vec3(1.0, 0.0, 0.0);
        """
    }

    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10, 10, 100, 100);
}
```

## Standard uniforms

Uniform name     | GLSL type   | Description
-----------------|-------------|---------------
projectionMatrix | `mat44`     | The projection matrix
viewMatrix       | `mat44`     | The view matrix
normalMatrix     | `mat44`     | Modified view matrix that should be applied to normals
fill             | `vec4`      | The fill color
stroke           | `vec4`      | The stroke color
colorTransform   | `float[25]` | The 5x5 color transform matrix

## Standard Attributes

Attribute name | GLSL type | Description
---------------|-----------|------------
`a_position`   | vec3      | the position
`a_normal`     | vec3      | the normal

## Transformable values

These are values that can be transformed using shade styles.

### Vertex transform

Variable name        | GLSL type | Description
---------------------|-----------|------------
`x_position`         | `vec3`    | vertex position, initialized with value `a_position`
`x_normal`           | `vec3`    | vertex normal, intialized with value `a_normal`
`x_viewMatrix`       | `mat4`    | view matrix
`x_normalMatrix`     | `mat4`    | normal matrix, initialized with `normalMatrix`
`x_projectionMatrix` | `mat4`    | pjojection matrix, initialized with `projectionMatrix`

### Fragment transform

Variable name | GLSL type | Description
--------------|-----------|------------
`x_fill`      | `vec4`    | The fill color written to the fragment
`x_stroke`    | `vec4`    | The stroke color written to the fragment

## Constants

Constant name      | Scope               | GLSL type | Description
-------------------|---------------------|-----------|------------
`c_boundsPosition` | fragment transform  | vec3      |
`c_boundsMin`      | fragment transform  | vec3      |
`c_boundsMax`      | fragment transform  | vec3      |

## Parameters ##

Parameters can be used to supply external data to transforms. Parameters are translated to shader uniforms and are exposed
by uniforms with the `p_` prefix.

```kotlin

fun draw() {
    // -- set shade style
    drawer.shadeStyle = shadeStyle {
        fragmentTransform = """
            x_fill = p_userFill;
        """
    }

    // -- get active shade style and set parameter value
    drawer.shadeStyle?.parameter("userFill", Vector4(1, 0, 0, 1));

    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10.0, 10.0, 100.0, 100.0);
}

```

### Supported parameter types:

 JVM type        | GLSL type
-----------------|-------------
 `float`         | `float`
 `Vector2`       | `vec2`
 `Vector3`       | `vec3`
 `Vector4`       | `vec4`
 `ColorRGBa`     | `vec4`
 `Matrix44`      | `mat4`
 `ColorBuffer`   | `sampler2D`
 `BufferTexture` | `samplerBuffer`

## Prefix overview

Listed below is an overview of all the prefixes used in the shade style language.

prefix   | Scope               | Description
---------|--------------------|-----------------
`a_`     | vertex transform   | vertex attribute
`va_`    | fragment transform | varying attribute, interpolation passed from vertex to fragment shader
`i_`     | vertex transform   | instance attribute
`vi_`    | fragment transform | varying instance attribute
`x_`     | all                | transformable value
`p_`     | all                | user provided value
`o_`     | fragment transform | output value

## Outputs

Shade styles allow the output to multiple buffers

```kotlin

lateinit var rt: RenderTarget
fun setup() {
    rt = renderTarget(width, height) {
        colorBuffer("color")
        colorBuffer("extra")
    }
}

fun draw() {
    drawer.isolatedWithRenderTarget(rt) {
        shadeStyle = shadeStyle {
            fragmentTransform = """
                x_fill = vec4(1.0, 1.0, 1.0, 1.0);
                o_extra = vec4(1.0, 0.0, 0.0, 1.0);
            """
            // -- set the outputs
            output("color", 0)
            output("extra", 1)
        }
        rectangle(0.0, 0.0, 100.0, 100.0)
    }
    drawer.colorBuffer(rt.colorBuffer("color"), 0.0, 0.0, width/2.0, height/2.0)
    drawer.colorBuffer(rt.colorBuffer("extra"), width/2.0, height/2.0, width/2.0, height/2.0)
}

```