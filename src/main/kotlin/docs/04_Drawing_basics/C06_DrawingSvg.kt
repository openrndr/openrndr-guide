package docs.`04_Drawing_basics`

import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.extra.noise.Random.Vector2
import org.openrndr.shape.CompositionDrawer
import org.openrndr.svg.loadSVG
import org.openrndr.svg.writeSVG
import java.io.File

fun main(args: Array<String>) {
    @Text """# Drawing SVG
Loading a composition and drawing it can be done as follows:
```
var composition = loadSVG("data/drawing.svg")
drawer.composition(composition)
```

Note that OPENRNDR's support for SVG files works best with SVG files that are saved in the Tiny SVG 1.x profile .


When drawing a composition from SVG you will notice that fill and stroke colors from the SVG file are used over the `Drawer` colors.

## Compositions

Here a `Composition` is a tree structure with `Composition.root` as its root node.

##### CompositionNode types

Node Type         | Function
------------------|-----------------------------------------
`GroupNode`       | Holds multiple child nodes in `children`
`ShapeNode`       | Holds a a single shape in `shape`
`TextNode`        | Holds text (currently not implemented)
`ImageNode`       | Holds an image (currently not implemented)
`CompositionNode` | The base class for composition node

##### CompositionNode properties

Property name                | Property type      | Description
-----------------------------|--------------------|------------
`transform`                  | `Matrix44`         | local transformation
`fill`                       | `CompositionColor` | fill color
`stroke`                     | `CompositionColor` | stroke color
`id`                         | `String?`          | node id
`parent`                     | `CompositionNode?` | node parent
`effectiveFill` (read-only)  | `ColorRGBa?`       | the effective fill color, potentially inherited from ancestor
`effectiveStroke`(read-only) | `ColorRGBa?`       | the effective stroke color, potentially inherited from ancestor

##### GroupNode properties

Property name   | Property type                   | Description
----------------|---------------------------------|---------------
`children`      | `MutableList<CompositionNode>`  | child nodes

##### ShapeNode properties

Property name   | Property type  | Description
----------------|----------------|---------------
`shape`         | `Shape`        | a single shape

## Querying the composition

### Finding all shapes in the composition

```kotlin
val shapeNodes = composition.findShapes()
```

## Modifying the composition

Since a `Composition` contains many immutable parts it is easier to (partially) replace parts of the composition.

```kotlin
val m = translate(1.0, 0.0, 0.0);
composition.root.map {
  if (it is ShapeNode) {
    it.copy(shape=it.shape.transform(m))
  } else {
    it
  }
}
```

## Creating compositions manually

```kotlin
val root = GroupNode()
val c = Composition(root)
```
"""

    @Text """## Drawer interface for compositions"""
    @Text """Creating Compositions from code may be a bit tedious. [`CompositionDrawer`](https://api.openrndr.org/org.openrndr.shape/-composition-drawer/index.html) simplifies creating compositions by
using a `Drawer`-like interface.
    """.trimMargin()

    @Code.Block
    run {
        // -- create the composition drawer
        val compositionDrawer = CompositionDrawer()

        // -- set fill/stroke and draw a cicrcle
        compositionDrawer.fill = ColorRGBa.PINK
        compositionDrawer.stroke = ColorRGBa.BLACK
        compositionDrawer.circle(Vector2(100.0, 100.0), 50.0)

        // -- get the composition from the composition drawer
        val composition = compositionDrawer.composition
    }

    @Text """## Converting compositions to SVG"""
    @Text """Compositions can be converted to SVG using the `writeSVG` function.

In the following example we convert a composition to an SVG document and save it to file. 
    """

    @Code.Block
    run {
        // -- load in a composition
        val composition = loadSVG("data/example.svg")

        // -- convert the composition back to an SVG document as a string
        // -- note that the svg output will be different from the original svg
        val svgDoc = writeSVG(composition)

        // -- write the svg string to a file
        File("output.svg").writeText(svgDoc)
    }

}