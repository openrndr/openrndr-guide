 
 # User Interfaces

This topic discusses the creation of graphical user interfaces in OPENRNDR applications.

## The Panel library

The Panel library provides an HTML/CSS like user interface toolkit and is written using OPENRNDR.

## Basic Usage

The easiest way to use Panel is to use it as a Program extension. When used as an extension all mouse and keyboard events are automatically handled and drawing of the user interface will take place after your program's `draw()` has been invoked.

To create a very simple user interface that consists of just a single button one would do the following: 
 
 <img src="media/ui-001.png"/> 
 
 ```kotlin
application {
    program {
        
        var color = ColorRGBa.GRAY.shade(0.250)
        extend(ControlManager()) {
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
        extend {
            drawer.clear(color)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C04UserInterfaces000.kt) 
 
 ## Style sheets

The Panel library borrows a lot of ideas from HTML/CSS based layouting systems, one of those ideas is style sheets.

Style sheets can be used as shown in the following example in which a style sheet is used to color a button pink. 
 
 <img src="media/ui-002.png"/> 
 
 ```kotlin
application {
    program {
        
        extend(ControlManager()) {
            styleSheet(has type "button") {
                background = Color.RGBa(ColorRGBa.PINK)
                color = Color.RGBa(ColorRGBa.BLACK)
            }
            
            layout {
                button {
                    label = "click me"
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C04UserInterfaces001.kt) 
 
 ### Selectors

The following example shows how to build and use complex selectors 
 
 ## Elements

The Panel library comes with a built-in set of elements with which user interfaces can be composed.

### Element
`Element` is the base class from which all other elements derive. `Element` can be used directly but it is advised to use `Div` instead.


### Div

The `Div` represents a rectangular area in which other elements can be placed. The `Div` element is the main ingredient in the creation of layouts. Divs are best created using the `div {}` builder. 
 
 ```kotlin
application {
    program {
        controlManager {
            layout {
                div("some-class-here", "another-class-here") {// -- children here
                }
            }
        }
    }
}
``` 
 
 ```kotlin
div("some-class-here", "another-class-here") {// -- children here
}
``` 
 
 ### Button
An ordinary labelled button.
The default width of buttons is set to Auto such that the width is determined by the label contents. 
 
 <img src="media/ui-006.png"/> 
 
 ```kotlin
application {
    configure {
        width = 770
        height = 45
    }
    program {
        extend(ControlManager()) {
            layout {
                button {
                    label = "Click me "
                    events.clicked.listen {// -- do something with the clicked event
                    }
                }
            }
        }
        extend {
            drawer.clear(ColorRGBa.GRAY.shade(0.250))
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C04UserInterfaces002.kt) 
 
 ### Slider

A horizontal labelled slider control.

##### Properties
 * `label : String` - the slider label
 * `precision : Int` - the number of digits behind the point, set to 0 for an integer slider
 * `value : Double` - the slider value
 * `range: Range` - the slider range, default is `Range(0.0, 1.0)`

##### Events
* `valueChanged` - emitted when the slider value has changed 
 
 <img src="media/ui-007.png"/> 
 
 ```kotlin
application {
    configure {
        width = 770
        height = 45
    }
    program {
        extend(SingleScreenshot()) {
            outputFile = "media/ui-007.png"
        }
        extend(ControlManager()) {
            layout {
            
                slider {
                    label = "Slide me"
                    value = 0.50
                    range = Range(0.0, 1.0)
                    precision = 2
                    events.valueChanged.listen {
                        println("the new value is ${it.newValue}")
                    }
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C04UserInterfaces003.kt) 
 
 ### ColorPickerButton
A button like control that slides out a HSV color picker when clicked

##### Properties
 * `label : String` - the label on the button
 * `value : ColorRGBa` - the currently picked color

##### Events
* `valueChanged` - emitted when a color is picked 
 
 <img src="media/ui-008.png"/> 
 
 ```kotlin
application {
    program {
        extend(SingleScreenshot()) {
            outputFile = "media/ui-008.png"
        }
        extend(ControlManager()) {
            layout {
            
                colorpickerButton {
                    label = "Pick a color"
                    color = ColorRGBa.PINK
                    events.valueChanged.listen {
                        println("the new color is ${it.color}")
                    }
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C04UserInterfaces004.kt) 
 
 ### DropdownButton
A button like control that slides out a list of items when clicked.

##### Properties
 * `label : String` - the label on the button
 * `value : Itme` - the currently picked item

##### Events
* `valueChanged` - emitted when an option is picked 
 
 <img src="media/ui-009.png"/> 
 
 ```kotlin
application {
    program {
        extend(SingleScreenshot()) {
            outputFile = "media/ui-009.png"
        }
        extend(ControlManager()) {
            layout {
            
                dropdownButton {
                    label = "Option"
                    
                    item {
                        label = "Item 1"
                        events.picked.listen {
                            println("you picked item 1")
                        }
                    }
                    
                    item {
                        label = "Item 2"
                        events.picked.listen {
                            println("you picked item 2")
                        }
                    }
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C04UserInterfaces005.kt) 
