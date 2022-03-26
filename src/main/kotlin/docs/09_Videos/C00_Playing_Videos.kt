@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Playing videos")
@file:ParentTitle("Videos")
@file:Order("100")
@file:URL("videos/playingVideos")

package docs.`09_Videos`

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
    
    * [VideoPlayerFFMPEG.fromFile](https://api.openrndr.org/org.openrndr.ffmpeg/-video-player-f-f-m-p-e-g/from-file.html)
    * [VideoPlayerFFMPEG.play](https://api.openrndr.org/org.openrndr.ffmpeg/-video-player-f-f-m-p-e-g/play.html)
    * [VideoPlayerFFMPEG.draw](https://api.openrndr.org/org.openrndr.ffmpeg/-video-player-f-f-m-p-e-g/draw.html)

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
    ## Video from camera devices

    The `VideoPlayerFFMPEG` class can be used to get and display video data 
    from camera devices. To open a camera device you use the `fromDevice()` 
    method. When this method is called without any arguments it attempts to 
    open the default camera device.

    `VideoPlayerFFMPEG` has minimal device listing capabilities. The device 
    names of available input devices can be listed using 
    [VideoPlayerFFMPEG.listDeviceNames](https://api.openrndr.org/org.openrndr.ffmpeg/-video-player-f-f-m-p-e-g/list-device-names.html).
    
    ##### Relevant APIs
    
    * [VideoPlayerFFMPEG.fromDevice](https://api.openrndr.org/org.openrndr.ffmpeg/-video-player-f-f-m-p-e-g/from-device.html)
    * [VideoPlayerFFMPEG.defaultDevice](https://api.openrndr.org/org.openrndr.ffmpeg/-video-player-f-f-m-p-e-g/default-device.html)
    
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




