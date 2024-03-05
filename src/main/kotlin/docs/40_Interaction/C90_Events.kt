@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Events")
@file:ParentTitle("Interaction")
@file:Order("90")
@file:URL("interaction/Events")

package docs.`07_Interaction`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.events.Event
import org.openrndr.extra.noise.Random


fun main() {
    @Text
    """
    # Events
    
    Events are notifications sent by a sender to one or more subscribers indicating 
    that an action has taken place.

    Mouse and keyboard events are among the most commonly used events in OPENRNDR. 
    Before we get to those in the next chapter, let's examine how to create 
    our own custom events.
    
    We will often use events in class instances, so let's create
    a simple class called `Thing`. This class will include an update method 
    that will emit an event every 60 times it's called. Anyone listening to this
    event will receive it.
    """

    @Code
    class Thing {
        val timeEvent = Event<Boolean>()

        private var frame = 0

        fun update() {
            if(++frame % 60 == 0) {
                timeEvent.trigger(Random.bool())
                timeEvent.deliver()
            }
        }
    }

    @Text
    """
    ### Sending an event        
    
    Notice how events carry a payload, in this case `Boolean`. This is convenient
    because it allows us to transmit information together with the event.
    Mouse and Keyboard events contain details about the mouse position or 
    the key pressed. In this program we are free to choose any type, so lets
    just broadcasting a message containing a random boolean value.
     
    Another thing to observe is that `timeEvent` is a public variable. If it
    was private we couldn't listen to it from outside 
    by calling `thing.timeEvent.listen { ... }`.    
    
    At some point in our program execution we need to call `.trigger()` 
    to queue an event. We can call it as many times as needed.
    
    Finally, we call `.deliver()` to deliver the queued events to those
    listening to them.
    
    ### Listening to an event
    
    The following small program shows how to listen to an event emitted by a class.
    
    First, let's create one instance of the class (`Thing` in this case).
     
    Next, listen to an event this instance can emit (`timeEvent` here).
    
    """.trimIndent()

    @Code
    application {
        program {
            val thing = Thing()
            extend {
                thing.update()
            }
            thing.timeEvent.listen {
                println("timeEvent triggered! It contains a: $it")
            }
        }
    }

    @Text
    """
    There are multiple approaches to including events in our class. For example, a
    class could have separate `loadStart` and `loadComplete` events, or instead,
    have just one `loadEvent` and have the payload describe if it was
    a `start` or a `complete` event. Having two separate events arguably produces 
    more readable code.
    """.trimIndent()
}
