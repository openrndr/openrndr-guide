@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Vectors")
@file:ParentTitle("Drawing")
@file:Order("210")
@file:URL("drawing/vectors")

package docs.`30_Drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.noise.uniform
import org.openrndr.math.*


fun main() {
    @Text
    """
    # Vectors
    
    The `Vector2`, `Vector3` and `Vector4` classes are used for 2, 3 and 4 
    dimensional vector representations. Vector instances are immutable; 
    once a Vector has been instantiated its values cannot be changed.
    """

    @Code.Block
    run {
        val v2 = Vector2(1.0, 10.0)
        val v3 = Vector3(1.0, 1.0, 1.0)
        val v4 = Vector4(1.0, 1.0, 1.0, 1.0)
    }

    @Text
    """
    ## Standard vectors
    """

    @Code.Block
    run {
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
    }

    @Text
    """
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
    """

    @Code.Block
    run {
        val a = Vector2(2.0, 4.0)
        val b = Vector2(1.0, 3.0)
        val sum = a + b
        val diff = a - b
        val scale = a * 2.0
        val div = a / 2.0
        val cwdiv = a / b

    }

    @Text
    """  
    ## Vector properties
    
    property     | description
    -------------|-------------------------
    `length`     | the length of the vector
    `normalized` | a normalized version of the vector
    
    ## Swizzling and sizing
    
    Vector2 swizzles allow reordering or sizing of vector fields.
    This is a common pattern in GLSL.
    """

    @Code.Block
    run {
        val v3a = Vector2(1.0, 2.0).vector3(z = 0.0)
        val v3b = Vector2(1.0, 2.0).xy0
        val v3c = Vector2(1.0, 2.0).xy1
        val v4a = Vector2(1.0, 2.0).xy01
        val v2a = Vector3(1.0, 2.0, 3.0).xy
        val v2b = Vector3(1.0, 2.0, 3.0).yx
    }

    @Text
    """
    ## Let/copy pattern
    
    Here we present two patterns that make working with immutable Vector classes a bit more convenient.
    
    The copy pattern (which comes from Vectors being Kotlin data classes)
    """

    @Code.Block
    run {
        val v = Vector2(1.0, 2.0)
        val w = v.copy(y = 5.0)      // (1.0, 5.0)
    }

    @Text
    """
    The let/copy pattern, which combines Kotlin's `let` with `copy`
    """

    @Code.Block
    run {
        @Exclude
        fun someFunctionReturningAVector() = Vector2.ZERO
        val v = someFunctionReturningAVector().let { it.copy(x = it.x + it.y) }
    }

    @Text
    """    
    ## Mixing
    
    Linear interpolation of vectors using `mix()`
    """

    @Code.Block
    run {
        @Exclude
        val v0 = Vector2.ZERO

        @Exclude
        val v1 = Vector2.ONE

        @Exclude
        val f = 0.2
        val m = mix(v0, v1, f)
    }

    @Text
    """   
    which is short-hand for
    """

    @Code.Block
    run {
        @Exclude
        val v0 = Vector2.ZERO

        @Exclude
        val v1 = Vector2.ONE

        @Exclude
        val f = 0.2
        val m = v0 * (1.0 - f) + v1 * f
    }

    @Text
    """
    ## Randomness
    
    Generating random vectors with minimum and maximum values
    """

    @Code.Block
    run {
        val v2 = Vector2.uniform(min = -1.0, max = 1.0)

        val v3 = Vector2.uniform(
            min = Vector2(50.0, 50.0),
            max = Vector2(200.0, 100.0)
        )

        // A list with 10 random points
        val points = List(10) {
            Vector2.uniform(0.0, 1.0)
        }
    }

    @Text
    """
    To generate other random distributions of vectors see 
    [orx-noise](https://guide.openrndr.org/ORX/noise.html).
    
    # Polar coordinates
    
    Sometimes we may need to specify positions using angles and distances
    instead of `x` and `y` values. For such cases we can use `Polar` (2D) and
    `Spherical` (3D).
    """

    @Code.Block
    run {
        val p = Polar(theta = 45.0, radius = 100.0)
        val s = Spherical(theta = 30.0, phi = 90.0, radius = 100.0)
    }

    @Text
    """
    Since drawing operations don't accept `Polar` or `Spherical`
    coordinates, we can use the `.cartesian` method to convert them 
    back to something we can use for drawing.
    
    Here an example drawing 150 small circles with increasing `theta`
    and `radius` to form a spiral.
    """

    @Media.Image "../media/drawing-vectors-001.jpg"

    @Application
    @ProduceScreenshot("media/drawing-vectors-001.jpg")
    @Code
    application {
        @Exclude
        configure {
            width = 770
            height = 578
        }
        program {
            val points = List(150) {
                drawer.bounds.center + Polar(it * 5.0, it + 100.0).cartesian
            }
            extend {
                drawer.clear(ColorRGBa.WHITE)
                drawer.circles(points, 10.0)
            }
        }
    }

    @Text
    """
    The reverse operation of `.cartesian`, that is, to convert
    Cartesian coordinates to `Polar` or `Spherical`, use these methods:
    """

    @Code.Block
    run {
        // Polar(theta=45.0, radius=1.4142135623730951)
        val p = Polar.fromVector(Vector2.ONE)

        // Spherical(theta=90.0, phi=90.0, radius=1.0)
        val s = Spherical.fromVector(Vector3.UNIT_X)
    }
}