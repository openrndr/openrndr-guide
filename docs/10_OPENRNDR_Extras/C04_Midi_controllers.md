 
 # Midi controllers with orx-midi 
 
 The [`orx-midi`](https://github.com/openrndr/orx/tree/master/orx-midi) library provides a simple interface
to interact with MIDI controllers. 

The library is easily added to a [openrndr-template](https://github.com/openrndr/openrndr-template) 
project by adding `orx-midi` to the `orxFeatures` line in `build.gradle.kts`

```
orxFeatures = setOf("orx-midi")
```
 
 
 ## Listing MIDI controllers 
 
 To connect to a MIDI controller you will need the exact name and vendor of the controller as they are reported
to the operating system. To discover these identifiers it is easiest to list the controllers, this can be done using
the `MidiDeviceDescription.list()` function. 
 
 ```kotlin
application {
    program {
        MidiDeviceDescription.list().forEach {
            println("name: '${it.name}', vendor: '${it.vendor}', receiver:${it.receive}, transmitter:${it.transmit}")
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C04_Midi_controllers000.kt) 
 
 From what this program outputs you can pick a controller by copying its name and vendor identifiers. 
 
 ## Connecting to a MIDI controller 
 
 Once you have the controller name and vendor you can use `MidiTransceiver.fromDeviceVendor` to open the
midi controller. For example to use a Behringer BCR2000 controller on a Ubuntu system we can use the following. 
 
 ```kotlin
application {
    program {
        val controller = MidiTransceiver.fromDeviceVendor("BCR2000 [hw:2,0,0]", "ALSA (http://www.alsa-project.org)")
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C04_Midi_controllers001.kt) 
 
 ## Listening to the controller 
 
 Once connected to a controller we can start listening to the MIDI events it sends out. The orx-midi library
supports controller change, note on and note off events. 
 
 ```kotlin
application {
    program {
        val controller = MidiTransceiver.fromDeviceVendor("BCR2000 [hw:2,0,0]", "ALSA (http://www.alsa-project.org)")
        controller.controlChanged.listen {
            println("control change: channel: ${it.channel}, control: ${it.control}, value: ${it.value}")
        }
        
        controller.noteOn.listen {
            println("note on: channel: ${it.channel}, key: ${it.note}, velocity: ${it.velocity}")
        }
        
        controller.noteOff.listen {
            println("note off:  ${it.channel}, key: ${it.note},")
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C04_Midi_controllers002.kt) 
