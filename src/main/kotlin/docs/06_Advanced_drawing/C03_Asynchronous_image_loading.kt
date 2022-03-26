@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Asynchronous image loading")
@file:ParentTitle("Advanced drawing")
@file:Order("130")
@file:URL("advancedDrawing/asynchronousImageLoading")

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.internal.colorBufferLoader

fun main() {

    @Text 
    """
    # Asynchronous image loading
    
    In scenarios in which images are required to be loaded without blocking 
    the draw thread you can use `ColorBufferLoader`. This loader runs on its 
    own thread and uses shared contexts of the underlying graphics API (OpenGL).

    `ColorBufferLoader` maintains a prioritized loading queue, such that most 
    recently touched images will be loaded first.

    `ColorBufferLoader` maintains an unloading queue, such that image proxies 
    that have not been touched for 5000ms will be unloaded automatically 
    (unless the proxy is requested to be persistent).

    The `ColorBufferLoader` loads URL strings, it supports loading from file 
    and http/https.
    """

    application {
        @Code
        program {
            extend {
                val proxy = colorBufferLoader.loadFromUrl("https://avatars3.githubusercontent.com/u/31103334?s=200&v=4")
                proxy.colorBuffer?.let {
                    drawer.image(it)
                }
            }
        }
    }


    @Text 
    """
    Here we see that `fromUrl` can safely be called many times as 
    `ColorBufferLoader` caches these requests.
    Accessing `proxy.colorBuffer` touches its internal timestamp and queues 
    the image for loading if the proxy state is `NOT_LOADED`.

    ## Handling errors
    
    Sometimes loading fails; for example because the file was not found, 
    or the http connection timed out.
    `ColorBufferLoader` signals such errors by placing `ColorBufferProxy` 
    in `RETRY` state. The proxy can be taken out of
    this state by calling its `.retry()` function.

    Alternatively some errors cannot be retried; these will be signalled 
    by setting the proxy state to `ERROR`. Such errors
    should only occur if the loading of the image causes a non-IOException 
    to be thrown. 

    ## Cancelling a queued image
    
    Cancelling an image which is queued (and is thus in `QUEUED` state) for 
    loading is done by calling `.cancel()` on the `ColorBufferProxy`. 
    This will set the proxy state back to `NOT_LOADED`.
    """
}