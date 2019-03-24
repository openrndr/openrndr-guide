# Render targets and color buffers # 

OPENRNDR extensively supports drawing in off-screen buffers.

A `RenderTarget` models a place to draw to. A `RenderTarget` has two kind of buffer attachments
`ColorBuffer` attachments and `DepthBuffer` attachements. At least a single `ColorBuffer` attachment is needed to draw on be able to draw on a `RenderTarget`.

A `ColorBuffer` is a buffer that can hold up to 4 channel color. A `ColorBuffer` can hold 8 bit integer, 16 bit float or 32 bit float channels.

A `DepthBuffer` is a buffer that can hold depth and stencil values.

## Creating a render target

The adviced method of creating `RenderTarget` instances is to use the `renderTarget {}` builder

```kotlin
    val rt = renderTarget(640, 480) { }
```

This creates a render target, but the render target does not have attachments that can hold the actual image data.
In the following snippet a render target with a single color buffer attachment is created using the builder.

```kotlin
    val rt = renderTarget(640, 480) {
        colorBuffer()
    }
```

One can also create render targets without using the builder as follows:

```kotlin
    val cb = ColorBuffer.create(640, 480)
    val rt = RenderTarget.create(640, 480)
    rt.attach(cb)
```

## Drawing on a render target

In the following code snippet you will find an example showing how to draw on an off-screen buffer followed by drawing that offscreen buffer on screen.

```kotlin
lateinit var rt : RenderTarget

fun setup() {
    // -- build a render target with a single color buffer attachment
    rt = renderTarget(width, height) {
        colorBuffer()
    }
}

fun draw() {
    // -- clear the program target
    drawer.background(ColorRGBa.BLACK)

    // -- bind our render target, clear it, draw on it, unbind it
    rt.bind()
    drawer.background(ColorRGBa.PINK)
    drawer.fill = Color.WHITE
    drawer.stroke = null
    drawer.rectangle(40.0, 40.0, 80.0, 80.0)
    rt.unbind()
 
    // draw the backing color buffer to the screen
    drawer.image(rt.colorBuffer(0))
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/render-targets-001/src/main/kotlin/Example.kt)

## Render targets and projection transforms

Keep in mind that projection transform has to be set to fit the render target, this becomes apparent specifically when the used render target
has dimensions that differ from those of the window. In case of orthographic (2D) projections one can use the following:

```kotlin
lateinit var RenderTarget rt;

fun setup() {
    // -- build a render target with a single color buffer attachment
    rt = renderTarget(400, 400) {
        colorBuffer()
    }
}

fun draw() {
    // -- clear the program target
    drawer.background(ColorRGBa.BLACK)

    drawer.isolatedWithTarget(rt) { // -- pushes matrices and styles
        // -- bind our render target, clear it, draw on it, unbind it
        background(ColorRGBa.PINK)
        // -- set the orthographic transform that matches with the render target
        ortho(rt)
        fill = Color.WHITE
        stroke = null
        drawer.rectangle(40.0, 40.0, 80.0, 80.0)
    }
    // -- matrices and styles are popped back here

    // draw the backing color buffer to the screen
    drawer.image(rt.colorBuffer(0))
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/render-targets-001/src/main/kotlin/Example.kt)
## Compositing using render targets and alpha channels

OPENRNDR allows for compositing using `RenderTargets` through the use of transparency encoded in alpha channels.

The following code snippet uses two `RenderTarget` instances and clears them using `ColorRGBa.TRANSPARENT`.

```kotlin
lateinit var RenderTarget rt0;
lateinit var RenderTarget rt1;

fun setup() {
    // -- build a render target with a single color buffer attachment
    rt0 = RenderTarget.build(width, height) { colorBuffer() }
    rt1 = RenderTarget.build(width, height) { colorBuffer() }
}

fun draw() {
    // -- clear the program target
    drawer.background(ColorRGBa.BLACK)

    drawer.stroke = null

    // -- bind our first render target, clear it, draw on it, unbind it
    rt0.bind()
    drawer.background(ColorRGBa.TRANSPARENT)
    drawer.fill = Color.WHITE
    drawer.rectangle(40.0, 40.0, 80.0, 80.0)
    rt0.unbind()

    // -- bind our second render target, clear it, draw on it, unbind it
    rt1.bind()
    drawer.background(ColorRGBa.TRANSPARENT)
    drawer.fill = Color.RED
    drawer.rectangle(140.0, 140.0, 80.0, 80.0)
    rt1.unbind()

    // draw the backing color buffer to the screen
    drawer.image(rt0.colorBuffer(0))
    drawer.image(rt1.colorBuffer(0))
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/render-targets-002/src/main/kotlin/Example.kt)

## Creating high precision floating point render targets

The default color buffer format is unsigned 8 bit RGBa. There is support for floating point render targets.

```kotlin
val rt = renderTarget(640.0, 480.0) {
    colorBuffer(ColorFormat.RGBa, ColorType.FLOAT16)
    colorBuffer(ColorFormat.RGBa, ColorType.FLOAT32)
}
```

## Named attachements

```kotlin
val rt = renderTarget(640.0, 480.0) {
    colorBuffer("albedo", ColorFormat.RGBa, ColorType.FLOAT16)
    colorBuffer("position", ColorFormat.RGBa, ColorType.FLOAT32)
}
```

