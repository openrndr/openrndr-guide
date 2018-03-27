# Drawing with SVG files # 

##### Relevant APIs
```kotlin
drawer.composition(composition: Composition)
loadSVG(svg:String) : Composition
```

Loading a composition and drawing it can be done as follows:
```
var composition = loadSVG(File("data/drawing.svg").readText())
drawer.composition(composition)
```

Here a `Composition` is a tree structure with `Composition.root` as its root node.

Node Type         | Function
------------------|-----------------------------------------
`GroupNode`       | Holds multiple child nodes in `children`
`ShapeNode`       | Holds a a single shape in `shape`
`TextNode`        | Holds text (currently not implemented)
`ImageNode`       | Holds an image (currently not implemented)
`CompositionNode` | The base class for composition node

## Querying the composition

### Finding all shapes in the composition

```kotlin
val shapeNodes = composition.findShapes()
```

-------------------------
##### Notes
 * Writing to SVG files is explained in [Writing SVG Files](Topic_DrawingSVGFiles).
 * All shapes in the composition tree can be queried as explained in [Drawing Complex Shapes](Ttuorial_DrawingComplexShapes)