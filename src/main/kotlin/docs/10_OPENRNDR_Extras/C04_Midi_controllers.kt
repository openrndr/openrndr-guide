package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiTransceiver

fun main() {

    @Text "# Midi controllers with orx-midi"

    @Text """The [`orx-midi`](https://github.com/openrndr/orx/tree/master/orx-midi) library provides a simple interface
to interact with MIDI controllers. 
"""

    @Text "## Prerequisites"
    @Text """Assuming you are working on an [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
project, all you have to do is enable `orx-midi` in the `orxFeatures`
 set in `build.gradle.kts` and reimport the gradle project."""

    @Text "## Listing MIDI controllers"
    @Text """To connect to a MIDI controller you will need the exact name and vendor of the controller as they are reported
to the operating system. To discover these identifiers it is easiest to list the controllers, this can be done using
the `MidiDeviceDescription.list()` function."""


    @Code
    application {
        program {
            MidiDeviceDescription.list().forEach {
                println("name: '${it.name}', vendor: '${it.vendor}', receiver:${it.receive}, transmitter:${it.transmit}")
            }
        }
    }

    @Text """From what this program outputs you can pick a controller by copying its name and vendor identifiers."""

    @Text "## Connecting to a MIDI controller"

    @Text """Once you have the controller name and vendor you can use `MidiTransceiver.fromDeviceVendor` to open the
midi controller. For example to use a Behringer BCR2000 controller on a Ubuntu system we can use the following."""

    @Code
    application {
        program {
            val controller = MidiTransceiver.fromDeviceVendor("BCR2000 [hw:2,0,0]", "ALSA (http://www.alsa-project.org)")
        }
    }

    @Text """## Listening to the controller"""
    @Text """Once connected to a controller we can start listening to the MIDI events it sends out. The orx-midi library
supports controller change, note on and note off events."""

    @Code
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

    @Text """## Talking to the controller"""
    @Text """MIDI controllers can often react to data received from 
software. A common use case with MIDI controllers with endless rotary
encoders is setting up initial values for the encoders when the program 
launches. Those values are then reflected in LED lights or in a display 
in the controller.
"""

    @Code
    application {
        program {
            val controller = MidiTransceiver.fromDeviceVendor("BCR2000 [hw:2,0,0]", "ALSA (http://www.alsa-project.org)")

            // send a control change
            controller.controlChange(channel = 1, control = 3, value = 42)

            // send a program change
            controller.programChange(channel = 2, program = 5)

            // send a note event
            controller.noteOn(channel = 3, key = 60, velocity = 100)

            // note: send velocity 0 to stop a note
        }
    }

}