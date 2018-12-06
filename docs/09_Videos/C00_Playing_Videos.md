
 # Playing videos #
 OPENRNDR comes with preliminary support for video playback. Currently the video playback is not suitable for
 resentation as support for sound, seeking and timing is missing. The video playback is suitable for video processing.
 # The openrndr-ffmpeg library
 The openrndr-ffmpeg library holds interfaces to an video player based on ffmpeg, as well as native
 fmpeg binaries (provided by the JavaCPP project). Adding openrndr-ffmpeg to the project is enough for full video support.
 Add the `openrndr-ffmpeg` library to the list of dependencies in `gradle.build`:
 ```
 dependencies {
    […]
    compile "org.openrndr:openrndr-ffmpeg:$openrndr_version"
    […]
}
 ```
 ## A simple video player

```kotlin
application {
    program {
        val videoPlayer = FFMPEGVideoPlayer.fromFile("data/video.mp4")
        
        extend {
            drawer.background(ColorRGBa.BLACK)
            videoPlayer.next()
            videoPlayer.draw(drawer)
        }
    }
}
```

## Video from camera devices

The `FFMPEGVideoPlayer` class can be used to get and display video data from camera devices. To open a camera device you use the `fromDevice()` method. When this method is called without any arguments it attempts to open the default camera device.

OPENRNDR is currently not able to enumerate camera devices and relies on the user to supply correct camera device names. The device name schemes are different for Linux, macOS and Windows.

Platform | Default device name
---------|--------------------
macOS    | 0
Linux    | /dev/video0
Windows  | video=Integrated Webcam


##### Relevant APIs
```
FFMPEGVideoPlayer.fromDevice(deviceName:String = defaultDevice(),
                             width:Int = -1, height:Int = -1, fps:Double = -1.0,
                             format:String = defaultFormat())
FFMPEGVideoPlayer.defaultDevice()
FFMPEGVideoPlayer.defaultFormat()
```


##### Examples

```kotlin
application {
    program {
        val videoPlayer = FFMPEGVideoPlayer.fromDevice()
        videoPlayer.start()
        extend {
            drawer.background(ColorRGBa.BLACK)
            videoPlayer.next()
            videoPlayer.draw(drawer)
        }
    }
}
```

## Processing video

Video playback can be combined with render targets and filters.

In the following example the video player output is blurred before presenting it on the screen.

```kotlin
application {
    program {
        val videoPlayer = FFMPEGVideoPlayer.fromFile("data/video.mp4")
        val blur = BoxBlur()
        val renderTarget = renderTarget(width, height) {
            colorBuffer()
        }
        
        extend {
            drawer.background(ColorRGBa.BLACK)
            // -- draw the video on the render target
            drawer.withTarget(renderTarget) {
                videoPlayer.next()
                videoPlayer.draw(drawer)
            }
            // -- apply a blur on the render target's color attachment
            blur.apply(renderTarget.colorBuffer(0), renderTarget.colorBuffer(0))
            // -- draw the blurred color attachement
            drawer.image(renderTarget.colorBuffer(0))
        }
    }
}
```
