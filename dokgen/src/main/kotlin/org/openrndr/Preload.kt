package org.openrndr

import org.openrndr.draw.BufferMultisample
import org.openrndr.extensions.SingleScreenshot
import org.openrndr.ffmpeg.ScreenRecorder

class Preload : ApplicationPreload() {
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
            }
            program.extend(screenRecorder)
        }

        System.getProperty("screenshot_location")?.let { path ->
            val pMultiSample = System.getProperty("screenshot_multiSample", "0")
            val screenshots = SingleScreenshot().apply {
                multisample = if (pMultiSample != "0") {
                    BufferMultisample.SampleCount(pMultiSample.toInt())
                } else {
                    BufferMultisample.Disabled
                }
                outputFile = path
            }
            program.extend(screenshots)
        }
    }
}
