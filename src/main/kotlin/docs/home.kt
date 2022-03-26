@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Home")
@file:Order("0")
@file:URL("/index")

package docs

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    Welcome to the OPENRNDR guide!
    """.trimIndent()
}