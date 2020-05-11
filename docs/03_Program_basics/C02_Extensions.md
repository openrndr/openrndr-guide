 
 # Extensions

Extensions add functionality to a Program. Extensions can be used to control how a program draws, setup keyboard and
mouse bindings and much more. 
 
 ## Basic extension use
Here we demonstrate how to use an OPENRNDR extension. The extension that we use is the `Screenshots` extension, which, when 
the space bar is pressed will capture the application window's contents and save it to a timestamped file. 
 
 ```kotlin
fun main() = application {
    program {
        // -- one time setup code goes here
        extend(Screenshots())
        extend {// -- drawing code goes here
        }
    }
}
``` 
 
 ## Extension configuration
Some extensions have configurable options. They can be set using the configuring `extend` function as follows: 
 
 ```kotlin
fun main() = application {
    program {
        extend(Screenshots()) {
            scale = 4.0
        }
    }
}
``` 
 
 ## Extension functions
The functional `extend` function allows one to use a single function as an extension. This is commonly used to
create a "draw loop". 
 
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
contributed OPENRNDR extensions and add-ons. If you work from `openrndr-template` you can easily add and remove extensions 
from your project by editing the `orxFeatures` property in `build.gradle.kts`. 
