# Custom rendering

OPENRNDR is designed with the idea that users should be able to draw beyond the primitives offered by `Drawer`.

A simple interface is provided with which a very wide spectrum of graphics can be drawn.

## Vertex buffers

A vertex buffer is a (on the GPU residing) amount of memory in which vertices that describe geometry are stored. A single vertex consists of a number of customizable attributes such as position, normal and color. In OPENRNDR the attributes of a vertex are described using a `VertexFormat`.

## Defining a vertex format

A very simple vertex format that just contains a position attribute is defined as follows using the `vertexFormat {}` builder.

```kotlin
val vf = vertexFormat {
    position(3)
}
```

##### vertexFormat {} attributes

name                                                               | type      | description
-------------------------------------------------------------------|-----------|-------------------------------
`position(dimensions: Int)`                                        | `FLOAT32` | position attribute 
`normal(dimensions: Int)`                                          | `FLOAT32` | normal attribute
`textureCoordinate(dimensions: Int)`                               | `FLOAT32` | texture coordinate attribute
`color(dimensions: Int)`                                           | `FLOAT32` | color attribute
`attribute(name: String, dimensions:Int, type: VertexElementTYpe)` | `type`    | custom attribute

A more complex vertex format would then look like this:
```kotlin 
val vf = vertexFormat {
    position(3)
    normal(3)
    color(4)
    attribute("objectID", 1, FLOAT32)
}
```

## Drawing using VertexBuffer

Relevant APIs:
```kotlin
Drawer.vertexBuffer()
Drawer.shadeStyle
vertexFormat {}
vertexBuffer()
```

The following snippet shows how to create, populate and draw a `VertexBuffer`. [full source](http://github.com/openrndr/openrndr-examples/custom-rendering-001/src/main/kotlin/main.kt)

```kotlin

lateinit var geometry: VertexBuffer
override fun setup() {
    // -- create the vertex buffer
    geometry = vertexBuffer(
        vertexFormat {
            position(3)
        }, 6)

    // -- fill the vertex buffer with vertices
    geometry.put {
        // the geometry
    }
}

override fun draw() {
    drawer.background(ColorRGBa.BLACK);
    drawer.fill = ColorRGBa.WHITE
    drawer.vertexBuffer(geometry, Primitive.TRIANGLES)
}
```

### Shading geometry

Drawing using `Drawer.vertexBuffer` will be with respect to the set shade style. Using shade styles the appearance of the geometry in the vertex buffer can be fully customized.

The last snippet can be modified to include a simple shading over the geometry. [full source](http://github.com/openrndr/openrndr-examples/custom-rendering-002/src/main/kotlin/main.kt)

```kotlin
override fun draw() {
    drawer.background(ColorRGBa.BLACK);
    drawer.fill = ColorRGBa.WHITE

    drawer.shadeStyle = shadeStyle {
        fragmentTransform = """
            x_fill.rgb = vec3(cos(view.position.x)*0.5+0.5, 0.0, 0.0);
        """
    }
    drawer.vertexBuffer(geometry, Primitive.TRIANGLES)
}
```

## Drawing instances

The same geometry can be drawn more than once in a single draw call.

Relevant APIs

```kotlin
Drawer.vertexBufferInstances()
```

Per instance attributes can be stored in a vertex buffer. For example a transform per instance be realized by creating a vertex buffer that contains
matrix attributes. [full source](http://github.com/openrndr/openrndr-examples/custom-rendering-003/src/main/kotlin/main.kt)

```kotlin
lateinit var geometry: VertexBuffer
lateinit var transforms: VertexBuffer
override fun setup() {
    // -- create the vertex buffer
    geometry = vertexBuffer(
        vertexFormat {
            position(3)
        }, 6)

    // -- fill the vertex buffer with vertices
    geometry.put {
        // the geometry
    }

    // -- create the transform buffer
    transforms = vertexBuffer(
        vertexFormat {
            attribute("transform", 16)
        }, 1000)

    // -- fill the transform buffer
    transforms.put {
        for (i in 0 until 1000) {
            write( transform {
                translate(Math.random() * 100, Math.random() * 100)
                rotate(Math.random() * 360.0)
            } )
        }
    }
}

override fun draw() {
    drawer.background(ColorRGBa.BLACK);
    drawer.fill = ColorRGBa.WHITE
    drawer.shadeStyle = shadeStyle {
        vertexTransform = """
            x_viewTransform = x_viewTransform * i_transform;
            """
        }
    drawer.vertexBufferInstances(listOf(geometry), listOf(transforms), Primitive.TRIANGLES, 1000)
}
```

## Low-level drawing

One can bypass using `Drawer` entirely and use `Driver.instance.drawVertexBuffer`. This is completely safe and intentional but likely more involved to set up.

The `Driver` interface is what is used by the internals of `Drawer`
