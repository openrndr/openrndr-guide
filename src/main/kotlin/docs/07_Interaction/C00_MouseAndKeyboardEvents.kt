@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Mouse And Keyboard Events")
@file:ParentTitle("Interaction")
@file:Order("100")
@file:URL("interaction/mouseAndKeyboardEvents")

package docs.`07_Interaction`

import org.openrndr.KEY_ARROW_LEFT
import org.openrndr.KEY_BACKSPACE
import org.openrndr.KeyModifier
import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.math.Vector2


fun main() {
    @Text
    """
    # Mouse and keyboard events
    
    Most user-input interaction in OPENRNDR manifests through events.

    # Mouse events
    
    A simple demonstration of a listening for mouse button clicks looks as follows:     
    """

    @Code
    application {
        program {
            mouse.buttonDown.listen {
                // -- it refers to a MouseEvent instance here
                println(it.position)
            }
        }
    }

    @Text 
    """
    Every program has a `mouse` object that exposes events and mouse properties. 
    In the example above we attach a listener to the `clicked` event.
    The `{}` block after `listen` is a short-hand notation for passing a 
    function into `listen`.
    
    There are some limitations to what the listener function can (or should) 
    do. As a rule-of-thumb: don't draw in events. The result of drawing
    in listener functions is that it does not work. You are encouraged to 
    use listener functions to change the state of your program.   
                        
    Let's provide a commonly used pattern to deal with this limitation. 
    The idea here is that we introduce variables in our program that are used
    to communicate between the listener function and the draw function.
    """

    @Code
    application {
        program {
            // -- a variable to keep of where we clicked
            // -- this is an "optional type" that can be set to null
            var drawPosition : Vector2? = null

            mouse.buttonDown.listen {
                drawPosition = it.position
            }

            extend {
                // -- check if the drawPosition is not null
                drawPosition?.let {
                    drawer.circle(it.x, it.y, 100.0)
                    // -- reset the drawPosition to null
                    drawPosition = null
                }
            }
        }
    }

    @Text
    """
    ### Overview of mouse events
    
    event        | description                                                   | relevant MouseEvent properties
    -------------|---------------------------------------------------------------|---------------------------------
    `moved`      | generated when mouse has been moved                           | `position`, `modifiers`
    `buttonDown` | generated when a button is pressed                            | `position`, `button`, `modifiers`
    `buttonUp`   | generated when a button is released                           | `position`, `button`, `modifiers`
    `scrolled`   | generated when mouse wheel is used                            | `position`, `rotation`
    `clicked`    | generated when a button has been pressed and released         | `position`, `button`, `modifiers`
    `dragged`    | generated when mouse has been moved while a button is pressed | `position`, `button`, `modifiers`

    # Keyboard events
    
    OPENRNDR provides two classes of keyboard events. The first are _key_ 
    events, which should be used to respond to the user pressing or releasing 
    buttons on the keyboard. The second class are _character_ events, which 
    should be used for handling text input as they also deal with composed 
    characters.

    To use the _key_ events one listens to `keyboard.keyDown` events and 
    compares the `key` value. For example:
    """

    @Code
    application {
        program {
            keyboard.keyDown.listen {
                // -- it refers to a KeyEvent instance here
                // -- compare the key value to a predefined key constant
                if (it.key == KEY_ARROW_LEFT) {
                    // -- react to the keypress here
                }
            }
        }
    }

    """
    Note that the `key` property should *not* be used to check for 
    alpha-numeric keys. The `key` property returns a
     key identifier that is relative to a US keyboard layout. Instead, 
     alpha-numeric key comparisons should be made through
     the `name` property. Note that the `name` property is not affected 
     by pressing the shift-key.
    """

    @Code
    application {
        program {
            keyboard.keyDown.listen {
                // -- it refers to a KeyEvent instance here
                // -- compare the name value against "a"
                if (it.name == "a") {
                    // -- react to the keypress here
                }
            }
        }
    }


    @Text
    """
    To use the _character_ events one listens to `keyboard.character` 
    events which provide `character` values. For example:
    """

    @Code
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
                    if (input.isNotEmpty()) {
                        input = input.substring(0, input.length - 1)
                    }
                }
            }
        }
    }


    @Text
    """
    ### Querying key modifiers
    
    Checking for modifiers can be done by checking if the desired modifier 
    key is active in `modifiers`.  In the example below we check
    if both shift and the left arrow key are pressed.
    """

    @Code
    application {
        program {
            keyboard.keyDown.listen {
                if (it.key == KEY_ARROW_LEFT && KeyModifier.SHIFT in it.modifiers) {
                    // -- react to the keypress here
                }
            }
        }
    }

    @Text
    """
    Note that also mouse events come with modifiers and can be queried in 
    a similar way.

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

    Mouse and keyboard events are buffered and processed before `draw()` 
    is called. It is possible but not advised to perform drawing on event.
    """
}
