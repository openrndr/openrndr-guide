@file:Suppress("UNUSED_EXPRESSION")

package docs.`07_Interaction`

import org.openrndr.application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text


fun main(args: Array<String>) {
    @Text
    """
    # Clipboard

    OPENRNDR programs can access the clipboard. Currently only text snippets can be read from and written to the clipboard.

    ##### Setting the clipboard content
    """


    @Code
    application {
        program {
            clipboard.contents = "this is the new clipboard content"
        }
    }


    @Text
    """
    ##### Getting the clipboard content

    Note that `clipboard.contents` is optional and its value can be `null`. The clipboard contents are reported `null` in case the clipboard contents are non-text or the clipboard is empty.
    """

    @Code
    application {
        program {
            clipboard.contents?.let {
                println("the clipboard contents: $it")
            }
        }
    }
}