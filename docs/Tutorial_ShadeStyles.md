# Shade styles #

Shade styles are used to change the drawing behaviour of the drawer.

Shade styles are comprised of transforms that change aspects of the drawing behaviour. 

* position transform
* fill transform
* stroke transform
* tint transform

What can be achieved through shade styles? A lot. Applying gradients, texture mapping, filtering.

## Basic usage ##


```java

void draw() {
    // -- set shade style
    drawer.shadeStyle(new ShadeStyle().fill("x_fill = vec4(1,0,0,1);"));

    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10, 10, 100, 100);
}

```

## Fill transform ##

The fill transform adjusts the fill color. The outcome of the transform must be assigned to the `x_fill` variable.

```java

// -- force fill to be red
shadeStyle.fill("x_fill = vec4(1, 0, 0, 1);");

```

## Stroke transform ##

```java

// -- force stroke to be red
shadeStyle.fill("x_stroke = vec4(1, 0, 0, 1);");

```

## Tint transform ##

The tint transform is used in image based drawing. The result must be written to `x_tint`

```java

// -- force tint to be red
shadeStyle.tint("x_tint = vec4(1, 0, 0, 1);");

```

## Position transform ##

The position transform is used to modify the vertex positions.

## Constants and variables ##

Shade styles expose the following constants and variables.

| variable name    | GLSL type | access | scope | description |
|------------------|-----------|--------|-------|-------------|
| `x_tint`         | `vec4`    | write  | tint  | output for the tint transform |
| `x_fill`         | `vec4`    | write  | fill  | output for the fill transform |
| `x_stroke`       | `vec4`    | write  | stroke  | output for the stroke transform |
| `fill`           | `vec4`    | read   | all   | the original fill color |
| `stroke`         | `vec4`    | read   | all   | the original stroke color |
| `screenPosition` | `vec2`    | read   | ^position | the position in screen space |
| `bounds`         | `vec4`    | read   | all | llll |
| `contourLength`  | `float` | read | stroke | the length of the contour being drawn (where applicable) |
| `contourOffset`  | `float` | read | stroke | the length of the contour being drawn (where applicable) |
| `contourFraction` | `float` | read | stroke | |

## Parameters ##

Parameters can be used to supply external data to transforms.

```java

void draw() {
    // -- set shade style
    drawer.shadeStyle(new ShadeStyle().
                          fill("x_fill = p_userFill");
                     );

    // -- get active shade style and set parameter value
    drawer.shadeStyle().parameter("userFill", new Vector4(1, 0, 0, 1));

    // -- draw rectangle with the shade style applied to it
    drawer.rectangle(10, 10, 100, 100);
}

```

Supported parameter types:

| Java type | GLSL type |
|-----------|-----------|
| `float`   | `float`   |
| `Vector2` | `vec2`    |
| `Vector3` | `vec3`    |
| `Vector4` | `vec4`    |
| `Matrix44`| `mat4`    |
| `ColorBuffer` | `sampler2D` |
| `BufferTexture` | `samplerBuffer` |