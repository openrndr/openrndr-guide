package org.openrndr.dokgen.sourceprocessor

import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SourceProcessorTest {

    private val source: String =
        SourceProcessorTest::class.java.getResource("/Sample.kt")!!.readText()

    private fun process() = SourceProcessor.process(
        source = source,
        packageDirective = "exported.pkg",
        mkLink = { index -> "https://example.com/Sample$index.kt" }
    )

    @Test
    fun `extracts file annotations`() {
        val output = process()
        assertEquals("Sample Page", output.annotations["Title"])
        assertEquals("Samples", output.annotations["ParentTitle"])
        assertEquals("100", output.annotations["Order"])
        assertEquals("samples/samplePage", output.annotations["URL"])
    }

    @Test
    fun `generates one source per @Application`() {
        val output = process()
        assertEquals(2, output.appSources.size, "two @Application blocks expected")
        assertEquals(2, output.appSourcesForExport.size)
    }

    @Test
    fun `collects media links in source order`() {
        val output = process()
        assertEquals(listOf("media/image-001.png", "media/video-001.mp4"), output.media)
    }

    @Test
    fun `renders markdown, code, media and a single example link`() {
        val doc = process().doc

        // @Text becomes markdown
        assertContains(doc, "# Heading")
        assertContains(doc, "Intro paragraph.")
        assertContains(doc, "## Code block")

        // @Media renders image and video tags
        assertContains(doc, """<img alt="media/image-001.png" src="media/image-001.png" loading="lazy">""")
        assertContains(doc, """<source src="media/video-001.mp4" type="video/mp4">""")

        // @Application + @Code renders the application as a runnable `main`
        assertContains(doc, "fun main() = application {")

        // The full-example link is added once, for the first (index 0) application.
        assertContains(doc, "[Link to the full example](https://example.com/Sample0.kt)")
        assertEquals(
            1,
            Regex("Link to the full example").findAll(doc).count(),
            "only @Code blocks inside an @Application get a link"
        )
        assertFalse(doc.contains("Sample1.kt"), "the no-@Code application must not get a link")

        // dokgen annotations and @Exclude blocks are stripped from the documentation
        assertFalse(doc.contains("@Exclude"))
        assertFalse(doc.contains("@Code"))
        assertFalse(doc.contains("@Application"))
        assertFalse(doc.contains("@ProduceScreenshot"))
        assertFalse(doc.contains("width = 640"), "@Exclude configure block must be dropped from docs")

        // @Code.Block unwraps `run {}`, keeps the comment and preserves verbatim formatting
        assertContains(doc, "// a comment")
        assertContains(doc, "val r = Rectangle(\n    0.0,")
        assertFalse(doc.contains("run {"), "@Code.Block must unwrap the run block")

        // Nested @Code extend (no @Application) is shown without a link
        assertContains(doc, "drawer.clear(ColorRGBa.BLACK)")
    }

    @Test
    fun `runnable screenshot source sets system properties and keeps the configure block`() {
        val runnable = process().appSources[0]

        assertContains(runnable, "package examples.samples")
        assertContains(runnable, "fun main() {")
        assertContains(runnable, """System.setProperty("screenshot_location", "media/image-001.png")""")
        assertContains(runnable, """System.clearProperty("screenshot_multiSample")""")

        // @Exclude blocks are KEPT in runnable sources so media is produced at the right size,
        // with their indentation intact (regression: orphan indentation whitespace).
        assertContains(runnable, "    application {\n        configure {\n            width = 640")

        // annotations and the dokgen import are removed
        assertFalse(runnable.contains("@ProduceScreenshot"))
        assertFalse(runnable.contains("@Exclude"))
        assertFalse(runnable.contains("@Application"))
        assertFalse(runnable.contains("@Code"))
        assertFalse(runnable.contains("org.openrndr.dokgen"))
        assertContains(runnable, "import org.openrndr.application")
    }

    @Test
    fun `runnable video source maps all produce-video arguments`() {
        val runnable = process().appSources[1]
        assertContains(runnable, """System.setProperty("video_location", "media/video-001.mp4")""")
        assertContains(runnable, """System.setProperty("video_duration", "5.0")""")
        assertContains(runnable, """System.clearProperty("video_frameRate")""")
        assertContains(runnable, """System.clearProperty("video_multiSample")""")
    }

    @Test
    fun `exported source uses given package and drops excluded blocks`() {
        val exported = process().appSourcesForExport[0]

        assertContains(exported, "package exported.pkg")
        assertContains(exported, "fun main() {")
        assertContains(exported, "drawer.rectangle(")

        // @Exclude blocks are removed from exported examples
        assertFalse(exported.contains("configure {"))
        assertFalse(exported.contains("width = 640"))

        // Removing the @Exclude block leaves clean, correct indentation
        // (regression: orphan indentation whitespace merged into the next line).
        assertContains(exported, "    application {\n        program {\n            extend {")

        // no annotations, no media setup, no dokgen import
        assertFalse(exported.contains("@"))
        assertFalse(exported.contains("System.setProperty"))
        assertFalse(exported.contains("org.openrndr.dokgen"))
    }

    @Test
    fun `preserves verbatim multi-line formatting in exported sources`() {
        val exported = process().appSourcesForExport[0]
        // The multi-line Rectangle(...) call is kept as written, not collapsed.
        assertContains(exported, "Rectangle(\n")
    }
}
