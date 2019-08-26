``
# orx-kinect

Provides Kinect support (only Kinect version 1 at the moment and only depth camera).
Source and extra documentation can be found in the [orx sourcetree](https://github.com/openrndr/orx/tree/master)

Note: the support is split into several modules:

* `orx-kinect-common`
* `orx-kinect-v1`
* [`orx-kinect-v1-demo`](https://github.com/openrndr/orx/tree/master/orx-kinect-v1-demo/src/main/kotlin)
* `orx-kinect-v1-natives-linux-x64`
* `orx-kinect-v1-natives-macos`
* `orx-kinect-v1-natives-windows`

## Adding kinect support to the project

Additional dependencies needed in `build.gradle`:

```groovy
dependencies {
    // ...
    compile "org.openrndr.extra:orx-kinect-v1:$orxVersion"
    runtime "org.openrndr.extra:orx-kinect-v1-natives-$openrndrOs:$orxVersion"
}
```

In case of using `build.gradle.kts`:

```groovy
dependencies {
    // ...
    compile("org.openrndr.extra:orx-kinect-v1:$orxVersion")
    runtime("org.openrndr.extra:orx-kinect-v1-natives-$openrndrOs:$orxVersion")
}
```

## Using Kinect depth camera

Here is the simplest example of using Kinect depth camera:

```kotlin
import org.openrndr.application
import org.openrndr.extra.kinect.v1.getKinectsV1

fun main() = application {
    configure { // default resolution of the Kinect v1 depth camera
        width = 640
        height = 480
    }
    program {
        val kinects = getKinectsV1(this)
        val kinect = kinects.startDevice()
        kinect.depthCamera.enabled = true
        extend {
            drawer.image(kinect.depthCamera.currentFrame)
        }
    }
}
```

Note: depth values are mapped into `0-1` range and stored on a `ColorBuffer` containing only
RED color channel.

## Mirroring depth camera image

```kotlin
kinect.depthCamera.mirror = true
```

## Using multiple Kinects

The `kinects.startDevice()` can be supplied with device number (`0` by default):

```kotlin
import org.openrndr.application
import org.openrndr.extra.kinect.v1.getKinectsV1

fun main() = application {
    configure {
        width  = 640 * 2
        height = 480
    }
    program {
        val kinects = getKinectsV1(this)
        val depthCamera1 = kinects.startDevice(0).depthCamera
        val depthCamera2 = kinects.startDevice(1).depthCamera
        depthCamera1.enabled = true
        depthCamera2.enabled = true
        extend {
            drawer.image(depthCamera1.currentFrame)
            drawer.image(depthCamera2.currentFrame, depthCamera1.width.toDouble(), 0.0)
        }
    }
}

```

## Reacting only to the latest frame from the Kinect camera

Kinect is producing 30 frames per second, while screen refresh rates are usually higher.
Usually, if the data from the depth camera is processed, it is desired to react to the latest
Kinect frame only once:

```kotlin
kinect.depthCamera.getLatestFrame()?.let { frame ->
    myFilter.apply(frame, outputColorBuffer)
}
```

## Using color map filters

Raw kinect depth data might be visualized in several ways, the following filters are included:

* `DepthToGrayscaleMapper`
* `DepthToColorsZucconi6Mapper` - [Colors of natural light dispersion](https://www.alanzucconi.com/2017/07/15/improving-the-rainbow/) by Alan Zucconi
* `DepthToColorsTurboMapper` - [Turbo, An Improved Rainbow Colormap for Visualization](https://ai.googleblog.com/2019/08/turbo-improved-rainbow-colormap-for.html) by Google

An example presenting these filters side by side:

```kotlin
package org.openrndr.extra.kinect.v1.demo

import org.openrndr.application
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.ColorFormat
import org.openrndr.draw.colorBuffer
import org.openrndr.extra.kinect.*
import org.openrndr.extra.kinect.v1.getKinectsV1

fun main() = application {
    configure {
        width =  2 * 640
        height = 2 * 480
    }
    program {
        val kinects = getKinectsV1(this)
        val kinect = kinects.startDevice()
        kinect.depthCamera.enabled = true
        kinect.depthCamera.mirror = true
        val camera = kinect.depthCamera
        val grayscaleFilter = DepthToGrayscaleMapper()
        val zucconiFilter = DepthToColorsZucconi6Mapper()
        val turboFilter = DepthToColorsTurboMapper()
        val grayscaleBuffer = kinectColorBuffer(camera)
        val zucconiBuffer = kinectColorBuffer(camera)
        val turboBuffer = kinectColorBuffer(camera)
        extend {
            kinect.depthCamera.getLatestFrame()?.let { frame ->
                grayscaleFilter.apply(frame, grayscaleBuffer)
                zucconiFilter.apply(frame, zucconiBuffer)
                turboFilter.apply(frame, turboBuffer)
            }
            drawer.image(camera.currentFrame)
            drawer.image(grayscaleBuffer, camera.width.toDouble(), 0.0)
            drawer.image(turboBuffer, 0.0, camera.height.toDouble())
            drawer.image(zucconiBuffer, camera.width.toDouble(), camera.height.toDouble())
        }
    }
}

private fun kinectColorBuffer(camera: KinectCamera): ColorBuffer {
    return colorBuffer(camera.width, camera.height, format = ColorFormat.RGB)
}
```

## Executing native freenect commands

This kinect support is built on top of the [freenect](https://github.com/OpenKinect/libfreenect)
library. Even though the access to freenect is abstracted, it is still possible to execute
low level freenect commands in the native API:

```kotlin
kinects.execute { ctx -> freenect_set_log_level(ctx.fnCtx, freenect.FREENECT_LOG_FLOOD) }
```

And in the scope of particular device:

```kotlin
kinect.execute { ctx -> freenect_set_led(ctx.fnDev, LED_BLINK_RED_YELLOW) }
```
