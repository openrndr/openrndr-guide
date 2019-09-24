 
 # Extensions

Extensions add functionality to a Program. Extensions can be used to control how a program draws, setup keyboard and
mouse bindings and much more. 
 
 ## Basic extension use 
 
 ```kotlin
fun main(args: Array<String>) = application {
    program {
        extend(Screenshots())
    }
}
``` 
 
 ## Extension configuration
Some extensions have configurable options. They can be set using the configuring `extend` function as follows: 
 
 ```kotlin
fun main(args: Array<String>) = application {
    program {
        extend(Screenshots()) {
            scale = 4.0
        }
    }
}
``` 
 
 ## Extension functions
The functional `extend` function allows one to use a single function as an extension. 
 
 ```kotlin
fun main(args: Array<String>) = application {
    program {
        extend {
            drawer.circle(width / 2.0, height / 2.0, 50.0)
        }
    }
}
``` 
 
 ## Built-in and contributed extensions 
 
 OPENRNDR provides a few built-in extensions to simplify common tasks. One is `Screenshots`, which is used
to create screenshots of your programs. Another is `ScreenRecorder` which is used to write videos to files.

Next to the built-in extensions there is [ORX](https://github.com/openrndr/orx), an extensive repository of provided and
contributed OPENRNDR extensions and add-ons. 
