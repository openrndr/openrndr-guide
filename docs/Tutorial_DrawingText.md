# Drawing text #
OPENRNDR comes with two text drawing modes: vector based and image based. The vector based mode is advised for large font sizes while the bitmap based mode is advised for smaller font sizes.


## Relevant APIs ##
```java
Drawer.text(FontMap fontMap, String text, double x, double y)
FontImageMap.fromUrl(String url, int size)
FontVectorMap.fromUrl(String url, int size)
```


## Drawing bitmap based text ##

```java
public void draw() {
    background(ColorRGBa.BLACK);
    drawer.text(FontImageMap.fromUrl("cp:fonts/SourceCodePro-Medium.ttf", 12, "here is a text", 20, 100);
}
```

## Drawing vector based text ##

```java

public void draw() {
   background(ColorRGBa.BLACK);
   drawer.text(FontVectorMap.fromUrl("cp:fonts/SourceCodePro-Medium.ttf", 40, "here is a text", 20, 100);
}
```

## Advanced text drawing ##

OPENRNDR comes with a `Writer` class that allows for basic typesetting. The `Writer` tool is based on the concept of text box and a cursor. 

Its use is easiest demonstrated through an example:

```java
public void draw() {
    // -- First create a new Writer object. 
    // -- The writer needs a reference to the drawer in order to match style and transforms.
    Writer writer = new Writer(drawer);

    // -- Set a font, this is a required step
    writer.font(FontImageMap.fromUrl("cp:fonts/SourceCodePro-Medium.ttf", 12));

    // -- Setup a 400 by 400 pixel text box at position 100, 100
    writer.box(100, 100, 400, 400);

    // -- Output some text
    writer.text("Here is the text");

    // -- Go to the next line and output more text
    writer.newLine().text("Here is another line of text");
}

```





