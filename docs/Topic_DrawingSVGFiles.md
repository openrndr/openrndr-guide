# Drawing with SVG files # 

Relevant APIs
```kotlin
drawer.composition(composition: Composition)
loadSVG(svg:String) : Composition
```

Loading a composition and drawing it can be done as follows:
```
var composition = loadSVG(File("data/drawing.svg").readText())

drawer.composition(composition)
```
