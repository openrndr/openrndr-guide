# Mouse and keyboard events

All interaction in OPENRNDR manifests through events.

## Listening to mouse events

```kotlin
fun setup() {
    mouse.moved.listen {
        // -- it refers to a MouseEvent instance here
        it.position
    }
    mouse.buttonDown.listen {

    }
}
```

Tutorial [`mouse-001`](https://github.com/openrndr/openrndr-tutorials/blob/master/mouse-001/src/main/kotlin/Example.kt) shows how to use the mouse position, [`mouse-002`](https://github.com/openrndr/openrndr-tutorials/blob/master/mouse-002/src/main/kotlin/Example.kt) shows how to use mouse button events.

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

```kotlin
fun setup() {
    keyboard.keyDown.listen {
        // -- it refers to a KeyEvent instance here
        it.key
    }
}
```

### Overview of keyboard events

event       | description                              | relevant KeyboardEvent properties
------------|------------------------------------------|---------------------------------
`keyDown`   | generated when a key is pressed          | `key`, `modifiers`
`keyUp`     | generated when a key is released         | `key`, `modifiers`
`keyRepeat` | generated when a key is pressed and held | `key`, `modifiers`


## Event processing order

Mouse and keyboard events are buffered and processed before `draw()` is called. It is possible but not adviced to perform drawing on event.
