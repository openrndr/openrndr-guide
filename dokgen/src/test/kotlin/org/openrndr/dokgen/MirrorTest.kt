package org.openrndr.dokgen

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MirrorTest {

    @TempDir
    lateinit var tmp: File

    private fun write(path: String, text: String) =
        File(tmp, path).apply { parentFile.mkdirs(); writeText(text) }

    @Test
    fun `only changed files are written`() {
        write("src/a.md", "a")
        write("src/dir/b.md", "b")
        val target = File(tmp, "target")

        assertEquals(2, mirrorChangedFiles(listOf(File(tmp, "src")), target))
        val b = File(target, "dir/b.md")
        b.setLastModified(1000)

        write("src/a.md", "a2")
        assertEquals(1, mirrorChangedFiles(listOf(File(tmp, "src")), target))
        assertEquals("a2", File(target, "a.md").readText())
        assertEquals(1000, b.lastModified())

        assertEquals(0, mirrorChangedFiles(listOf(File(tmp, "src")), target))
    }

    @Test
    fun `extra files are deleted unless preserved`() {
        write("src/a.md", "a")
        write("target/old/gone.md", "x")
        write("target/content/kept.md", "x")
        val target = File(tmp, "target")

        mirrorChangedFiles(listOf(File(tmp, "src")), target, preserve = listOf("content"))
        assertFalse(File(target, "old").exists())
        assertTrue(File(target, "content/kept.md").exists())
        assertTrue(File(target, "a.md").exists())
    }

    @Test
    fun `last source wins and excluded paths are skipped`() {
        write("one/x.png", "1")
        write("two/x.png", "2")
        write("two/.stamps/s", "s")
        write("target/other.png", "o")
        val target = File(tmp, "target")

        mirrorChangedFiles(
            listOf(File(tmp, "one"), File(tmp, "two")), target,
            exclude = listOf(".stamps"), deleteExtra = false
        )
        assertEquals("2", File(target, "x.png").readText())
        assertFalse(File(target, ".stamps").exists())
        assertTrue(File(target, "other.png").exists())
    }
}
