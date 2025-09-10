@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Managing draw style")
@file:ParentTitle("Drawing")
@file:Order("140")
@file:URL("drawing/managingDrawStyle")

package docs.`30_Drawing`

import org.intellij.lang.annotations.Language
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.isolated

fun main() {
    @Text
    """
    # Managing draw style
    
    In previous sections we briefly talked about controlling the appearance of drawing primitives using
    `fill`, `stroke` and `lineCap`. In this section we present the existing draw style properties.
    
    #### DrawStyle properties
    
    | Property           | Type              | Default                    | Description                                               |
    |--------------------|-------------------|----------------------------|-----------------------------------------------------------|
    | `fill`             | `ColorRGBa?`      | `ColorRGBa.WHITE`          | The fill color                                            |
    | `stroke`           | `ColorRGBa?`      | `ColorRGBa.BLACK`          | The stroke color                                          |
    | `strokeWeight`     | `Double`          | `1.0`                      | The stroke weight                                         |
    | `smooth`           | `Boolean`         | `true`                     | Should contours and segments look smooth or pixelated?    |
    | `lineCap`          | `LineCap`         | `LineCap.BUTT`             | The segment cap style                                     |
    | `lineJoin`         | `LineJoin`        | `LineJoin.MITER`           | The segment join style                                    |
    | `miterLimit`       | `Double`          | `4.0`                      | The segment join maximum miter length                     |
    | `fontMap`          | `FontMap?`        | `null`                     | The font to use                                           |
    | `kerning`          | `KernMode`        | `KernMode.METRIC`          | The kerning mode used for rendering text                  |
    | `textSetting`      | `TextSettingMode` | `TextSettingMode.SUBPIXEL` | The text setting mode                                     |
    | `colorMatrix`      | `Matrix55`        | `Matrix55.IDENTITY`        | The color matrix (used for images)                        |
    | `channelWriteMask` | `ChannelMask`     | `ChannelMask.ALL`          | The channel write mask                                    |
    | `shadeStyle`       | `ShadeStyle?`     | `null`                     | The shade style                                           |
    | `blendMode`        | `BlendMode`       | `BlendMode.OVER`           | The blend mode                                            |
    | `quality`          | `DrawQuality`     | `DrawQuality.QUALITY`      | A hint that controls the quality of some primitives       |
    | `cullTestPass`     | `CullTestPass`    | `CullTestPass.ALWAYS`      | What fragments be rendered: back or front facing?         |
    | `depthTestPass`    | `DepthTestPass`   | `DepthTestPass.ALWAYS`     | When should fragments pass the depth test                 |
    | `depthWrite`       | `Boolean`         | `false`                    | Should the fragment depth be written to the depth buffer? |
    | `stencil`          | `StencilStyle`    | `StencilStyle()`           | The stencil style                                         |
    | `frontStencil`     | `StencilStyle`    | `StencilStyle()`           | The stencil style for front-facing fragments              |
    | `backStencil`      | `StencilStyle`    | `StencilStyle()`           | The stencil style for back-facing fragments               |
    | `clip`             | `Rectangle?`      | `null`                     | A rectangle that describes where drawing will take place  |
    
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
                // colorMatrix, channelWriteMask, blendMode, quality, stencil, frontStencil, backStencil, clip
                drawer.drawStyle
            }
        }
    }

    @Text 
    """
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
