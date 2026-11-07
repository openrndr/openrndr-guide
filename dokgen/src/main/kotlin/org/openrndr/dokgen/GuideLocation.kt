package org.openrndr.dokgen

/**
 * Where a guide source file ends up in the Zola site.
 *
 * Everything is derived from the path of the `.kt` file relative to the
 * docs root, so sources only need a `@file:Title` annotation:
 *
 * - Folders are named `<number>_<Words>`, e.g. `30_Drawing`.
 *   The number sets the order, the words become the URL segment (`drawing`).
 * - Each folder contains an `index.kt` which becomes the section page
 *   (`drawing/_index.md`, URL `/drawing/`).
 * - Other files are named `C<number>_<Words>.kt`, e.g. `C305_Render_targets.kt`
 *   (weight 305, `drawing/renderTargets.md`, URL `/drawing/renderTargets/`).
 *
 * Folders can be nested to any depth.
 *
 * @property contentPath path of the Markdown file inside Zola's `content` folder
 * @property urlPath URL path without leading or trailing slashes ("" for the home page)
 * @property weight sort order among siblings
 * @property isSection true for `index.kt` files
 * @property legacyUrl the URL the page had when the guide was built with Jekyll,
 *   relative to the site root, or null if it did not change
 */
data class GuideLocation(
    val contentPath: String,
    val urlPath: String,
    val weight: Int,
    val isSection: Boolean,
    val legacyUrl: String?
) {
    /**
     * Directory and file name used for the generated example programs,
     * like the former `@file:URL` annotation: `drawing` + `renderTargets`
     * for a page, `drawing` + `index` for a section.
     */
    val urlDir: String
        get() = if (isSection) urlPath else urlPath.substringBeforeLast('/', "")

    val name: String
        get() = if (isSection) "index" else urlPath.substringAfterLast('/')

    companion object {
        private val folderPattern = Regex("""^(\d+)[_-](.+)$""")
        private val filePattern = Regex("""^C(\d+)[_-](.+)$""")

        /**
         * Converts `Render_targets` to `renderTargets`, `ORX` to `ORX`,
         * `FileIO` to `fileIO` and `MacOSAndWindowsTips` to `macOSAndWindowsTips`.
         * Words are separated by `_` or `-`. The first character is
         * lowercase unless the second one is uppercase (acronyms like OSC).
         */
        fun camelCase(words: String): String {
            val joined = words.split('_', '-')
                .filter { it.isNotEmpty() }
                .joinToString("") { it.replaceFirstChar { c -> c.uppercaseChar() } }
            return if (joined.length > 1 && joined[1].isLowerCase())
                joined.replaceFirstChar { it.lowercaseChar() }
            else joined
        }

        private fun folderSegment(folder: String): Pair<Int, String> {
            val m = requireNotNull(folderPattern.matchEntire(folder)) {
                "Folder `$folder` must be named like `30_Drawing`: a number, an underscore and words."
            }
            return m.groupValues[1].toInt() to camelCase(m.groupValues[2])
        }

        /**
         * @param relativePath path of the `.kt` file relative to the docs root,
         *   using `/` as separator, e.g. `30_Drawing/C305_Render_targets.kt`
         * @param urlOverride optional `@file:URL` value, e.g. `drawing/renderTargets`
         *   or `drawing/index` for a section. Overrides the derived URL.
         */
        fun of(relativePath: String, urlOverride: String? = null): GuideLocation {
            val parts = relativePath.replace('\\', '/').split('/')
            val folders = parts.dropLast(1).map { folderSegment(it) }
            val fileName = parts.last().removeSuffix(".kt")
            val isSection = fileName == "index"

            val weight: Int
            var urlPath: String
            if (isSection) {
                weight = folders.lastOrNull()?.first ?: 0
                urlPath = folders.joinToString("/") { it.second }
            } else {
                val m = requireNotNull(filePattern.matchEntire(fileName)) {
                    "File `$relativePath` must be named `index.kt` or like `C305_Render_targets.kt`: " +
                            "a C, a number, an underscore and words."
                }
                weight = m.groupValues[1].toInt()
                urlPath = (folders.map { it.second } + camelCase(m.groupValues[2])).joinToString("/")
            }

            if (!urlOverride.isNullOrBlank()) {
                urlPath = urlOverride.trim('/').let {
                    if (isSection) it.removeSuffix("index").trimEnd('/') else it
                }
            }

            val contentPath = if (isSection) {
                if (urlPath.isEmpty()) "_index.md" else "$urlPath/_index.md"
            } else {
                "$urlPath.md"
            }

            // Jekyll served pages as `drawing/renderTargets.html`. Sections were
            // served at `drawing/`, which is still the same URL.
            val legacyUrl = if (isSection) null else "$urlPath.html"

            return GuideLocation(contentPath, urlPath, weight, isSection, legacyUrl)
        }
    }
}
