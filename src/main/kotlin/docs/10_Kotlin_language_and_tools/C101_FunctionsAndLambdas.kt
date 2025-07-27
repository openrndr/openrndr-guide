@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Functions and lambdas")
@file:ParentTitle("Kotlin language and tools")
@file:Order("101")
@file:URL("kotlinLanguageAndTools/functionsAndLambdas")

package docs.`10_Kotlin_language_and_tools`

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.*

fun main() {
    val n = ""
    @Language("markdown") val a =
    @Text
    """
    # Functions and Lambdas

    If you look at a basic OPENRNDR program and can't stop thinking about
    that syntax with curly brackets following certain words, this section is for you.
    
    ### Functions
        
    Please read the section on functions in the 
    [Kotlin Tour](https://kotlinlang.org/docs/kotlin-tour-functions.html)
    all the way to the end. There you will learn about Lambda functions.
    
    When you're done, you should probably be able to understand the syntax of
    a basic OPENRNDR program: when we encounter code like
    `extend { ... }` we know that we are dealing with two functions.
    The first one is called `extend()` and the second one, `{ ... }`, is an anonymous
    lambda function. In Kotlin, it's not necessary to include the parentheses, as in
    `extend({ ... })`, if the last argument of the `extend` function is a Lambda
    function.
    
    What `extend { ... }` does is calling the `extend` function to tell OPENRNDR: 
    "here, take this anonymous function and execute it on every animation frame". 
    
    Other creative coding frameworks usually expect *one* specific method, often called `draw`,
    where the user can draw to the screen.
    
    Having an `extend` method that accepts a lambda function gives OPENRNDR 
    the unique capability of combining multiple `extend` blocks 
    in the same program. 
    
    The framework provides several such `extend` blocks (called Extensions),
    for instance, [Screenshots](/extensions/screenshots.html), 
    [ScreenRecorder](/extensions/writingToVideoFiles.html)
    and [Camera2D](/extensions/camera2D.html), among others. This makes it possible to build programs
    by composing code blocks as if they were LEGO pieces and add powerful
    functionality with just one or two lines of code.
    
    """.trimIndent()

}
