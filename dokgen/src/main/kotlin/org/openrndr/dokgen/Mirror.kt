package org.openrndr.dokgen

import java.io.File

/**
 * Mirrors the files found in [sources] into [target], like Gradle's `sync`,
 * but only writes files whose content differs. Gradle's `copy` and `sync`
 * rewrite every file, which makes `zola serve` rebuild the site once per file.
 *
 * Each source is a directory whose contents are copied into [target], or a
 * single file copied to the root of [target]. If a path exists in several
 * sources, the last one wins.
 *
 * Paths in [exclude] and [preserve] are relative to the source / target,
 * use `/` as separator and match the path itself and everything below it.
 *
 * @param exclude source paths which are not copied
 * @param deleteExtra delete files in [target] not present in [sources]
 *   (and directories left empty)
 * @param preserve target paths which are never deleted
 * @return the number of files written or deleted
 */
fun mirrorChangedFiles(
    sources: List<File>,
    target: File,
    exclude: List<String> = emptyList(),
    deleteExtra: Boolean = true,
    preserve: List<String> = emptyList(),
): Int {
    fun List<String>.matches(path: String) = any { path == it || path.startsWith("$it/") }
    fun File.relPath(root: File) = toRelativeString(root).replace(File.separatorChar, '/')

    val wanted = linkedMapOf<String, File>()
    sources.forEach { root ->
        if (root.isFile) {
            wanted[root.name] = root
        } else if (root.isDirectory) {
            root.walkTopDown().filter { it.isFile }.forEach { file ->
                val path = file.relPath(root)
                if (!exclude.matches(path)) wanted[path] = file
            }
        }
    }

    var changes = 0
    wanted.forEach { (path, source) ->
        val destination = File(target, path)
        if (!sameContent(source, destination)) {
            source.copyTo(destination, overwrite = true)
            changes++
        }
    }

    if (deleteExtra && target.isDirectory) {
        // Bottom-up: a directory is visited after its contents were handled
        target.walkBottomUp().filter { it != target }.forEach { file ->
            val path = file.relPath(target)
            when {
                preserve.matches(path) -> {}
                file.isFile && path !in wanted -> if (file.delete()) changes++
                file.isDirectory && file.list()?.isEmpty() == true -> file.delete()
            }
        }
    }
    return changes
}

private fun sameContent(a: File, b: File): Boolean {
    if (!b.isFile || a.length() != b.length()) return false
    a.inputStream().buffered().use { ia ->
        b.inputStream().buffered().use { ib ->
            val bufA = ByteArray(64 * 1024)
            val bufB = ByteArray(64 * 1024)
            while (true) {
                val n = ia.readNBytes(bufA, 0, bufA.size)
                if (n != ib.readNBytes(bufB, 0, bufB.size)) return false
                if (n == 0) return true
                if (!bufA.contentEquals(bufB)) return false
            }
        }
    }
}
