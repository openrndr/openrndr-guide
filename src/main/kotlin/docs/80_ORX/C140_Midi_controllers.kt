@file:Suppress("UNUSED_EXPRESSION")
@file:Title("MIDI controllers")
@file:ParentTitle("ORX")
@file:Order("140")
@file:URL("ORX/midiControllers")

package docs.`80_ORX`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.midi.*
import org.openrndr.extra.parameters.ColorParameter
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.math.Vector2

fun main() {

    @Text """
    # Midi controllers with orx-midi
    
    The [`orx-midi`](https://github.com/openrndr/orx/blob/master/orx-jvm/orx-midi) 
    library provides a simple interface to interact with MIDI controllers. 

    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-midi` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    ## Listing MIDI controllers
    
    To connect to a MIDI controller you will need its device name which can be 
    discovered by calling the `listMidiDevices()` function.
    """

    @Code
    application {
        program {
            listMidiDevices().forEach {
                println("name: '${it.name}', vendor: '${it.vendor}', receiver:${it.receive}, transmitter:${it.transmit}")
            }
        }
    }

    @Text
    """
    
    ## Connecting to a MIDI controller
    
    Once you have the controller name you can use 
    `openMidiDevice` to connect to the MIDI controller. 
    For example to use a Behringer BCR2000 controller on a Ubuntu system we 
    can use the following.
    """

    @Code
    application {
        program {
            val controller = openMidiDevice("BCR2000")
        }
    }

    @Text
    """
    Tip: request `BCR2000` instead of `BCR2000 [hw:2,0,0]` so your program continues
    to work after plugging your controller into a different USB port.
    Caveat: do specify the full name if connecting multiple controllers of the same brand and model.
    
    ## Listening to the controller
    
    Once connected to a controller we can start listening to the MIDI events 
    it sends out. The orx-midi library supports six types of MIDI events.
    """

    @Code
    application {
        program {
            val controller = openMidiDevice("BCR2000 [hw:2,0,0]")

            controller.controlChanged.listen {
                println("[control change] channel: ${it.channel}, control: ${it.control}, value: ${it.value}")
            }
            controller.noteOn.listen {
                println("[note on] channel: ${it.channel}, key: ${it.note}, velocity: ${it.velocity}")
            }
            controller.noteOff.listen {
                println("[note off] channel: ${it.channel}, key: ${it.note},")
            }
            controller.channelPressure.listen {
                println("[channel pressure] channel: ${it.channel}, pressure: ${it.pressure}")
            }
            controller.pitchBend.listen {
                println("[pitch bend] channel: ${it.channel}, pitch: ${it.pitchBend}")
            }
            controller.programChanged.listen {
                println("[program change] channel: ${it.channel}, program: ${it.program}")
            }
        }
    }

    @Text
    """
    ## Talking to the controller
    
    MIDI controllers can often react to data received from 
    software. A common use case with MIDI controllers with endless rotary
    encoders is setting up initial values for the encoders when the program 
    launches. Those values are then reflected in LED lights or in a display 
    in the controller.
    """

    @Code
    application {
        program {
            val controller = openMidiDevice("BCR2000")

            // send a control change
            controller.controlChange(channel = 1, control = 3, value = 42)

            // send a program change
            controller.programChange(channel = 2, program = 5)

            // send a note on event
            controller.noteOn(channel = 3, key = 60, velocity = 100)

            // send a note off event
            controller.noteOn(channel = 3, key = 60, velocity = 0)

            // send a pitch bend event
            controller.pitchBend(channel = 4, 50)

            // send a channel pressure event
            controller.channelPressure(channel = 4, 100)
        }
    }

    @Text
    """
    ## MIDI console
    
    For debugging purposes one can visualize all the MIDI events by using the `MidiConsole`.        
    """

    @Code
    application {
        program {
            val controller = openMidiDevice("Launchpad [hw:4,0,0]")

            extend(MidiConsole()) {
                register(controller)
            }
        }
    }

    @Text
    """
    ## Variable binding
    
    One can easily bind MIDI controller inputs like knobs and sliders to program variables.
    In the following example 7 inputs control the radius, position 
    and color of a circle.
    
    Note that `ColorParameter` binds four consecutive inputs (red, green, blue and alpha).
    
    [More about Parameter Annotations](/ORX/quickUIs.html#parameter-annotations). 
    """

    @Code
    application {
        program {
            val controller = openMidiDevice("Launchpad [hw:4,0,0]")

            val settings = object {
                @DoubleParameter("radius", 0.0, 100.0)
                var radius = 0.0

                @DoubleParameter("x", -100.0, 100.0)
                var x = 0.0

                @DoubleParameter("y", -100.0, 100.0)
                var y = 0.0

                @ColorParameter("fill")
                var color = ColorRGBa.WHITE
            }

            bindMidiControl(settings::radius, controller, channel = 0, control = 1)
            bindMidiControl(settings::x, controller, 0, 2)
            bindMidiControl(settings::y, controller, 0, 3)
            bindMidiControl(settings::color, controller, 0, 4)

            extend {
                drawer.fill = settings.color
                drawer.circle(drawer.bounds.center + Vector2(settings.x, settings.y), settings.radius)
            }
        }
    }
}