# Writing to video files #

## Prerequisites

Add the `openrndr-ffmpeg` library to your project.
Make sure ffmpeg is installed on your system.

## Writing to video using render targets

```kotlin
lateinit var videoWriter: VideoWriter
lateinit var videoTarget: RenderTarget

fun setup() {
    videoWriter = VideoWriter().width(width).height(height).output("output.mp4").start()
    videoTarget = renderTarget(width, height) {
        colorBuffer()
        depthBuffer()
    }
}

var frame = 0
public void draw() {
    videoTarget.bind()
    drawer.background(ColorRGBa.RED)
    drawer.rectangle(40 + frame, 40, 100,1 00 );
    videoTarget.unbind()
    videoWriter.frame(renderTarget.colorBuffer(0))

    // also draw the result to the screen
    drawer.image(renderTarget.colorBuffer(0))
    frame++
    if (frame == 100) {
        videoWriter.stop()
        quit()
    }
}
```

## The ScreenRecorder extension

A much simpler way of writing your program's output to video is offered by the `ScreenRecorder` extension. The extension creates video files named after your Program class name plus the date. For example: `MyProgram-2018-04-11-11.31.03.mp4`. The video files are located in the current working directory.

To setup the screen recorder you do the following:
```kotlin
fun setup() {
    extend(ScreenRecorder())
}

fun draw() {
     // -- whatever you do here ends up in the video
}
```
