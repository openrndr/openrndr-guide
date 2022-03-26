@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Quaternions")
@file:ParentTitle("Drawing and transformations")
@file:Order("120")
@file:URL("drawingAndTransformations/quaternions")

package docs.`05_Drawing_and_transformations`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.math.Quaternion
import org.openrndr.math.Quaternion.Companion.fromAngles
import org.openrndr.math.slerp

fun main() {
    @Text """# Quaternions
Quaternions represent rotation through an extension of complex numbers. A full explanation of quaternions and their intrinsics is out of this document's scope, in this section however enough information is provided to use quaternion's effectively as a tool.

In practice quaternions are rarely constructed directly as it is fairly difficult to get an intuition for its argument values.
"""
    @Code.Block
    run {
        val q = Quaternion(0.4, 0.3, 0.1, 0.1)
    }

@Text """Instead quaternions are created from Euler-rotation angles and concatenated in quaternion space. Working in
    quaternion domains warrants consistent rotations and avoids gimbal locks."""

    run {
        val pitch0 = 0.0
        val yaw0 = 0.0
        val roll0 = 0.0
        val pitch1 = 0.0
        val yaw1 = 0.0
        val roll1 = 0.0
        @Code.Block
        run {
            val q0 = fromAngles(pitch0, yaw0, roll0)
            val q1 = fromAngles(pitch1, yaw1, roll1)
            val q = q0 * q1
        }
    }

@Text """
## Slerp

Spherical linear interpolation, or colloquially "slerping" solves the problem of interpolating or blending
between rotations.
"""
    run {
        val pitch0 = 0.0
        val yaw0 = 0.0
        val roll0 = 0.0
        val pitch1 = 0.0
        val yaw1 = 0.0
        val roll1 = 0.0
        @Code.Block
        run {
            val q0 = fromAngles(pitch0, yaw0, roll0)
            val q1 = fromAngles(pitch1, yaw1, roll1)
            val q = slerp(q0, q1 , 0.5)
        }
    }
@Text """
## Quaternion to matrix

Naturally quaternions can be converted to matrices. Quaternions have a `matrix` property that holds a `Matrix44` representation of the orientation represented by the Quaternion.
"""
    application {
        program {
            val pitch = 0.0
            val yaw = 0.0
            val roll = 0.0
            @Code.Block
            run {
                val q0 = fromAngles(pitch, yaw, roll)
                drawer.model *= q0.matrix.matrix44
            }
        }
    }
}