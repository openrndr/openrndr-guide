# Drawing text #
OPENRNDR comes with two text drawing modes: vector based and image based. The vector based mode is advised for large font sizes while the bitmap based mode is advised for smaller font sizes.

## Relevant APIs ##
```kotlin
Drawer.fontMap
Drawer.text(text:String, x:Double, y:Double)
FontImageMap.fromUrl(url:String, size:Double)
```

## Drawing bitmap based text ##

```kotlin
fun draw() {
    background(ColorRGBa.BLACK);
    drawer.fontMap = FontImageMap.fromUrl("file:fonts/SourceCodePro-Medium.ttf")
    drawer.text("here is a text", 20.0, 100.0)
}
```

## Advanced text drawing ##

OPENRNDR comes with a `Writer` class that allows for basic typesetting. The `Writer` tool is based on the concept of text box and a cursor. 

Its use is easiest demonstrated through an example:

```kotlin
fun draw() {
    drawer.background(ColorRGBa.BLACK)

    // -- First create a new Writer object. 
    // -- The writer needs a reference to the drawer in order to match style and transforms.
    val writer = Writer(drawer)

    // -- Set a font, this is a required step
    drawer.fontMap = FontImageMap.fromUrl("file:fonts/SourceCodePro-Medium.ttf")
    drawer.stroke = null
    drawer.fill = ColorRGBa.WHITE

    // -- Setup a 400 by 400 pixel text box at position 100, 100
    writer.box = Rectangle(Vector2(100.0, 100.0), 400.0, 400.0)

    // -- Output some text
    writer.text("Here is the text")

    // -- Go to the next line and output more text
    writer.newLine().text("Here is another line of text")
}
```

### Specifying the text area

The writer has a `box` property that determines the area in which it can place text.

```kotlin
    writer.box = Rectangle(Vector2(100.0, 100.0), 400.0, 400.0)
```

In some cases you may want to have a an infinitely large box, this avoids line breaks altogether.

```kotlin
    writer.box = Rectangle(Vector2(100.0, 100.0), Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
```

A quick way to set the box to equate the bounds of the screen:

```kotlin
    writer.box = drawer.bounds
    // -- with margins of 50 pixels
    writer.box = drawer.bounds.offsetEdges(-50.0)
```

### Text properties

The `Writer` has several properties that affect the appearance of text.

| Property name      | Description |
|---------------------|-------------|
| `tracking` | additional space between the characters |
| `leading`   | additional space between lines of text |
| `ellipsis`   | the character to display when text does not fit the designated space|