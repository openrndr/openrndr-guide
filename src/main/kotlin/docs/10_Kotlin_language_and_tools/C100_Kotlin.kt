@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Introduction to Kotlin")
@file:ParentTitle("Kotlin language and tools")
@file:Order("100")
@file:URL("kotlinLanguageAndTools/kotlin")

package docs.`10_Kotlin_language_and_tools`

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.*

fun main() {
    @Language("markdown") val a =
    @Text
    """
    # The Kotlin programming language
    
    Kotlin is a modern, readable and fun language, perfect for
    creative coding.
    
    A good place to start discovering the language is the
    [Kotlin Tour](https://kotlinlang.org/docs/kotlin-tour-welcome.html)
    
    Most of the examples on that website can be edited and 
    run to immediately see the result. 
                
    From the [Official documentation](https://kotlinlang.org/docs/home.html)
    we recommend exploring the *Basics*, *Concepts* and *Standard Library* sections.
    Data structures like `List`, `Map` and `Set` are explained under the
    *Standard Library* section, and they are one of the aspects that make
    working with Kotlin enjoyable. Check them out!
    
    ## Kotlin in OPENRNDR
    
    When designing a framework like OPENRNDR, many decisions need to be made.
    What should be favored? Brief syntax? Flexibility? Expressiveness? Execution speed? 
    Similarity with other frameworks?
    
    Those decisions shape what user code will look like. In this section we attempt to explain
    some of those decisions and possible differences with other languages and frameworks.
    
    Most concepts in the Kotlin programming language will sound familiar to anyone
    experienced in other languages, but some may be new. 
    
    Let's take a look at them.
    """.trimIndent()

}