@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Vectors")
@file:ParentTitle("Drawing")
@file:Order("210")
@file:URL("drawing/vectors")

package docs.`30_Drawing`

import org.openrndr.dokgen.annotations.*


fun main() {
    @Text 
    """
    # Vectors
    
    The `Vector2`, `Vector3` and `Vector4` classes are used for 2, 3 and 4 dimensional vector representations. Vector instances are immutable; once a Vector has been instantiated its values cannot be changed.
    
    ```kotlin
    val v2 = Vector2(1.0, 10.0)
    val v3 = Vector3(1.0, 1.0, 1.0)
    val v3 = Vector4(1.0, 1.0, 1.0, 1.0)
    ```
    
    ## Standard vectors
    
    ```kotlin
    Vector2.ZERO    // (0, 0)
    Vector2.UNIT_X  // (1, 0)
    Vector2.UNIT_Y  // (0, 1)
    
    Vector3.ZERO    // (0, 0, 0)
    Vector3.UNIT_X  // (1, 0, 0)
    Vector3.UNIT_Y  // (0, 1, 0)
    Vector3.UNIT_Z  // (0, 0, 1)
    
    Vector4.ZERO    // (0, 0, 0, 0)
    Vector4.UNIT_X  // (1, 0, 0, 0)
    Vector4.UNIT_Y  // (0, 1, 0, 0)
    Vector4.UNIT_Z  // (0, 0, 1, 0)
    Vector4.UNIT_W  // (0, 0, 0, 1)
    ```
    
    ## Vector arithmetic
    The vector classes have operator overloads for the most essential operations.
    
    left operand | operator | right operand | result
    -------------|----------|---------------|---------------------------
    `VectorN`    | `+`      | `VectorN`     | addition of two vectors
    `VectorN`    | `-`      | `VectorN`     | subtraction of two vectors
    `VectorN`    | `/`      | `Double`      | scaled vector
    `VectorN`    | `*`      | `Double`      | scaled vector
    `VectorN`    | `*`      | `VectorN`     | component-wise multiplication (l.x * r.x, l.y * r.y)
    `VectorN`    | `/`      | `VectorN`     | component-wise division (l.x / r.x, l.y / r.y)
    
    Some examples of vector arithmetic in practice
    ```kotlin
    val a = Vector2(2.0, 4.0)
    val b = Vector2(1.0, 3.0)
    val sum = a + b
    val diff = a - b
    val scale = a * 2.0
    val div = a / 2.0
    val cwdiv = a / b
    ```
    
    ## Vector properties
    
    property     | description
    -------------|-------------------------
    `length`     | the length of the vector
    `normalized` | a normalized version of the vector
    
    ## Swizzling and sizing
    
    Vector2 swizzles allow reordering of vector fields, this is a common pattern in GLSL
    
    ```kotlin
    val v3 = Vector2(1.0, 2.0).vector3(z=0.0)
    val v2 = Vector3(1.0, 2.0, 3.0).xy
    ```
    
    ## Let/copy pattern
    
    Here we present two patterns that make working with immutable Vector classes a bit more convenient.
    
    The copy pattern (which comes from Vectors being Kotlin data classes)
    
    ```kotlin
    val v = Vector2(1.0, 2.0)
    val w = v.copy(y=5.0)      // (1.0, 5.0)
    ```
    
    The let/copy pattern, which combines Kotlin's `let` with `copy`
    
    ```kotlin
    val v = someFunctionReturningAVector().let { it.copy(x=it.x + it.y) }
    ```
    
    ## Mixing
    
    Linear interpolation of vectors using `mix()`
    
    ```kotlin
    val m = mix(v0, v1, f)
    ```
    
    which is short-hand for
    ```kotlin
    val m = v0 * (1.0 - f) + v1 * f
    ```
    
    ## Randomness
    
    Generating random vectors
    
    ```kotlin
    val v2 = Random.vector2(-1.0, 1.0)
    val v3 = Random.vector3(-1.0, 1.0)
    val v4 = Random.vector4(-1.0, 1.0)
    ```
    
    To generate random distributions of vectors see 
    [orx-noise](https://guide.openrndr.org/ORX/noise.html).

    """
}