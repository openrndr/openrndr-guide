@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Concurrency and multithreading")
@file:ParentTitle("Drawing")
@file:Order("360")
@file:URL("drawing/concurrencyAndMultithreading")

package docs.`04_Drawing`

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.*
import org.openrndr.internal.finish
import org.openrndr.launch
import java.nio.ByteBuffer
import kotlin.random.Random

fun main() {
    @Text 
    """
    # Concurrency and multi-threading
    
    Here we talk about OPENRNDR's primitives for concurrency.
    
    The largest complication in multi-threading in OPENRNDR lies in how the 
    underlying graphics API (OpenGL)
    can only be used from threads on which an OpenGL context is active. 
    This means that any interactions with `Drawer`,
    `ColorBuffer`, `VertexBuffer`, `RenderTarget`, `Shader`, `BufferTexture`, 
    `CubeMap`, `ArrayTexture` can only be 
    performed on the primary draw thread or specially created draw threads. 

    ## Coroutines
    
    Coroutines, as they are discussed here are a Kotlin specific framework for 
    concurrency. Please read the 
    [coroutines overview](https://kotlinlang.org/docs/reference/coroutines-overview.html) 
    in the Kotlin reference for an introduction to coroutines
    
    `Program` comes with its own coroutine dispatcher, which guarantees that 
    coroutines will be handled on the 
    primary draw thread. This means that coroutines when executed or resumed 
    by the program dispatcher will block the draw thread.

    In the following example we launch a coroutine that slowly counts to 99. 
    Note that the delay inside the 
    coroutine does _not_ block the primary draw thread.
    """

    @Code
    application {
        program {
            var once = true
            extend {
                if (once) {
                    once = false
                    launch {
                        for (i in 0 until 100) {
                            println("Hello from coroutine world ($i)")
                            delay(100)
                        }
                    }
                }
            }
        }
    }

    @Text 
    """
    You may be asking, what is the purpose of the `Program` dispatcher if 
    running coroutines blocks the primary
    draw thread. The answer is, blocking coroutines are useful when the work 
    performed is light. Light work includes 
    waiting for (off-thread) coroutines to complete and using the results to 
    write to graphics resources.
    
    In the below example we nest coroutines; the outer one is launched on the 
    `Program` dispatcher, the inner
    one is launched on the `GlobalScope` dispatcher. The `GlobalScope` 
    dispatcher executes the coroutine on a thread
    (from a thread pool) such that it does not block the primary draw thread. 
    By using `.join()` on the inner coroutine
    we wait for it to complete, waiting is non-blocking (thanks to coroutine 
    magic!). Once the join operation completes 
    we can write the results to a graphics resource on the primary draw thread.
     """

    @Code
    application {
        program {
            val colorBuffer = colorBuffer(512, 512)
            val data = ByteBuffer.allocateDirect(512 * 512 * 4)
            var once = true
            extend {
                if (once) {
                    once = false
                    launch {
                        // -- launch on GlobalScope
                        // -- this will cause the coroutine to be executed off-thread.
                        GlobalScope.launch {
                            // -- perform some faux-heavy calculations
                            val r = Random(100)
                            for (y in 0 until 512) {
                                for (x in 0 until 512) {
                                    for (c in 0 until 4) {
                                        data.put(r.nextBytes(1)[0])
                                    }
                                }
                            }
                        }.join() // -- wait for coroutine to complete

                        // -- write data to graphics resources
                        data.rewind()
                        colorBuffer.write(data)
                    }
                }
            }
        }
    }

    @Text 
    """
    ## Secondary draw threads
    
    In some scenarios you may want to have a separate thread on which all 
    graphic resources can be used and
    drawing is allowed. In those cases you use `drawThread`.
    
    Most graphic resources can be used and shared between threads, with the 
    exception of the `RenderTarget`.
    
    In the next example we create a secondary draw thread and a `ColorBuffer` 
    that is shared between the threads.
    On the secondary draw thread we create a `RenderTarget` with the color 
    buffer attachment. The image is made visible on the primary draw thread.
    """

    @Code
    application {
        program {
            val result = colorBuffer(512, 512)
            var once = true
            var done = false
            val secondary = drawThread()

            extend {
                if (once) {
                    once = false
                    // -- launch on the secondary draw thread (SDT)
                    secondary.launch {
                        // -- create a render target on the SDT.
                        val rt = renderTarget(512,512) {
                            colorBuffer(result)
                        }

                        // -- make sure we use the draw thread's drawer
                        val drawer= secondary.drawer
                        drawer.withTarget(rt) {
                            drawer.ortho(rt)
                            drawer.clear(ColorRGBa.PINK)
                        }

                        // -- destroy the render target
                        rt.destroy()
                        finish()
                        // -- tell main thread the work is done
                        done = true
                    }
                }
                // -- draw the result when the work is done
                if (done) {
                    drawer.image(result)
                }
            }
        }
    }
}
