# Shade styles #

Shade styles are used to change the drawing behaviour of the `Drawer`, the appearance of
all drawing primitives by applying shade styles.

Shade styles are composed of two types of transforms: vertex transforms and fragment transforms. The two transforms are applied in separate stages of the rendering process.

In the vertex transform it is possible to change the geometry of what is drawn.

In the fragment transform it is possible to change the appearance of the geometry.

For those interested in authoring shade styles it is helpful to have some knowledge of shaders and GLSL.

## Basic usage ##

In essence shade styles are fragments of GLSL code that are inserted into OPENRNDRs templated shaders.

As quick first step we override the output to red in the following snippet.
```kotlin
fun draw() {
    // -- set shade style
    drawer.shadeStyle = shadeStyle {
        fragmentTransform = """
            x_fill.rgb = vec3(1.0, 0.0, 0.0);
        """
    }
    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10, 10, 100, 100)
}
```

The idea of shade styles is to allow more complex changes in the appearance. In the next snippet we create a wavy pattern by using cosines and the screen position.

```kotlin
fun draw() {
    // -- set shade style
    drawer.shadeStyle = shadeStyle {
        fragmentTransform = """
            float c = cos(c_screenPosition.x * 0.1) * 0.5 + 0.5;
            x_fill.rgb = vec3(c, c, c);
        """
    }
    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10, 10, 100, 100)
}
```

Here the fragment transform uses one of the built-in constants `c_screenPosition` as a basis for the wave pattern.

In the next step we introduce animation by adding an external clock signal to the shade style.
```kotlin
fun draw() {
    // -- set shade style
    drawer.shadeStyle = shadeStyle {
        fragmentTransform = """
            float c = cos(c_screenPosition.x * 0.1 + p_time) * 0.5 + 0.5;
            x_fill.rgb = vec3(c, c, c);
        """
        // -- add a time parameter
        parameter("time", seconds)
    }
    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10, 10, 100, 100)
}
```
Here an external clock signal is added by introducing a parameter to the shade style and setting its value to `seconds` (which is a property of `Program`)

## Built-in shade shade styles

OPENRNDR offers a number of built-in shadestyles to facilitate common use cases.

## Prefix overview

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

## Standard uniforms

Standard uniforms are variables that are set by `Drawer`. Standard uniforms are constant and accessible from both the
vertex and fragment transforms.

Uniform name       | GLSL type   | Description
-------------------|-------------|---------------
u_projectionMatrix | `mat44`     | The projection matrix
u_viewMatrix       | `mat44`     | The view matrix
u_normalMatrix     | `mat44`     | Modified view matrix that should be applied to normals
u_fill             | `vec4`      | The fill color
u_stroke           | `vec4`      | The stroke color
u_strokeWeight     | `vec4`      | The stroke weight
u_colorTransform   | `float[25]` | The 5x5 color transform matrix
u_contentScale     | `float`     | The content scale factor of the active render target

## Standard Attributes

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

Constant name       | Scope               | GLSL type | Description
--------------------|---------------------|-----------|------------
`c_screenPosition`  | fragment transform  | vec2      | the position on screen in device coordinates
`c_contourPosition` | fragment transform  | vec3      | the on the contour, only non-zero when drawing line segments and contours

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

### ColorBuffer parameters

Can be used to map images.


### BufferTexture parameters

Can be used to map custom values.

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
