package org.openrndr

import org.openrndr.draw.BufferMultisample
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.ffmpeg.h264

class Preload : ApplicationPreload() {
    private fun screenshots(
        path: String,
        skipFrames: Int,
        quitWhenDone: Boolean
    ) = SingleScreenshot().apply {
        val pMultiSample = System.getProperty("screenshot_multiSample", "0")
        multisample = if (pMultiSample != "0") {
            BufferMultisample.SampleCount(pMultiSample.toInt())
        } else {
            BufferMultisample.Disabled
        }
        outputFile = path
        quitAfterScreenshot = quitWhenDone
        delayFrames = skipFrames
    }

    override fun onProgramSetup(program: Program) {
        System.getProperty("video_location")?.let { path ->
            val pDuration = System.getProperty("video_duration", "10.0")
            val pFrameRate = System.getProperty("video_frameRate", "30")
            val pMultiSample = System.getProperty("video_multiSample", "0")
            val screenRecorder = ScreenRecorder().apply {
                quitAfterMaximum = true
                outputFile = path
                maximumDuration = pDuration.toDouble()
                multisample = if (pMultiSample != "0") {
                    BufferMultisample.SampleCount(pMultiSample.toInt())
                } else {
                    BufferMultisample.Disabled
                }
                frameRate = pFrameRate.toInt()
                h264 {
                    // Lower quality for smaller files. Default is 23.
                    constantRateFactor = 30
                }
            }
            program.extend(screenRecorder)

            val normScreenshotTime = 1 / 3.0
            val skipFrames = (screenRecorder.frameRate.toDouble() *
                    screenRecorder.maximumDuration * normScreenshotTime).toInt()
            val thumbPath = path.replace(".mp4", "-thumb.jpg")
            program.extend(screenshots(thumbPath, skipFrames, false))
        }

        System.getProperty("screenshot_location")?.let { path ->
            program.extend(screenshots(path, 0, true))
        }
    }
}
