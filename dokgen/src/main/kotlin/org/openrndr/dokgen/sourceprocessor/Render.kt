package org.openrndr.dokgen.sourceprocessor

/**
 * Puts together a package directive, the imports and the code of a Kotlin program.
 *
 * @param pkg Package directive
 * @param imports A list of imports
 * @param body The source code of the program
 * @return The source code of a complete Kotlin program as a String
 */
fun appTemplate(pkg: String, imports: List<String>, body: String): String {
    return """
package $pkg

${imports.joinToString("\n")}

fun main() {
${body.prependIndent(" ".repeat(4))}
}
    """
}

/**
 * Media paths in the sources are relative (`media/x.png` or `../media/x.png`).
 * Pages can be nested at any depth, so link media from the site root instead.
 */
fun mediaUrl(src: String): String {
    val s = src.trim()
    if (s.startsWith("/") || s.contains("://")) return s
    var p = s
    while (p.startsWith("../") || p.startsWith("./")) {
        p = p.substringAfter('/')
    }
    return "/$p"
}

/**
 * Produces markdown page of the guide page
 *
 * @param doc A document composed on multiple blocks of text, code and
 * links to video and image files.
 * @param title A header to inject at the top of the document
 * @return A Markdown document
 */
fun renderDoc(doc: Doc, title: String? = null): String {
    val strDoc = doc.elements.fold("") { acc, el ->
        val str = when (el) {
            is Doc.Element.Code -> {
                val code = if(el.value.startsWith("application"))
                    "fun main() = ${el.value}" else el.value
                """
                 |```kotlin
                 |$code
                 |```
               """.trimMargin()
            }
            is Doc.Element.Markdown -> {
                el.text.trimIndent()
            }
            is Doc.Element.Media.Image -> {
                val src = mediaUrl(el.src)
                """<img alt="$src" src="$src" loading="lazy">"""
            }
            is Doc.Element.Media.Video -> {
                val video = mediaUrl(el.src)
                val still = video.replace(".mp4", "-thumb.jpg")
                """
                |<video controls preload="none" loop poster="$still">
                |    <source src="$video" type="video/mp4">
                |</video>
                |""".trimMargin()
            }
        }
        "$acc \n$str \n"
    }
    return title?.let {
        "# $title\n$strDoc"
    } ?: strDoc
}
