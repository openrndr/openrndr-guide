# Custom rendering

OPENRNDR is designed with the idea that users should be able to draw beyond the primitives offered by `Drawer`.

A simple interface is provided with which a very wide spectrum of graphics can be drawn.

## Vertex buffers

A vertex buffer is a (on the GPU residing) amount of memory in which vertices that describe geometry are stored. A single vertex consists of a number of customizable attributes such as position, normal and color. In OPENRNDR the attributes of a vertex are described using a `VertexFormat`.

Vertex buffers allow geometry to be prepared and stored in such a way that graphics hardware can draw it directly. 

## Defining a vertex format

A very simple vertex format that just contains a position attribute is defined as follows using the `vertexFormat {}` builder.

```kotlin
val vf = vertexFormat {
    position(3)
}
```

##### vertexFormat {} attributes

Listed below are the attributes that can be added to the vertex format.

name                                                                  | type      | description
----------------------------------------------------------------------|-----------|-------------------------------
`position(dimensions: Int)`                                           | `FLOAT32  | VECTOR2_FLOAT32 | VECTOR3_FLOAT32 | VECTOR4_FLOAT32 ` | position attribute
`normal(dimensions: Int)`                                             | `FLOAT32  | VECTOR2_FLOAT32 | VECTOR3_FLOAT32 | VECTOR4_FLOAT32 ` | normal attribute
`textureCoordinate(dimensions: Int)`                                  | `FLOAT32  | VECTOR2_FLOAT32 | VECTOR3_FLOAT32 | VECTOR4_FLOAT32 ` | texture coordinate attribute
`color(dimensions: Int)`                                              | `FLOAT32  | VECTOR2_FLOAT32 | VECTOR3_FLOAT32 | VECTOR4_FLOAT32 ` | color attribute
`attribute(name: String, type: VertexElementType, arraySize:Int = 1)` | `type`    | custom attribute

A more complex vertex format would then look like this:
```kotlin 
val vf = vertexFormat {
    position(3)
    normal(3)
    color(4)
    attribute("objectID", FLOAT32)
}
```

## Creating a vertex buffer

##### Relevant APIs
```kotlin
vertexBuffer(vertexFormat: VertexFormat, vertexCount: Int): VertexBuffer
```

The `vertexBuffer()` function is used to create a `VertexBuffer` instance. For example to create a vertex buffer with our previously defined vertex format `vf` we use the following:

```kotlin
val geometry = vertexBuffer(vf, 1000)
```

Often the creation of the vertex buffer and the definition of the vertex format are combined in a single expression:

```kotlin
val geometry = vertexBuffer(vertexFormat {
        position(3)
        normal(3)
        color(4)
        attribute("objectID", FLOAT32)
    }, 1000)
```

## Placing data in the vertex buffer

##### Relevant APIs
```kotlin
fun VertexBuffer.put {}
fun VertexBuffer.write(data: ByteBuffer, offset:Int = 0)

val VertexBuffer.shadow: VertexBufferShadow
val VertexBufferShadow.writer
fun VertexBufferShadow.upload()
```

Now that a vertex format has been defined and a vertex buffer has been created we can place data in the vertex buffer.
The data placed in the vertex buffer must closely match the vertex format; any form of mismatch will lead to surprising or undefined behaviour.

The `VertexBuffer.put {}` builder is the easiest and safest way of placing data in the vertex buffer.

In the following example we create a very simple vertex format that holds just a position attribute. After creation we fill the vertex buffer with random data.
```kotlin
val geometry = vertexBuffer(vertexFormat {
        position(3)
    }, 1000)

geometry.put {
    for (i in 0 until 1000) {
        write(Vector3(Math.random()-0.5, Math.random()-0.5, Math.random()-0.5))
    }
}
```

## Drawing using VertexBuffer

##### Relevant APIs:
```kotlin
fun Drawer.vertexBuffer(vertexBuffer: VertexBuffer, primitive: DrawPrimitive, vertexOffset: Int = 0, vertexCount: Int = vertexBuffer.vertexCount)
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

