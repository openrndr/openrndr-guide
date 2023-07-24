@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Writing to video files")
@file:ParentTitle("Videos")
@file:Order("110")
@file:URL("videos/writingToVideoFiles")

package docs.`09_Videos`

import org.openrndr.KEY_ESCAPE
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.ffmpeg.VideoWriter

fun main() {

    @Text
    """
    # Writing to video files

    ## Prerequisites

    Make sure ffmpeg is installed on your system.

    ## Writing to video using render targets
    """

    @Code
    application {
        program {
            val videoWriter = VideoWriter()
            videoWriter.size(width, height)
            videoWriter.output("output.mp4")
            videoWriter.start()

            val videoTarget = renderTarget(width, height) {
                colorBuffer()
                depthBuffer()
            }


            var frame = 0
            extend {
                drawer.isolatedWithTarget(videoTarget) {
                    clear(ColorRGBa.BLACK)
                    rectangle(40.0 + frame, 40.0, 100.0, 100.0)
                }

                videoWriter.frame(videoTarget.colorBuffer(0))
                drawer.image(videoTarget.colorBuffer(0))
                frame++
                if (frame == 100) {
                    videoWriter.stop()
                    application.exit()
                }
            }
        }
    }


    @Text
    """
    ## The `ScreenRecorder` extension

    A much simpler way of writing your program's output to video is
    offered by the `ScreenRecorder` extension. The extension creates video 
    files named after your Program class name plus the date. 
    For example: `MyProgram-2018-04-11-11.31.03.mp4`. The video files 
    are located in the `video/` directory in your project.

    To setup the screen recorder do the following:
    """

    @Code
    application {
        program {
            extend(ScreenRecorder())
            extend {
                // -- whatever you draw here ends up in the video
            }
        }
    }

    @Text
    """
    ### Toggle the `ScreenRecorder` on and off
    
    By default the screen recorder extension adds video frames as long as the
    program runs. If you keep running the program again and again you will end
    up with a folder full of video files.
    
    We can make it more flexible by letting the user start and pause the
    recorder, for instance by pressing the `v` key on the keyboard.
    """

    @Code
    application {
        program {
            // keep a reference to the recorder so we can start it and stop it.
            val recorder = ScreenRecorder().apply {
                outputToVideo = false
            }
            extend(recorder)
            extend {
                // -- draw things here
            }
            keyboard.keyDown.listen {
                when {
                    it.key == KEY_ESCAPE -> program.application.exit()
                    it.name == "v" -> {
                        recorder.outputToVideo = !recorder.outputToVideo
                        println(if (recorder.outputToVideo) "Recording" else "Paused")
                    }
                }
            }
        }
    }

    @Text
    """
    ## Video formats
    
    By default videos are recorded in h264 format with a `.mp4` file extension,
    but animated `gif` or `webp`, `png` or `tif` sequences, `prores` and `h265` 
    are also available by enabling the 
    [orx-video-profiles](https://github.com/openrndr/orx/tree/master/orx-jvm/orx-video-profiles)
    extension.
        
    """
}