# Drawing images #

FIX ME: Add a little sentence here. Question: should Relevant APIs be on top, or be under the other topics, or explain how to use the APIs??

## Relevant APIs ##

```kotlin
fun Drawer.image(image: ColorBuffer, x: Double = 0.0, y: Double = 0.0)
fun Drawer.image(image: ColorBuffer, x: Double, y: Double, width: Double, height:Double)
fun Drawer.image(image: ColorBuffer, source: Rectangle, dest: Rectangle)
```

## Loading and drawing images

```kotlin
lateinit var image: ColorBuffer;

override fun setup() {
    image = ColorBuffer.fromUrl("file:data/myImage.png");
}

override fun draw() {
    drawer.image(image);
}
```
## Drawing parts of images

```kotlin
override fun draw() {
    drawer.image(image, Rectangle(Vector2(0.0, 0.0), 100.0, 200.0), Rectangle(Vector2(0.0, 0.0), 100.0, 200.0))
}
```

## Changing the appearance ##

The drawer provides two methods of changing the appearance of images. The simplest one is `tint` which allows for simple image tinting. Tint performs a multiplication of the image color with the provided tint color.

```kotlin
override fun draw() {
    drawer.tint(ColorRGBa(1.0, 1.0, 1.0, Math.cos(seconds()) * 0.5 + 0.5)
    drawer.image(image)
}
```

A more complex, yet more powerful way of changing the appearance is the `colorMatrix` method. Color matrices allow for a wider range of color transformations.

```kotlin
override fun draw() {
    drawer.colorMatrix(ColorTransforms.GRAYSCALE)
    drawer.image(image)
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

```kotlin
override fun draw() {
    // -- construct a color transform that switches the red and blue channel 
    val ct = Matrix44.fromColumnVectors(
        new Vector4(0, 0, 1, 0),
        new Vector4(0, 1, 0, 0),
        new Vector4(1, 0, 0, 0),
        new Vector4(0, 0, 0, 1)
    );
    drawer.colorMatrix = ct;
    drawer.draw(image, 0, 0)
} 
```