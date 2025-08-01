@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Immutability")
@file:ParentTitle("Kotlin language and tools")
@file:Order("102")
@file:URL("kotlinLanguageAndTools/immutability")

package docs.`10_Kotlin_language_and_tools`

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.*

fun main() {
    @Language("markdown") val a =
    @Text
    """
    # Mutability and immutability
                
    According to the [coding conventions](https://kotlinlang.org/docs/coding-conventions.html#immutability) 
    section of the Kotlin guide, we should prefer using immutable to mutable data structures 
    and declare local variables and properties as `val` rather than `var` if they are 
    not modified after initialization.
    
    To learn more about this topic, please take a look at the 
    [hello world](https://kotlinlang.org/docs/kotlin-tour-hello-world.html),
    [basic types](https://kotlinlang.org/docs/kotlin-tour-basic-types.html) and
    [collections](https://kotlinlang.org/docs/kotlin-tour-collections.html) sections 
    of the Kotlin Tour.
    
    This should help you familiarize with the idea of mutable and immutable data in Kotlin.
            
    # OPENRNDR data structures
    
    ## Vectors and Matrices
    
    OPENRNDR makes extensive use of data types like `Vector2`, `Vector3`, `Vector4`,
    `Matrix2x2`, `Matrix3x3`, `Matrix4x4` and `Matrix5x5` for creating and drawing 2D and 3D shapes
    
    Such data structures are immutable in OPENRNDR.
    That means that the following would not work:
    
    ```kotlin
    val v = Vector2.ZERO
    v.x += 0.1 // error: x is immutable
    ```
    
    We could switch from `val` to `var`: 
    
    ```kotlin
    var v = Vector2.ZERO
    ```
    
    but we still can't modify the `x` component of that vector, 
    because `x` is still immutable. 
    The solution is to not try changing the components independently, but to
    assign a new value to the variable:
    
    ```kotlin
    var v = Vector2.ZERO
    v += Vector2(0.1, 0.0)
    ```
    
    The same idea applies to matrices. For example:
    
    ```kotlin
    var m = Matrix4x4.IDENTITY // One of the existing Matrix4x4 "presets"
    m = m * 2.0 + 1.0 // Assign a new matrix with all components multiplied by 2.0 and incremented by 1.0
    ```
    
    In a nutshell, don't try changing individual properties of OPENRNDR objects
    but assign a new value to the variable instead. 
 
    ## The drawer state is mutable
    
    One area in the framework that does have a mutable state is the 
    [drawer](https://api.openrndr.org/openrndr-draw/org.openrndr.draw/-drawer/index.html). 
    
    We can alter properties like `drawer.fill`, `drawer.stroke`, 
    `drawer.strokeWeight` and doing so will affect any following shapes we draw.
    
    One detail: `drawer.fill` holds a `ColorRGBa` variable. This variable has
    `r`, `g`, `b` and `a` components. But the same way we could not modify the 
    `x` component of a `Vector2`, we cannot modify the `r` component of 
    a `ColorRGBa`. We just need to assign a new value to the `fill`, instead
    of trying to modify the components of the color.
    
    ## Mutating other OPENRNDR objects
    
    A [`ShapeContour`](https://guide.openrndr.org/drawing/curvesAndShapes.html#shapecontour) is a core OPENRNDR class that holds an open or closed 
    contour made out of straight or curved segments.
    
    Let's imagine we have a circular contour made out of four curved segments. 
    Could we mutate it? Could we animate the position of one of its vertices? 
    Again, the same rule applies here. Contours, segments and vertices are all immutable.
    
    If we want to animate and deform the circle, we need to recreate the contour
    on every animation frame. We could do this based on the `seconds` variable, 
    based on the previous version of the contour, or based on some other changing data. 
    
    OPENRNDR provides many powerful tools to construct and deform contours.
    We only need to get used to the idea of working with immutable data. 
    """.trimIndent()

}
