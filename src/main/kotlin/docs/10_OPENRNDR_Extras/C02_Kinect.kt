@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Kinect")
@file:ParentTitle("OPENRNDR Extras")
@file:Order("120")
@file:URL("OPENRNDRExtras/kinect")

package docs.`10_OPENRNDR_Extras`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.ColorFormat
import org.openrndr.draw.colorBuffer
import org.openrndr.extra.kinect.DepthToColorsTurboMapper
import org.openrndr.extra.kinect.DepthToColorsZucconi6Mapper
import org.openrndr.extra.kinect.DepthToGrayscaleMapper
import org.openrndr.extra.kinect.KinectCamera
import org.openrndr.extra.kinect.v1.getKinectsV1

fun main() {
    @Text """
    # orx-kinect
    
    Provides Kinect support (only Kinect version 1 at the moment and 
    only depth camera).
    Source and extra documentation can be found in the 
    [orx sourcetree](https://github.com/openrndr/orx/tree/master)

    Note: the support is split into several modules:

    * `orx-kinect-common`
    * `orx-kinect-v1`
    * [`orx-kinect-v1-demo`](https://github.com/openrndr/orx/tree/master/orx-kinect-v1-demo/src/main/kotlin)
    * `orx-kinect-v1-natives-linux-x64`
    * `orx-kinect-v1-natives-macos`
    * `orx-kinect-v1-natives-windows`
    
    ## Prerequisites
    
    Assuming you are working on an 
    [`openrndr-template`](https://github.com/openrndr/openrndr-template) based
    project, all you have to do is enable `orx-kinect-v1` in the `orxFeatures`
    set in `build.gradle.kts` and reimport the gradle project.
    
    ## Using the Kinect depth camera
    """

    @Code
    application {
        configure {
            // default resolution of the Kinect v1 depth camera
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

    @Text 
    """
    Note: depth values are mapped into `0-1` range and stored on a 
    `ColorBuffer` containing only RED color channel.
    
    ## Mirroring depth camera image
    
    ```kotlin 
    kinect.depthCamera.mirror = true 
    ```
    
    ## Using multiple Kinects
    
    The `kinects.startDevice()` can be supplied with device number 
    (`0` by default):
    """

    @Code
    application {
        configure {
            width = 640 * 2
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

    @Text 
    """
    ## Reacting only to the latest frame from the Kinect camera
    
    Kinect is producing 30 frames per second, while screen refresh rates 
    are usually higher.
    Usually, if the data from the depth camera is processed, it is desired 
    to react to the latest Kinect frame only once:

    ```kotlin
    kinect.depthCamera.getLatestFrame()?.let { frame ->
        myFilter.apply(frame, outputColorBuffer)
    }
    ```
    
    ## Using color map filters

    Raw kinect depth data might be visualized in several ways, the following 
    filters are included:

    * `DepthToGrayscaleMapper`
    * `DepthToColorsZucconi6Mapper` - [Colors of natural light dispersion](https://www.alanzucconi.com/2017/07/15/improving-the-rainbow/) by Alan Zucconi
    * `DepthToColorsTurboMapper` - [Turbo, An Improved Rainbow Colormap for Visualization](https://ai.googleblog.com/2019/08/turbo-improved-rainbow-colormap-for.html) by Google

    An example presenting these filters side by side:

    ![kinect-colormaps](media/kinect-colormaps.png)
    """

    @Code.Block
    run {
        fun kinectColorBuffer(camera: KinectCamera): ColorBuffer {
            return colorBuffer(camera.width, camera.height, format = ColorFormat.RGB)
        }
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
    }

    @Text 
    """
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
    """
}