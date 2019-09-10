
# Live coding with orx-olive

By using Kotlin's ability to run script files we can build a live coding environment. The `orx-olive` library 
simplifies the work to be done to set up a live coding environment. Code and additional documentation for the library
can be found in the [Github repository](https://github.com/openrndr/orx/tree/master/orx-olive).

Start by adding `orx-live` to the `orxFeatures` set in your gradle build file.

```kotlin
fun main() = application {
    configure {
        width = 768
        height = 576
    }
    program {
        extend(Olive<Program>())
    }
}
```

When running this script you will see a file called `live.kts` appear in `src/main/kotlin`. When you edit
this file you will see that changes are automatically detected (after save) and that the program reloads. 

## Interaction with extensions

The Olive extension works well together with other extensions, but only those which are installed before
the Olive extension. In the following example we see the use of `Debug3D` in combination with `Olive`.

```kotlin
fun main() = application {
    program {
        extend(Debug3D())
        extend(Olive<Program>())
    }
}
```

## Adding script drag/drop support

A simple trick to turn your live coding host program into a versatile live coding environment is to add
file drop support. With this enabled one can drag a .kts file onto the window and drop it to load the script file.

```kotlin
fun main() = application {
    program {
        extend(Olive<Program>()) {
            this@program.window.drop.listen {
                this.script = it.files.first().absolutePath
            }
        }
    }
}
```

## Adding persistent state

Sometimes you want to keep parts of your application persistent, that means its state will survive a script reload.
In the following example we show how you can prepare the host program to contain a persistent camera device.

```kotlin
class PersistentProgram : Program() {
    lateinit var camera: FFMPEGVideoPlayer
}

fun main() = application {
    program(PersistentProgram()) {
        camera = FFMPEGVideoPlayer.fromDevice()
        camera.start()
        
        extend(Olive<PersistentProgram>()) {
            script = "src/main/PersistentCamera.Kt"
        }
    }
}
```

Note that when you create a custom host program you also have to adjust script files to include the program
type. For example `live.kts` would become like this.
```kotlin
@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*

{ program: PersistentProgram ->
    program.apply {
        extend {
            camera.next()
            drawer.drawStyle.colorMatrix = tint(ColorRGBa.GREEN) * grayscale(0.0, 0.0, 1.0)
            camera.draw(drawer)
        }
    }
}         
```         
