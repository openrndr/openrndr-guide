
# Images

Images are stored in `ColorBuffer` instances, the image data resides in GPU memory

## Loading and drawing images

Images are loaded using the `loadImage` function and drawn using `Drawer.image`.


<img src="media/image-001.png"/>

```kotlin
program {
    val image = loadImage("data/cheeta.jpg")
    
    extend {
        drawer.image(image)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C01_Images000.kt)

To change the location of the image one can use `Drawer.image` with extra coordinates provided.

```kotlin
drawer.image(image, 40.0, 40.0)
```

Extra `width` and `height` arguments can be provided to draw a scaled version of the image

```kotlin
drawer.image(image, 40.0, 40.0, 64.0, 48.0)
```

## Drawing parts of images
It is possible to draw parts of images by specifying _source_ and _target_ rectangles. The source rectangle describes
the area that should be taken from the image and presented in the target rectangle.

<img src="media/image-002.png"/>

```kotlin
program {
    val image = loadImage("data/cheeta.jpg")
    
    extend {
        val source = Rectangle(0.0, 0.0, 320.0, 240.0)
        val target = Rectangle(160.0, 120.0, 320.0, 240.0)
        drawer.image(image, source, target)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C01_Images001.kt)

## Drawing many parts of images

<img src="media/image-003.png"/>

```kotlin
program {
    val image = loadImage("data/cheeta.jpg")
    
    extend {
        val a = 5
        val areas = (0..10).flatMap { y ->
            (0..10).map { x ->
                val source = Rectangle(x * (width / 10.0), y * (height / 10.0), width / 5.0, height / 5.0)
                val target = Rectangle(x * (width / 10.0), y * (height / 10.0), width / 10.0, height / 10.0)
                source to target
            }
        }
        drawer.image(image, areas)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C01_Images002.kt)

## Changing the appearance of images
A linear color transform can be applied to images by setting `Drawer.drawStyle.colorMatrix` to a `Matrix55` value.
### Tinting
Tinting multiplies the image color with a _tint color_.

<img src="media/image-004.png"/>

```kotlin
program {
    val image = loadImage("data/cheeta.jpg")
    
    extend {
        drawer.drawStyle.colorMatrix = tint(ColorRGBa.RED)
        drawer.image(image, 0.0, 0.0)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C01_Images003.kt)

### Inverting
Drawing an image with inverted colors can be achieved by using the `invert` color matrix.

<img src="media/image-005.png"/>

```kotlin
program {
    val image = loadImage("data/cheeta.jpg")
    
    extend {
        drawer.drawStyle.colorMatrix = invert
        drawer.image(image, 0.0, 0.0)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C01_Images004.kt)

### Grayscale
Drawing an image with inverted colors can be achieved by using the `grayscale` color matrix.

<img src="media/image-006.png"/>

```kotlin
program {
    val image = loadImage("data/cheeta.jpg")
    
    extend {
        // -- the factors below determine the RGB mixing factors
        drawer.drawStyle.colorMatrix = grayscale(1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0)
        drawer.image(image, 0.0, 0.0)
    }
}
```

[Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/04_Drawing_basics/C01_Images005.kt)
