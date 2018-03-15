# Application Flow

This section covers default and alternate application flows.

## Common application flow

The most common application flow is one in which a single window is created in which your program will run until termination. The call to `Application.run()` below is blocking, it will only return when a termination is requested.

```kotlin
class Main:Program { [...] }

fun main(args:Array<String>) {
    // -- runs the Main program
    Application.run(Main(), configuration { [...] })
}
```

## Start-up or configuration dialogs

In some scenarios it is desirable to present a simple dialog before the main program commences, for example in the case you
want the user to to configure resolution and fullscreen settings. While OPENRNDR natively doesn't offer the tools to create user interfaces it does offer the functionality to create a window to host a configuration dialog.


```kotlin
class Configuration:Program { [...] }
class Main:Program { [...] }

fun main(args:Array<String>) {
    // -- runs the Configuration program and blocks until it is terminated
    Application.run(Configuration(), configuration { [...] })

    // -- runs the Main program
    Application.run(Main(), configuration { [...] })
}
```

Tutorial [`start-dialog-001`](https://github.com/openrndr/openrndr-tutorials/blob/master/start-dialog-001/src/main/kotlin/Example.kt) demonstrates how to set up a start dialog.

## Multiple simultaneous application windows

OPENRNDR comes with the support for using more than one similtaneous application window.
The windows are backed by resource-sharing OpenGL contexts, as such resources can be used in more than one window.

Setting up a multi-window application involves using the `Application.runAsync()` function.

```kotlin
class Left:Program { [...] }
class Right:Program { [...] }

fun main(args:Array<String>) {
    // -- Runs the Left and Right programs asynchroneously
    Application.runAsync(Left(), configuration { [...] })
    Application.runAsync(Right(), configuration { [...] })
}
```

Tutorial [`multi-window-001`](https://github.com/openrndr/openrndr-tutorials/blob/master/multi-window-001/src/main/kotlin/Example.kt) demonstrates simultaneous windows.