# Managing draw style

In the previous section we briefly talked about controlling the appearance of drawing primitives, in this section we draw styles and tools to
manage the draw style.

#### Full overview of properties of DrawStyle

Property             | Type             | Default            | Description
---------------------|------------------|--------------------|-------------------
`fill`               | `ColorRGBa?`     | `ColorRGBa.BLACK`  | The fill color
`stroke`             | `ColorRGBa?`     | `ColorRGBa.BLACK`  | The stroke color
`strokeWeight`       | `Double`         | `1.0`              | The stroke weight
`lineCap`            | `LineCap`        |                    |
`lineJoin`           | `LineJoin`       |                    |
`fontMap`            | `FontMap?`       | `null`             | The font to use
`colorMatrix`        | `Matrix55`       | `IDENTITY`         | The color matrix (used for images)
`channelWriteMask`   | `ChannelMask`    | `ALL`              | The channel write mask
`shadeStyle`         | `ShadeStyle?`    | `null`             | The shade style
`blendMode`          | `BlendMode`      | `OVER`             | The blend mode
`quality`            | `DrawQuality`    | `QUALITY`          | A hint that controls the quality of some primitives
`depthTestPass`      | `DepthTestPass`  | `ALWAYS`           | When a fragment should pass the depth test
`depthWrite`         | `Boolean`        | `false`            | Should the fragment depth be written to the depth buffer?
`stencil`            | `StencilStyle`   |                    | The stencil style
`frontStencil`       | `StencilStyle`   |                    | The stencil style for front facing fragments
`backStencil`        | `StencilStyle`   |                    | The stencil style for back facing fragments


## The active draw style

```kotlin
val active = drawer.drawStyle.copy()
```

## The draw style stack

Styles can be pushed on and popped from a stack maintained by `Drawer`.

```kotlin
drawer.pushStyle()
drawer.fill = ColorRGBa.RED
drawer.rect(100.0, 100.0, 100.0, 100.0)
drawer.popStyle()
// -- style is now like it was when pushed
```

The `Drawer` provides a helper function called `isolated {}` that pushes style (and [transforms](Topic_Transforms)) with which style
can be managed concisely.

```kotlin
drawer.isolated {
    fill = ColorRGBa.RED
    rect(100.0, 100.0, 100.0, 100.0)
}
```