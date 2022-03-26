@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Managing draw style")
@file:ParentTitle("Drawing basics")
@file:Order("140")
@file:URL("drawingBasics/managingDrawStyle")

package docs.`04_Drawing_basics`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.isolated

fun main() {
    @Text """

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
`frontStencil`       | `StencilStyle`   |                    | The stencil style for front-facing fragments
`backStencil`        | `StencilStyle`   |                    | The stencil style for back-facing fragments
`clip`               | `Rectangle?`     | `null`             | A rectangle that describes where drawing will take place

## The active draw style

```kotlin
val active = drawer.drawStyle.copy()
```

## The draw style stack

Styles can be pushed on and popped from a stack maintained by `Drawer`.

"""

    application {
        program {
            @Code
            extend {
                drawer.pushStyle()
                drawer.fill = ColorRGBa.PINK
                drawer.rectangle(100.0, 100.0, 100.0, 100.0)
                drawer.popStyle()
            }
        }
    }

    @Text """
The `Drawer` provides a helper function called `isolated {}` that pushes style and transforms on a their respective
stacks, executes the user code and pops style and transforms back.
"""

    application {
        program {
            @Code
            extend {
                drawer.isolated {
                    fill = ColorRGBa.PINK
                    rectangle(100.0, 100.0, 100.0, 100.0)
                }
            }
        }
    }
}
