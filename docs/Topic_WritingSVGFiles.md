# Writing SVG files #

This covers various ways of generating SVG files from OPENRNDR.

## Generating SVG files from `Composition`

Relevant APIs
```kotlin
fun writeSVG(composition: Composition): String
fun Composition.saveToFile(file:File)
```

OPENRNDR offers a simple writer with which `Compositions` can be converted to SVG files.

An example use:

```kotlin
val c = Composition()
// -- make an actual composition here
composition.saveToFile(File("output.svg"))
```

## Using CompositionDrawer
Because creating `Composition` trees may be too difficult to do by hand OPENRNDR provides tools to simplify this.
`CompositionDrawer` provides a `Drawer`-like API with which compositions can be built.

#### Example use
```kotlin
fun main() = application { 
  program {
    val compositionDrawer = CompositionDrawer()
    compositionDrawer.fill = null
    compositionDrawer.stroke = ColorRGBa.BLACK
    compositionDrawer.circle(Vector2(width / 2.0, height / 2.0), 100.0)
    val composition = compositionDrawer.composition
    composition.saveToFile(File("output.svg"))
    
    extend {
      drawer.background(ColorRGBa.PINK)
      drawer.composition(composition)
    }
  }
}
```
