# Using RNDR from Kotlin #

Using RNDR from Kotlin is easy!


## Basic Kotlin Program ##

```kotlin
class KotlinProgram : Program {
    override fun draw() {
         drawer.background(ColorRGBa.BLACK).fill(ColorRGBa.WHITE).rectangle(20,20,120,120)
    }
}

fun main(args: Array<String>) {
    Application.run(KotlinProgram(), Configuration())
}
```
