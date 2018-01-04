# Drawing text #
OPENRNDR comes with two text drawing modes: vector based and image based. The vector based mode is advised for large font sizes while the bitmap based mode is advised for smaller font sizes.


## Relevant APIs ##
```kotlin
Drawer.fontMap
Drawer.text(text:String, x:Double, y:Double)
FontImageMap.fromUrl(url:String, size:Double)
FontVectorMap.fromUrl(url:String, size:Double)
```


## Drawing bitmap based text ##

```java
fun draw() {
    background(ColorRGBa.BLACK);
    drawer.fontMap = FontImageMap.fromUrl("file:fonts/SourceCodePro-Medium.ttf")
    drawer.text("here is a text", 20.0, 100.0)
}
```

## Drawing vector based text ##

```kotlin

public void draw() {
   background(ColorRGBa.BLACK);
   // currently under development
}
```

## Advanced text drawing ##

OPENRNDR comes with a `Writer` class that allows for basic typesetting. The `Writer` tool is based on the concept of text box and a cursor. 

Its use is easiest demonstrated through an example:

```java
fun draw() {
    drawer.background(ColorRGBa.BLACK)

    // -- First create a new Writer object. 
    // -- The writer needs a reference to the drawer in order to match style and transforms.
    val writer = Writer(drawer);

    // -- Set a font, this is a required step
    drawer.fontMap = FontImageMap.fromUrl("file:fonts/SourceCodePro-Medium.ttf")
    drawer.stroke = null
    drawer.fill = ColorRGBa.WHITE

    // -- Setup a 400 by 400 pixel text box at position 100, 100
    writer.box(100.0, 100.0, 400.0, 400.0)

    // -- Output some text
    writer.text("Here is the text")

    // -- Go to the next line and output more text
    writer.newLine().text("Here is another line of text")
}

```





