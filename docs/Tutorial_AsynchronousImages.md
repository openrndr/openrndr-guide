# Asynchronous images #

In scenarios in which images are required to be loaded without blocking the draw thread you can
use `ColorBufferLoader`. This loader runs on its own thread and uses shared contexts of the underlying graphics API
(OpenGL).

`ColorBufferLoader` maintains a prioritized loading queue, such that most recently touched images will be
loaded first.

`ColorBufferLoader` maintains an unloading queue, such that image proxies that have not been touched for 5000ms will be unloaded automatically (unless the proxy is requested to be persistent).

The `ColorBufferLoader` loads URL strings, it supports loading from file and http/https.

##### Relevant APIs

```kotlin
fun colorBufferLoader.fromUrl(url:String, persistent:Boolean)

val ColorBufferProxy.colorBuffer

fun Drawer.image(image: ColorBuffer, x: Double = 0.0, y: Double = 0.0)
fun Drawer.image(image: ColorBuffer, x: Double, y: Double, width: Double, height:Double)
fun Drawer.image(image: ColorBuffer, source: Rectangle, dest: Rectangle)
```

## Basic Usage

```kotlin

fun draw() {
    val proxy = colorBufferLoader.fromUrl("[.. some url ..]")
    if (proxy.colorBuffer != null) {
        drawer.image(proxy.colorBuffer)
    }
}

```

Here we see that `fromUrl` can safely be called many times as `ColorBufferLoader` caches these requests.

Accessing `proxy.colorBuffer` touches its internal timestamp and queues the image for loading if the proxy state is `NOT_LOADED`.

## Handling errors

Sometimes loading fails; for example because the file was not found, or the http connection
timed out. `ColorBufferLoader` signals such errors by placing `ColorBufferProxy` in `RETRY` state. The proxy can be taken out of this state
reby calling its `.retry()` function.

Alternatively some errors cannot be retried; these will be signalled by setting the proxy state tot `ERROR`. Such errors should only occur if the loading of the image causes a throw of a non-IOException.

