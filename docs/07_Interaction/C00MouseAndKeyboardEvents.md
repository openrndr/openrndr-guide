 
 # Mouse and keyboard events
All interaction in OPENRNDR manifests through events. 
 
 ## Listening to mouse events 
 
 ```kotlin
application {
    program {
        mouse.moved.listen {
            // -- it refers to a MouseEvent instance here
            println(it.position)
        }
        mouse.buttonDown.listen {
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/07_Interaction/C00MouseAndKeyboardEvents000.kt) 
 
 ### Overview of mouse events
event        | description                                                   | relevant MouseEvent properties
-------------|---------------------------------------------------------------|---------------------------------
`moved`      | generated when mouse has been moved                           | `position`, `modifiers`
`buttonDown` | generated when a button is pressed                            | `position`, `button`, `modifiers`
`buttonUp`   | generated when a button is released                           | `position`, `button`, `modifiers`
`scrolled`   | generated when mouse wheel is used                            | `position`, `rotation`
`clicked`    | generated when a button has been pressed and released         | `position`, `button`, `modifiers`
`dragged`    | generated when mouse has been moved while a button is pressed | `position`, `button`, `modifiers` 
 
 ## Listening to keyboard events
OPENRNDR provides two classes of keyboard events. The first are _key_ events, which should be used to respond to the user pressing or releasing buttons on the keyboard. The second class are _character_ events, which should be used for handling text input as they also deal with composed characters.

To use the _key_ events one listens to `keyboard.keyDown` events and compares the `key` value. For example: 
 
 ```kotlin
application {
    program {
        keyboard.keyDown.listen {
            // -- it refers to a KeyEvent instance here
            // -- compare the key value to a predefined key constant
            if (it.key == KEY_ARROW_LEFT) {}
        }
    }
}
``` 
 
 To use the _character_ events one listens to `keyboard.character` events which provide `character` values. For example: 
 
 ```kotlin
application {
    var input = ""
    program {
        keyboard.character.listen {
            input += it.character
        }
        keyboard.keyDown.listen {
            // -- it refers to a KeyEvent instance here
            // -- compare the key value to a predefined key constant
            if (it.key == KEY_BACKSPACE) {
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length - 1)
                }
            }
        }
    }
}
``` 
 
 ### Querying keyboard modifiers
Checking for modifiers can be done by checking if the desired modifier key is active in `modifiers`.  In the example below we check
if both shift and the left arrow key are pressed. 
 
 ```kotlin
application {
    program {
        keyboard.keyDown.listen {
            if (it.key == KEY_ARROW_LEFT && KeyboardModifier.SHIFT in it.modifiers) {}
        }
    }
}
``` 
 
 Note that also mouse events come with modifiers and can be queried in a similar way. 
 
 ### Overview of keyboard events

event       | description                                    | relevant KeyboardEvent properties
------------|------------------------------------------------|---------------------------------
`keyDown`   | generated when a key is pressed                | `key`, `modifiers`
`keyUp`     | generated when a key is released               | `key`, `modifiers`
`keyRepeat` | generated when a key is pressed and held       | `key`, `modifiers`
`character` | generated when an input character is generated | `character`, `modifiers`



### Overview of key constants

We provide constants for most of the functional/navigational keys.

constant             |  description
---------------------|------------------------------------------
`KEY_SPACEBAR`       | the spacebar key
`KEY_ESCAPE`         | the escape key
`KEY_ENTER`          | the enter key
`KEY_TAB`            | the tab key
`KEY_BACKSPACE`      | the backspace key
`KEY_INSERT`         | the insert key
`KEY_DELETE`         | the delete key
`KEY_ARROW_RIGHT`    | the arrow right key
`KEY_ARROW_LEFT`     | the arrow left key
`KEY_ARROW_UP`       | the arrow up key
`KEY_ARROW_DOWN`     | the arrow down key
`KEY_CAPSLOCK`       | the capslock key
`KEY_PRINT_SCREEN`   | the print screen or prtscrn key
`KEY_F1` … `KEY_F12` | the F1 … F12 function keys
`KEY_LEFT_SHIFT`     | the left shift key
`KEY_RIGHT_SHIFT`    | the right shift key

## Event processing order

Mouse and keyboard events are buffered and processed before `draw()` is called. It is possible but not advised to perform drawing on event. 
