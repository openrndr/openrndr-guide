# User Interfaces

This topic discusses the creation of graphical user interfaces in OPENRNDR applications.


## The Panel library

The Panel library provides an HTML/CSS like user interface toolkit and is written using OPENRNDR.

To use Panel, Add the panel library to your `build.gradle` configuration.

```groovy
dependencies {
    compile('org.openrndr.panel:openrndr-panel:0.3.3')
}
```

## Basic Usage

To create a very simple user interface that consists of just a single button.

```kotlin
var color = ColorRGBa.WHITE
override fun setup() {
    val cm = controlManager {
        layout {
            button {
                label = "click me"
                // -- listen to the click event
                clicked {
                    color = ColorRGBa(Math.random(), Math.random(), Math.random())
                }
            }
        }
    }
    extend(cm) // <- this registers the control manager as a Program Extension
}
```
[Complete tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/ui-001/src/main/kotlin/Example.kt)

## Style sheets

The Panel library borrows a lot of ideas from HTML/CSS based layouting systems, one of those ideas is style sheets.

Style sheets can be used as shown in the following example in which a style sheet is used to color a button blue.

```kotlin
controlManager {
    layout {
        styleSheet(has type "button") {
            background = Color.RGBa(ColorRGBa.BLUE)
        }

        button {
            label = "click me"
        }
    }
}

```
[Relevant tutorial code](https://github.com/openrndr/openrndr-tutorials/blob/master/ui-002/src/main/kotlin/Example.kt)


##### Stylesheet Properties

Property        | Values                                          | Default
----------------|-------------------------------------------------|---------
`display`       | Display.INLINE, .BLOCK, .FLEX, .NONE            | Display.BLOCK
`position`      | Position.ABSOLUTE, .RELATIVE, .FIXED, .STATIC   | Position.STATIC
`width`         | LinearDimension.PX, .Percent, .Auto             | Auto
`height`        | LinearDimension.PX, .Percent, .Auto             | Auto
`top`           | LinearDimension.PX                              | 0.px
`right`         | LinearDimension.PX                              | 0.px
`flexDirection` | FlexDirection.COLUMN, FlexDirection.ROW         | FlexDirection.Row
`flexGrow`      | FlexGrow.Ratio                                  | FlexGrow.Ratio(0.0)
`paddingTop`    | LinearDimension.PX                              | 0.px
`paddingBottom` | LinearDimension.PX                              | 0.px
`paddingLeft`   | LinearDimension.PX                              | 0.p
`paddingRight`  | LinearDimension.PX                              | 0.px
`marginTop`     | LinearDimension.PX                              | 0.px
`marginBottom`  | LinearDimension.PX                              | 0.px
`marginLeft`    | LinearDimension.PX                              | 0.px
`marginRight`   | LinearDimension.PX                              | 0.px
`color`         | Color.RGB, Color.INHERIT                        | WHITE
`background`    | Color.RGB, Color.INHERIT                        | TRANSPARENT
`fontSize`      | LinearDimension.PX                              | 14.px
`fontFamily`    | String                                          | default
`overflow`      | Overflow.VISIBLE, .HIDDEN, SCROLL               | Overflow.VISIBLE
`zIndex`        | ZIndex.Auto, .Value                             | ZIndex.Auto

## Selectors

The following example shows how to build and use complex selectors

```kotlin
styleSheet(has class_ "control-bar") {

    descendant(has type "button") {
        width = 100.percent
    }

    child(has type "slider") {
        width = 100.percent

        and (has state "hover") {
            background = Color.RGBa(ColorRGBa.RED)
        }
   }
}
```

