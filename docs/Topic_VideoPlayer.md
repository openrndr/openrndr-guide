# Playing videos #

OPENRNDR comes with preliminary support for video playback. Currently the video playback is not suitable for
presentation as support for sound, seeking and timing is missing. The video playback is suitable for video processing.
## The openrndr-ffmpeg library

The openrndr-ffmpeg library holds interfaces to an video player based on ffmpeg, as well as native
ffmpeg binaries (provided by the JavaCPP project). Adding openrndr-ffmpeg to the project is enough for full video support.

Add the openrndr-ffmpeg library to the list of dependencies.

When working with a Maven project add the folowing item to pom.xml:
```
<dependencies>
    […]
    <dependency>
        <artifact>openrndr-ffmpeg</artifact>
        <group>org.openrndr</group>
        <version>${openrndr_version}</version>
    </dependency>
    […]
</dependencies>
```

When working with a Gradle project add the following to gradle.build
```
dependencies {
    […]
    compile "org.openrndr:openrndr-ffmpeg:$openrndr_version"
    […]
}
```

## A simple video player

```kotlin
class Example: Program {

    lateinit var videoPlayer: FFMPEGVideoPlayer;

    override fun setup() {
        videoPlayer = VideoPlayer.fromUrl("file:data/video.mp4")
    }

    override fun draw() {
        drawer.background(ColorRGBa.BLACK);
        videoPlayer.next()
        videoPlayer.draw(drawer)
    }
}

fun main(args:Array<String>) {
    Application.run(Example(), Configuration())
}
```

## Processing video

Video playback can be combined with render targets and filters.

In the following example the video player output is blurred before presenting it on the screen.
```kotlin
class Example: Program {

    lateinit var videoPlayer: FFMPEGVideoPlayer;
    lateinit var blur: BoxBlur;
    lateinit var renderTarget: RenderTarget;

    override fun setup() {
        renderTarget = renderTarget(width, height) { colorBuffer(width, height) }
        videoPlayer = VideoPlayer.fromUrl("file:data/video.mp4")
    }

    override fun draw() {
        drawer.background(ColorRGBa.BLACK);
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

fun main(args:Array<String>) {
    Application.run(Example(), Configuration())
}
```
