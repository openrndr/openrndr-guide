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
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/tree/master/svg-001)


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

-------------------------
##### Notes
 * Writing to SVG files is explained in [Writing SVG Files](Topic_WritingSVGFiles).
 * All shapes in the composition tree can be queried as explained in [Drawing Complex Shapes](Ttuorial_DrawingComplexShapes)
