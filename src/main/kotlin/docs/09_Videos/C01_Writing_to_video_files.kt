@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Writing to video files")
@file:ParentTitle("Videos")
@file:Order("110")
@file:URL("videos/writingToVideoFiles")

package docs.`09_Videos`

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
    # Writing to video files #

    ## Prerequisites

    Add the `openrndr-ffmpeg` library to your project.
    Make sure ffmpeg is installed on your system.

    ## Writing to video using render targets
    """

    @Code
    application {
        program {
            val videoWriter =
                VideoWriter.create().size(width, height)
                    .output("output.mp4").start()

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
    ## The ScreenRecorder extension

    A much simpler way of writing your program's output to video is offered by the `ScreenRecorder` extension. The extension creates video files named after your Program class name plus the date. For example: `MyProgram-2018-04-11-11.31.03.mp4`. The video files are located in the current working directory.

    To setup the screen recorder you do the following:
    """

    @Code
    application {
        program {
            extend(ScreenRecorder())
            extend {
                // -- whatever you do here ends up in the video
            }
        }
    }
}