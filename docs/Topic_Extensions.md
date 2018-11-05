# Extensions

Extensions add functionality to a Program. Extensions can be used to control how a program draws, setup keyboard and
mouse bindings and much more.

##### Relevant APIs

```kotlin
fun Program.extend(extension: Extension) : Extension
fun <T : Extend> Program.extend(extension: T, configure: T.() -> Unit) : Extension
fun Program.extend(stage: ExtensionStage = ExtensionStage.BEFORE_DRAW, function: () -> Unit)
```

## Basic extension use

```kotlin
class ExtensionProgram : Program() {
    override fun setup() {
        extend(Screenshots())
    }
}
```

## Extension configuration

Some extensions have configurable options. They can be set using the configuring `extend` function as follows:

```kotlin
class ExtensionProgram : Program() {
    override fun setup() {
        extend(Screenshots()) {
            scale = 4.0
        }
    }
}
```

## Functional style extensions

The functional `extend` function allows one to use a single function as an extension. This can be used to make Programs
without implementing a draw() function. The benefit here is that lateinit vars at the class scope are fully eliminated.

```kotlin
class FunctionalProgram : Program() {
    override fun setup() {
        extend {
            drawer.circle(width / 2.0, height / 2.0, 50.0)
        }
    }
}
```