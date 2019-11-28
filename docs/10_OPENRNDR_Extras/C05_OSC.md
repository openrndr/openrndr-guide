 
 # Handling OSC messages with orx-osc 
 
 The [`orx-osc`](https://github.com/openrndr/orx/tree/master/orx-osc) osc provides a simple interface
to interact with OSC hosts and clients. 

The library is easily added to a [openrndr-template](https://github.com/openrndr/openrndr-template) 
project by adding `orx-osc` to the `orxFeatures` line in `build.gradle.kts`

```
orxFeatures = setOf("orx-osc")
```
 
 
 ## Listening to OSC messages 
 
 To listen to OSC messages we need to start an OSC server and use `listen` function to install listeners 
 
 ```kotlin
application {

    program {
        val osc = OSC()
        osc.listen("/live/track/2") { it ->
            // -- get the first value
            val firstValue = it[0] as Float
        }
        extend {
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C05_OSC000.kt) 
 
 ## Sending OSC messages 
 
 ```kotlin
application {
    program {
        val osc = OSC()
        
        extend {
            osc.send("/some/address", listOf(1.0f, 2.0f))
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C05_OSC001.kt) 
