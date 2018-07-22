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
    drawer.image(image, Rectangle(Vector2(0.0, 0.0), 100.0, 200.0), Rectangle(Vector2(0.0, 0.0), 100.0, 200.0))
```

Making use of `bounds` and the `Rectangle` class we can scale up an image to fit the screen.

```kotlin
    drawer.image(image, image.bounds, drawer.bounds)
```

## Changing the appearance ##

A linear color transform can be applied to images by setting `Drawer.drawStyle.colorMatrix`.

TODO: explain color matrices

##### Tinting

Tinting can be achieved by constructing a color matrix using the `tint()` function.

```kotlin
    drawer.drawStyle.colorMatrix = tint(ColorRGBa(1.0, 1.0, 1.0, Math.cos(seconds) * 0.5 + 0.5))
    drawer.image(image)
```

##### Inverting

Drawing an image with inverted colors can be achieved by using the color matrix `invert`.

```kotlin
    drawer.drawStyle.colorMatrix = invert
    drawer.image(image)
```

##### Grayscale

```kotlin
    drawer.drawStyle.colorMatrix = grayscale(0.33, 0.33, 0.33)
    drawer.image(image)
```


##### Custom color matrices

Custom color matrices can be made by constructing a `Matrix55`.

```kotlin
override fun draw() {
    val ct = Matrix55(c0r0 = 1.0)
    drawer.drawStyle.colorMatrix = ct;
    drawer.draw(image, 0, 0)
} 
```
