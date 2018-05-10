# Writing SVG files #

Relevant APIs
```kotlin
writeSVG(composition: Composition): String
```

OPENRNDR offers a simple writer with which `Compositions` can be converted to SVG files.

An example use:

```kotlin
val c = Composition()
// -- make an actual composition here
File("output.svg").writeText(writeSVG(c))
```

