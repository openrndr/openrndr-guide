# Color


## Basic RGBa color

OPENRNDR communicates most of its color in RGBa color space. That is red, green, blue and alpha.

```kotlin
val color = ColorRGBa(1.0, 0.0, 1.0)
```

`ColorRGBa` expects its channel values to in \[0, 1\]. `ColorRGBa(1.0, 1.0, 1.0) is thus white.

`ColorRGBa` (and the other color classes) are immutable.

### Color operations

```kotlin
ColorRGBa.shade(shade: Double): ColorRGBa
ColorRGBa.opacify(opacity: Double): ColorRGBa
```


### Predefined colors

```kotlin
ColorRGBa.BLACK
ColorRGBa.WHITE
ColorRGBa.RED
ColorRGBa.GREEN
ColorRGBa.BLUE
ColorRGBa.YELLOW
ColorRGBa.PINK
```

## Color models

OPENRNDR comes with tools to convert between various color spaces

Class name  | Color space description
------------|---------------------------------------
`ColorRGBa` | sRGB and linear RGB
`ColorHSVa` | Hue, saturation, value
`ColorHSLa` | Hue, saturation, lightness
`ColorXSVa` | Xue, saturation, value, kuler like colorspace
`ColorXSLa` | Xue, saturation, lightness, kuler like colorspace
