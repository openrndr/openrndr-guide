@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Introduction to Kotlin")
@file:ParentTitle("Kotlin language and tools")
@file:Order("100")
@file:URL("kotlinLanguageAndTools/kotlin")

package docs.`02_Kotlin_lang_and_tools`

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    # Introduction to Kotlin
    
    What follows is a simple introduction to the Kotlin programming language.
    
    If you type these example in the Kotlin code editor found at 
    [https://play.kotlinlang.org/](https://play.kotlinlang.org/) you don't need
    to install anything to get started learning the language.
    
    You can click the ⏵ Run button found above the code to run the program.
    
    The following examples demonstrate basic Kotlin concepts and do not require OPENRNDR. 
    
    ## Hello world
    
    Lets start with a classic:
    """.trimIndent()

    @Code.Block
    run {
        fun main() {
            println("Hello, world!!!")
        }
    }

    @Text
    """
    ## Functions    
    
    Notice how functions are declared with the keyword `fun` and followed by the name of the function, `main` in this case. 
    The `main` function is the default entry point for programs: the place where the execution starts. 
    By calling `println` we can display a line of text in the console.
    
    ### Declaring and calling functions
    """.trimIndent()

    @Code.Block
    run {
        fun sayHi() {
            println("Hello, world!!!")
        }

        fun main() {
            sayHi()
        }
    }

    @Text
    """
    Here we've introduced a small change: we created a second function called `sayHi`, then call that function from
    inside `main`. The result in the console should be the same.
    
    What if we wanted to modify the function so we could say hi to different people, not just to the world?
    In that case we need to change the `sayHi` function so it can receive who we want to greet as an function argument.
    
    ### Function arguments    
    
    To declare a function that accept arguments we need to specify the argument name and the argument type.
    """.trimIndent()

    @Code.Block
    run {
        fun sayHi(who: String) {
            println("Hello, $who!!!")
        }

        fun main() {
            sayHi("human")
            sayHi("friend")
        }
    }

    @Text
    """    
    The most common types are `String` for text, `Int` for integer numbers like 0, 1, 2... and `Double` for numbers
    with decimal point like `3.14159`. [More types](https://kotlinlang.org/docs/basic-types.html).
    """.trimIndent()

    @Code.Block
    run {
        fun addNumbers(a: Int, b: Int): Int {
            return a + b
        }
        fun main() {
            println()
        }
    }

    @Text
    """
    (More coming soon!)    
    """.trimIndent()

    @Text
    """
    # Learning resources
    
    - [Basic Syntax](https://kotlinlang.org/docs/basic-syntax.html) in the Kotlin documentation page.
    - Syntax examples in [Wikipedia](https://en.wikipedia.org/wiki/Kotlin_(programming_language)#Syntax).
    - [Official documentation](https://kotlinlang.org/docs/home.html). We recommend checking out the *Basics*, 
      *Concepts* and *Standard Library* sections.
    - Other [Learning materials](https://kotlinlang.org/docs/learning-materials-overview.html).    
    """.trimIndent()

}
