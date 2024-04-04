@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Video")
@file:ParentTitle("Drawing")
@file:Order("165")
@file:URL("drawing/video")

package docs.`30_Drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.renderTarget
import org.openrndr.extra.fx.blur.BoxBlur
import org.openrndr.ffmpeg.VideoPlayerFFMPEG

fun main() {
    @Text
    """
    # Playing videos
    
    OPENRNDR comes with FFMPEG-backed video support. 
    
    ## A simple video player

    ##### Relevant APIs
    
    * [VideoPlayerFFMPEG.fromFile](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-jvm/openrndr-ffmpeg/src/main/kotlin/org/openrndr/ffmpeg/VideoPlayerFFMPEG.kt#L350)
    * [VideoPlayerFFMPEG.play](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-jvm/openrndr-ffmpeg/src/main/kotlin/org/openrndr/ffmpeg/VideoPlayerFFMPEG.kt#L488)
    * [VideoPlayerFFMPEG.draw](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-jvm/openrndr-ffmpeg/src/main/kotlin/org/openrndr/ffmpeg/VideoPlayerFFMPEG.kt#L735)

    """

    @Code
    application {
        program {
            val videoPlayer = VideoPlayerFFMPEG.fromFile("data/video.mp4")
            videoPlayer.play()
            extend {
                drawer.clear(ColorRGBa.BLACK)
                videoPlayer.draw(drawer)
            }
        }
    }

    @Text
    """
    ## Looping a video
    
    """

    application {
        program {
            val videoPlayer = VideoPlayerFFMPEG.fromFile("data/video.mp4")
            videoPlayer.play()

            @Code.Block
            run {
                // Loop: restart when reaching the end
                videoPlayer.ended.listen {
                    videoPlayer.restart()
                }

                extend {
                    videoPlayer.draw(drawer)
                }
            }
        }
    }

    @Text
    """
    ## Video from camera devices

    The `VideoPlayerFFMPEG` class can be used to get and display video data 
    from camera devices. To open a camera device you use the `fromDevice()` 
    method. When this method is called without any arguments it attempts to 
    open the default camera device.

    `VideoPlayerFFMPEG` has minimal device listing capabilities. The device 
    names of available input devices can be listed using 
    [VideoPlayerFFMPEG.listDeviceNames](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-jvm/openrndr-ffmpeg/src/main/kotlin/org/openrndr/ffmpeg/VideoPlayerFFMPEG.kt#L241).
    
    ##### Relevant APIs
    
    * [VideoPlayerFFMPEG.fromDevice](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-jvm/openrndr-ffmpeg/src/main/kotlin/org/openrndr/ffmpeg/VideoPlayerFFMPEG.kt#L369)
    * [VideoPlayerFFMPEG.defaultDevice](https://github.com/openrndr/openrndr/blob/v0.4.0-rc.7/openrndr-jvm/openrndr-ffmpeg/src/main/kotlin/org/openrndr/ffmpeg/VideoPlayerFFMPEG.kt#L390)
    
    ##### Examples
    """

    @Code
    application {
        program {
            val videoPlayer = VideoPlayerFFMPEG.fromDevice()
            videoPlayer.play()
            extend {
                drawer.clear(ColorRGBa.BLACK)
                videoPlayer.draw(drawer)
            }
        }
    }

    @Text
    """
    ## Processing video

    Video playback can be combined with render targets and filters.

    In the following example the video player output is blurred before 
    presenting it on the screen.
    """

    @Code
    application {
        program {
            val videoPlayer = VideoPlayerFFMPEG.fromFile("data/video.mp4")
            val blur = BoxBlur()
            val renderTarget = renderTarget(width, height) {
                colorBuffer()
            }
            videoPlayer.play()

            extend {
                drawer.clear(ColorRGBa.BLACK)
                // -- draw the video on the render target
                drawer.withTarget(renderTarget) {
                    videoPlayer.draw(drawer)
                }
                // -- apply a blur on the render target's color attachment
                blur.apply(renderTarget.colorBuffer(0), renderTarget.colorBuffer(0))
                // -- draw the blurred color attachment
                drawer.image(renderTarget.colorBuffer(0))
            }
        }
    }
}




