@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Events")
@file:ParentTitle("Interaction")
@file:Order("90")
@file:URL("interaction/Events")

package docs.`07_Interaction`

import kotlinx.coroutines.delay
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.events.Event
import org.openrndr.extra.noise.Random
import org.openrndr.launch


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
        val timeEvent = Event<Int>("time-event")

        private var frame = 0

        fun update() {
            if (++frame % 60 == 0) {
                timeEvent.trigger(frame / 60)
            }
        }
    }

    @Text
    """
    ### Sending an event        
    
    Notice how events carry a payload, in this case `Int`. This is convenient
    because it allows us to transmit information together with the event.
    Mouse and Keyboard events contain details about the mouse position or 
    the key pressed. In this program we are free to choose any type, so lets
    just broadcasting a message containing the approximate time in seconds.
    
    Passing a name in the event constructor is not necessary, but can be
    useful for logging and debugging.
     
    Another thing to observe is that `timeEvent` is a public variable. If it
    was private we couldn't listen to it from outside 
    by calling `thing.timeEvent.listen { ... }`.    
    
    At some point in our program execution we need to call `.trigger()` 
    to broadcast the event. We can call it as many times as needed.
      
    ### Listening to an event
    
    The following small program shows how to listen to an event emitted by a class.
    
    First, let's create one instance of the class called `thing`.
     
    Next, listen to the event `thing` can emit (`timeEvent`).
    
    """.trimIndent()

    @Code
    application {
        program {
            val thing = Thing()
            extend {
                thing.update()
            }
            thing.timeEvent.listen {
                println("timeEvent triggered! $it")
            }
        }
    }

    @Text
    """
    We see a line appear every second: 
    ```
    timeEvent triggered! 1
    timeEvent triggered! 2
    timeEvent triggered! 3
    ...
    ```
    
    ## Events in coroutines and threads
    
    By default our OPENRNDR programs run in a single thread, which happens
    to be the "rendering thread". But what would happen if we sent
    Events from different threads or coroutines? Lets find out.
    
    The `Blob` class is a copy of `Thing` with three changes:
    
    1. To be able to spawn coroutines, we pass a `Program` in the constructor.
    2. We add a second event called `doneWaiting`. We use `Unit` as a type
    when we don't want to pass any useful data.
    3. When the class is constructed, we launch a coroutine to wait
    for 3 seconds, then trigger the new `doneWaiting` event.
    """.trimIndent()

    @Code
    class Blob(program: Program) {
        val timeEvent = Event<Int>("time-event")
        val doneWaiting = Event<Unit>("done-waiting")

        private var frame = 0

        fun update() {
            if (++frame % 60 == 0) {
                timeEvent.trigger(frame / 60)
            }
        }

        init {
            program.launch {
                delay(3000)
                doneWaiting.trigger(Unit)
            }
        }
    }

    @Text
    """
    Now lets use our `Blob` class in a new program
    that listens to its two events:
    """.trimIndent()

    @Code
    application {
        program {
            val blob = Blob(this)
            extend {
                blob.update()
            }
            blob.timeEvent.listen {
                println("timeEvent triggered! $it")
            }
            blob.doneWaiting.listen {
                println("done waiting")
            }
        }
    }

    @Text
    """
    ```
    timeEvent triggered! 1
    timeEvent triggered! 2
    timeEvent triggered! 3
    done waiting
    timeEvent triggered! 4
    ...
    ```
    """.trimIndent()

    @Text
    """
    Seems to work! right? There's one issue though:
    the `doneWaiting.listen` function does not run on the
    rendering thread. This would be the case for events
    triggered due to external causes (loading
    a file from the Internet and waiting for its completion 
    or an event coming from a hardware input device).
    
    This will become apparent when we fail to draw on our window:    
    """

    run {
        application {
            program {
                val blob = Blob(this)
                @Code.Block
                run {
                    blob.doneWaiting.listen {
                        drawer.clear(ColorRGBa.WHITE) // <-- will not work
                        println("done waiting")
                    }
                }
            }
        }
    }


    @Text
    """
    The solution is simple though: when constructing the `Event`, we
    set the `postpone` argument to true:
    """.trimIndent()

    @Code.Block
    run {
        val doneWaiting = Event<Unit>("done-waiting", postpone = true)
    }

    @Text
    """
    Now triggering the event no longer sends it immediately, but queues it.
    The second part of the solution is to actually deliver the queued events
    by calling `deliver()`.
    """.trimIndent()

    run {
        val doneWaiting = Event<Unit>("done-waiting", postpone = true)
        @Code.Block
        run {
            doneWaiting.deliver()
        }
    }

    @Text
    """
    It is essential to call `deliver()` from the rendering thread.
    Since `extend { }` executes in the rendering thread, and 
    `extend` calls `update()`, we can let `update()` call `deliver()`
    and everything will work nicely.
     
    This is the full program:
    """.trimIndent()

    @Code.Block
    run {
        class Blob(program: Program) {
            val timeEvent = Event<Int>("time-event")
            val doneWaiting = Event<Unit>("done-waiting", postpone = true)

            private var frame = 0

            fun update() {
                if (++frame % 60 == 0) {
                    timeEvent.trigger(frame / 60)
                }
                // Deliver any queued events
                doneWaiting.deliver()
            }

            init {
                program.launch {
                    delay(3000)
                    // Queue event outside the rendering thread
                    doneWaiting.trigger(Unit)
                }
            }
        }
    }

    @Code
    application {
        program {
            val blob = Blob(this)
            extend {
                blob.update()
            }
            blob.timeEvent.listen {
                println("timeEvent triggered! $it")
            }
            blob.doneWaiting.listen {
                // White flash when this event is received
                drawer.clear(ColorRGBa.WHITE)
                println("done waiting")
            }
        }
    }

}
