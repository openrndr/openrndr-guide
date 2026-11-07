package org.openrndr.dokgen

import org.junit.jupiter.api.Test
import org.openrndr.dokgen.sourceprocessor.mediaUrl
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GuideLocationTest {

    @Test
    fun `camel case keeps acronyms`() {
        assertEquals("renderTargets", GuideLocation.camelCase("Render_targets"))
        assertEquals("kotlinLanguageAndTools", GuideLocation.camelCase("Kotlin_language_and_tools"))
        assertEquals("ORX", GuideLocation.camelCase("ORX"))
        assertEquals("OSC", GuideLocation.camelCase("OSC"))
        assertEquals("fileIO", GuideLocation.camelCase("FileIO"))
        assertEquals("macOSAndWindowsTips", GuideLocation.camelCase("MacOSAndWindowsTips"))
        assertEquals("lowLevelDrawing", GuideLocation.camelCase("Low-level_drawing"))
        assertEquals("drawingSVG", GuideLocation.camelCase("Drawing_SVG"))
    }

    @Test
    fun `page in a section`() {
        val loc = GuideLocation.of("30_Drawing/C305_Render_targets.kt")
        assertEquals("drawing/renderTargets.md", loc.contentPath)
        assertEquals("drawing/renderTargets", loc.urlPath)
        assertEquals(305, loc.weight)
        assertEquals(false, loc.isSection)
        assertEquals("drawing/renderTargets.html", loc.legacyUrl)
        assertEquals("drawing", loc.urlDir)
        assertEquals("renderTargets", loc.name)
    }

    @Test
    fun `section index`() {
        val loc = GuideLocation.of("30_Drawing/index.kt")
        assertEquals("drawing/_index.md", loc.contentPath)
        assertEquals("drawing", loc.urlPath)
        assertEquals(30, loc.weight)
        assertEquals(true, loc.isSection)
        assertNull(loc.legacyUrl)
        assertEquals("drawing", loc.urlDir)
        assertEquals("index", loc.name)
    }

    @Test
    fun `root index and root page`() {
        val home = GuideLocation.of("index.kt")
        assertEquals("_index.md", home.contentPath)
        assertEquals("", home.urlPath)
        assertEquals(0, home.weight)

        val page = GuideLocation.of("C10_Requirements.kt")
        assertEquals("requirements.md", page.contentPath)
        assertEquals("requirements.html", page.legacyUrl)
        assertEquals("", page.urlDir)
    }

    @Test
    fun `nested folders`() {
        val section = GuideLocation.of("30_Drawing/50_Advanced_shaders/index.kt")
        assertEquals("drawing/advancedShaders/_index.md", section.contentPath)
        assertEquals(50, section.weight)

        val page = GuideLocation.of("30_Drawing/50_Advanced_shaders/10_Compute/C020_Work_groups.kt")
        assertEquals("drawing/advancedShaders/compute/workGroups.md", page.contentPath)
        assertEquals(20, page.weight)
        assertEquals("drawing/advancedShaders/compute", page.urlDir)
    }

    @Test
    fun `URL annotation overrides the derived path`() {
        assertEquals(
            "drawing/rt.md",
            GuideLocation.of("30_Drawing/C305_Render_targets.kt", "drawing/rt").contentPath
        )
        assertEquals(
            "painting/_index.md",
            GuideLocation.of("30_Drawing/index.kt", "painting/index").contentPath
        )
    }

    @Test
    fun `badly named files are rejected`() {
        assertFailsWith<IllegalArgumentException> { GuideLocation.of("Drawing/index.kt") }
        assertFailsWith<IllegalArgumentException> { GuideLocation.of("30_Drawing/RenderTargets.kt") }
    }

    @Test
    fun `media urls are relative to the site root`() {
        assertEquals("/media/a.png", mediaUrl("media/a.png"))
        assertEquals("/media/a.png", mediaUrl(" ../media/a.png "))
        assertEquals("/media/a.png", mediaUrl("/media/a.png"))
        assertEquals("https://x.org/a.png", mediaUrl("https://x.org/a.png"))
    }

    @Test
    fun `front matter`() {
        val fm = DokGen.zolaFrontMatter(
            "src/main/kotlin/docs/30_Drawing/C305_Render_targets.kt",
            "Render \"targets\"",
            GuideLocation.of("30_Drawing/C305_Render_targets.kt"),
            listOf("/advancedDrawing/renderTargets.html")
        )
        assertContains(fm, "title = \"Render \\\"targets\\\"\"\n")
        assertContains(fm, "weight = 305\n")
        assertContains(
            fm,
            "aliases = [\"drawing/renderTargets.html\", \"advancedDrawing/renderTargets.html\"]\n"
        )
        assertContains(fm, "[extra]\n")
        assertContains(fm, "source = \"src/main/kotlin/docs/30_Drawing/C305_Render_targets.kt\"\n")
        assert(fm.startsWith("+++\n") && fm.trimEnd().endsWith("+++"))

        val section = DokGen.zolaFrontMatter(
            "x/index.kt", "Drawing", GuideLocation.of("30_Drawing/index.kt"), emptyList()
        )
        assertContains(section, "sort_by = \"weight\"\n")
        assert("aliases" !in section)
    }
}
