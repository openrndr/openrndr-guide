package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.dokgen.annotations.Application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiTransceiver
import org.openrndr.extra.osc.OSC

fun main() {
    @Text "# Handling OSC messages with orx-osc"

    @Text """The [`orx-osc`](https://github.com/openrndr/orx/tree/master/orx-osc) osc provides a simple interface
to interact with OSC hosts and clients. 

The library is easily added to a [openrndr-template](https://github.com/openrndr/openrndr-template) 
project by adding `orx-osc` to the `orxFeatures` line in `build.gradle.kts`

```
orxFeatures = setOf("orx-osc")
```

"""
    @Text "## Listening to OSC messages"
    @Text """To listen to OSC messages we need to start an OSC server and use `listen` function to install listeners"""

    @Application
    @Code
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

    @Text "## Sending OSC messages"

    @Application
    @Code
    application {
        program {
            val osc = OSC()

            extend {
                osc.send("/some/address", listOf(1.0f, 2.0f))
            }
        }
    }

}