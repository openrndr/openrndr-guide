# User Interfaces

This topic discusses the creation of graphical user interfaces in OPENRNDR applications.


## The Panel library

The Panel library provides an HTML/CSS like user interface toolkit and is written using OPENRNDR.

To be able to use the Panel library add it to your `build.gradle` configuration.

```groovy
dependencies {
    compile('org.openrndr.panel:openrndr-panel:0.3.7')
}
```

## Basic Usage

The easiest way to use Panel is to use it as a Program extension. When used as an extension all mouse and keyboard events are automatically handled and drawing of the user interface will take place after your program's `draw()` has been invoked. 

To create a very simple user interface that consists of just a single button one would do the following:

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

## Elements

The Panel library comes with a built-in set of elements with which user interfaces can be composed.

### Element
`Element` is the base class from which all other elements derive. `Element` can be used directly but it is advised to use `Div` instead.


### Div

The `Div` represents a rectangular area in which other elements can be placed. The `Div` element is the main ingredient in the creation of layouts. Divs are best created using the `div {}` builder. 

```kotlin
div("some-class-here", "another-class-here") {
    // -- children here
}
```

### Button

An ordinary labelled button.

```kotlin
button { 
    label = "Click me"
    events.clicked.subscribe {
        // -- do something with the clicked event
    }
}
```
The default width of buttons is set to Auto such that the width is determined by the label contents.


### Slider 

A horizontal labelled slider control. 

##### Properties
 * `label : String` - the slider label
 * `precision : Int` - the number of digits behind the point, set to 0 for an integer slider
 * `value : Double` - the slider value
 * `range: Range` - the slider range, default is `Range(0.0, 1.0)`

##### Events
 * `valueChanged` - emitted when the slider value has changed
 
```kotlin
slider {
    label = "Slide me"
    value = 0.5
    range = Range(0.0, 1.0)
    precision = 3
    events.valueChanged.subscribe {
        println("the new value is ${it.newValue})
    }
}
```

### ColorPickerButton 
A button like control that slides out a HSV color picker when clicked

##### Properties
 * `label : String` - the label on the button
 * `value : ColorRGBa` - the currently picked color

##### Events
 * `valueChanged` - emitted when a color is picked
 
```kotlin 
colorPickerButton {
    label = "Pick a color"
    value = ColorRGBa.PINK
    events.valueChanged.subscribe {
        println("the new color is ${it.newValue})
    }
} 
```
