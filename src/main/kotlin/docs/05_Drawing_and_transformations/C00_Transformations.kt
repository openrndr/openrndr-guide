package docs.`05_Drawing_and_transformations`

import org.openrndr.dokgen.annotations.Text

fun main(args: Array<String>) {

    @Text """# Transformations

This section covers the topic of placing items on the screen.

## Transform pipeline

OPENRNDR's `Drawer` is build around model-view-projection transform pipeline. That means that three different transformations are applied to determine
the screen position.

matrix property | pipeline stage
----------------|---------------------
`model`         | model transform
`view`          | view transform
`projection`    | projection transform

Which matrices are affected by which `Drawer` operations?

operation            | matrix property
---------------------|----------------
`fun rotate(…)`      | `model`
`fun translate(…)`   | `model`
`fun scale(…)`       | `model`
`fun rotate(…)`      | `model`
`fun lookAt(…)`      | `view`
`fun ortho(…)`       | `projection`
`fun perspective(…)` | `projection`


## Projection matrix

The default projection transformation is set to an orthographic projection using `ortho()`. The origin is in the upper-left corner; positive y points down, positive x points right on the screen.

### Perspective projections

```kotlin
override fun draw() {
    drawer.perspective(90.0, width*1.0 / height, 0.1, 100.0)
}
```

## Transforms

In OPENRNDR transforms are represented by `Matrix44` instances.

OPENRNDR offers tools to construct `Matrix44`

### Transform builder

Relevant APIs
```
Matrix44
transform {}
```

In the snippet below a `Matrix44` instance is constructed using the `transform {}` builder. Note that the application order is from bottom to top.

```kotlin
drawer.view *= transform {
    rotate(32.0)
    rotate(Vector3(1.0, 1.0, 0.0).normalized, 43.0)
    translate(4.0, 2.0)
    scale(2.0)
}
```

This is equivalent to the following:
```kotlin
drawer.rotate(32.0)
drawer.rotate(Vector3(1.0, 1.0, 0.0).normalized, 43.0)
drawer.translate(4.0, 2.0)
drawer.scale(2.0)
```

## Applying transforms to vectors ##

```kotlin
    val x = Vector3(1.0, 2.0, 3.0)
    val m = transform {
        rotate(42.0)
    }
    val transformed = m * x
    val transformedTwice = m * m * x
```

    """.trimIndent()

}