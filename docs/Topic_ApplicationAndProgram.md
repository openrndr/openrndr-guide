# Application and Program

Your first OPENRNDR touch-point is the `Program` class. A `Program`
essentially describes all program logic.

`Program` comes with two methods that can be used to implement
program logic: `setup()` and `draw()`.

A simple `Program` implementation looks like this:

```kotlin
class SimpleProgram: Program() {

    // -- don't initialize GPU resources; the GL context is not ready

    override fun setup() {
        // -- what is here is executed once
    }

    override fun draw() {
        // -- what is here is executed 'as often as possible'

    }
}
```

To run the program we have to create an application that hosts it:
```kotlin
fun main(args: Array<String>) {
    application(SimpleProgram(), Configuration())
}
```

## Application builder function

We provide a simpler and cleaner alternative to the
`Application` / `Program` construction. It is the `application` builder
function, with which both Application and Program can be constructed as
simple as:

```
fun main() = application {
    configure {
        // set Configuration options here
    }

    program {
        // -- what is here is executed once
        extend {
            // -- what is here is executed 'as often as possible'
        }
    }
}
```

Internally this produces `Application`, `Program` and `Extension`
instances to make this work. The application builder function works
nicely for a programming style that seeks to avoid `lateinit var` as
no class fields are used in the `Program`.

We have only recently introduced this builder, it is our preferred way
of writing applications, but you will find we use the older method
through-out our documentation and example code. We are working on
converting our work to the new style and we advice that you use this new
style too.
