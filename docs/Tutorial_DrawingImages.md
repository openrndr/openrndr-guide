# Drawing images #

## Relevant APIs ##

```java
Drawer.image(double x, double y)
Drawer.image(double x, double y, double width, double height)
Drawer.image(double sourceX, double sourceY, double sourceWidth, double sourceHeight, double destX, double destY, double destWidth, double destHeight)
```

```java
ColorBuffer image;

public void setup() {
    image = ColorBuffer.fromFile("data/myImage.png");
}

public void draw() {
    drawer.tint(new Color(1, 1, 1, Math.cos(seconds()) * 0.5 + 0.5);
    drawer.image(image, 0, 0);
}
```

## Changing the appearance ##

The drawer provides two methods of changing the appearance of images. The simplest one is `tint` which allows for simple image tinting. Tint performs a multiplication of the image color with the provided tint color.

```java
public void draw() {
    drawer.tint(new Color(1, 1, 1, Math.cos(seconds()) * 0.5 + 0.5);
    drawer.draw(image, 0, 0);
}
```

A more complex, yet more powerful way of changing the appearance is the `colorMatrix` method. Color matrices allow for a wider range of color transformations.

```java
public void draw() {
    drawer.colorMatrix(ColorTransforms.GRAYSCALE)
    drawer.draw(image, 0, 0);
} 
```

A number of preset color transforms are available in the `ColorTransforms` class.

| Transform name      | Description |
|---------------------|-------------|
| `NEUTRAL_GRAYSCALE` | Grayscale transform achieved by mixing R, G, B in equal amounts |
| `BT709_GRAYSCALE`   | Grayscale transform as defined in the BT709 standard |
| `BT601_GRAYSCALE`   | Grayscale transform as defined in the BT601 standard |
| `RED` | Dim blue and green channels |
| `GREEN` | Dim red and blue channels |
| `BLUE` | Dim red and green channels |
| `NEUTRAL` | No color transform |
| `INVERT` | Inverts the colors |

Custom color matrices can be made by constructing a `Matrix44`.

```java
public void draw() {
    // -- construct a color transform that switches the red and blue channel 
    Matrix44 ct = Matrix44.fromColumnVectors(
        new Vector4(0, 0, 1, 0),
        new Vector4(0, 1, 0, 0),
        new Vector4(1, 0, 0, 0),
        new Vector4(0, 0, 0, 1)
    );
    drawer.colorMatrix(ct);
    drawer.draw(image, 0, 0);
} 
```

Color matrices can also be built in a more literal way using the `ColorMatrixBuilder`. Here follows an example that produces the same color matrix as in the previous example.

```java
public void draw() {
    // -- construct a color transform that switches the red and blue channel 
    Matrix44 ct = 
        new ColorMatrixBuilder().
            red().plusBlue(1.0).
            green().plusGreen(1.0).
            blue().plusRed(1.0).
            build();
        
    drawer.colorMatrix(ct);
    drawer.draw(image, 0, 0);
} 
```
