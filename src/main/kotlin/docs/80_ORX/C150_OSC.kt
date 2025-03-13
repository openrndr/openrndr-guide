@file:Suppress("UNUSED_EXPRESSION")
@file:Title("OSC")
@file:ParentTitle("ORX")
@file:Order("150")
@file:URL("ORX/OSC")

package docs.`80_ORX`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.osc.OSC
import java.net.InetAddress

fun main() {
    @Text """
    # Handling OSC messages with orx-osc
    
    The [`orx-osc`](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-osc) 
    osc provides a simple interface to interact with OSC hosts and clients. 

    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-osc` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    ## Listening to OSC messages
    
    To listen to OSC messages we need to start an OSC server and use `listen` 
    function to install listeners
    """

    @Code
    application {
        program {
            val osc = OSC()
            osc.listen("/live/track/2") { addr, it ->
                // -- get the first value
                val firstValue = it[0] as Float
            }
            extend {

            }
        }
    }

    @Text """
    Note that `.listen()` accepts wildcard characters like `*` and `?`.
    For instance to listen to addresses containing two words we
    can use `osc.listen("/*/*")`. Find out more about **Pattern Matching**
    in the 
    [OSC specification](https://opensoundcontrol.stanford.edu/spec-1_0.html).

    ## Sending OSC messages
    """

    @Code
    application {
        program {
            val osc = OSC()

            extend {
                osc.send("/some/address", listOf(1.0f, 2.0f))
            }
        }
    }

    @Text """
    ## Specifying IP address and ports

    The default IP address for OSC is `localhost` and the in and out
    ports are both set to `57110` by default. One can specify different
    values like this:
    """

    @Code.Block
    run {
        val osc = OSC(
            InetAddress.getByName("192.168.0.105"),
            portIn = 10000,
            portOut = 12000
        )
    }
}
