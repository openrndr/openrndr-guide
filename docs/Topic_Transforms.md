# Transforms. Vectors, Matrices, Quaternions #

OPENRNDR extensively uses vector and matrix classes to pass positions and transforms around.

OPENRNDR's vector and matrix classes are immutable, that means once they are constructed their value cannot be changed.

## Vectors ##

Relevant APIs

```
Vector2
Vector3
Vector4
````

The `Vector2`, `Vector3` and `Vector4` classes are used for 2, 3 and 4 dimensional vector representations.

```kotlin
val v2 = Vector2(1.0, 10.0)
val v3 = Vector3(1.0, 1.0, 1.0)
val v3 = Vector4(1.0, 1.0, 1.0, 1.0)
```

### Vector arithmetic

The vector classes have operator overloads for the most essential operations.

```kotlin
val v2sum = Vector2(1.0, 10.0) + Vector2(1.0, 1.0)
val v2diff = Vector2(1.0, 10.0) - Vector2(1.0, 1.0)
val v2scale = Vector2(1.0, 10.0) * 2.0
```

### Swizzling and sizing

```kotlin
    val v3 = Vector2(1.0, 2.0).vector3(z=0.0)
    val v2 = Vector3(1.0, 2.0, 3.0).xy
```

### Mixing

Linear interpolation of vectors using `mix()`

```kotlin
    val m = mix(v0, v1, f)
```

which is short-hand for
```
    val m = v0 * (1.0 - f) + v1 * f
```

## Quaternions

Quaternions represent rotation through an extension of complex numbers. A full explanation of quaternions and their intrinsics is out of this document's scope, in this section however enough information is provided to use quaternion's effectively as a tool.

In practice quaternions are rarely constructed directly as it is fairly difficult to get an intuition for its argument values.
```
    val q = Quaternion(0.4, 0.3, 0.1, 0.1)
```

Instead quaternions are created from Euler-rotation angles and concatenated in quaternion space. Working in quaternion space warrants consistent rotations and avoids gimbal locks.

```
    val q0 = fromAngles(pitch0, yaw0, roll0)
    val q1 = fromAngles(pitch1, yaw1, roll1)
    val q = q0 * q1
```

### Slerp

Spherical linear interpolation, or colloquially "slerping" solves the problem of interpolating or blending
between rotations.

```
    val q0 = fromAngles(pitch0, yaw0, roll0)
    val q1 = fromAngles(pitch1, yaw1, roll1)
    val q = slerp(q0, q1, 0.5)
```

### Quaternion to matrix

Naturally quaternions can be converted to matrices. Quaternions have a `matrix` property that holds a `Matrix44` representation of the orientation represented by the Quaternion.

```
    val q = fromAngles(pitch, yaw, roll)
    val m = q.matrix
    drawer.view = m
```

## Transforms

In OPENRNDR transforms are represented by `Matrix44` instances.

OPENRNDR offers tools to construct `Matrix44`

### Transform builder

Relevant APIs
```
Matrix44
transform {}
```

In the snippet below a `Matrix44` instance is constructed using the `transform {}` builder. Note that the application order is from bottom to top.

```kotlin
drawer.view *= transform {
    rotate(32.0)
    rotate(43.0, Vector3(1.0, 1.0, 0.0).normalized)
    translate(4.0, 2.0)
    scale(2.0)
}
```

This is equivalent to the following:
```kotlin
drawer.rotate(32.0)
drawer.rotate(43.0,  Vector3(1.0, 1.0, 0.0).normalized)
drawer.translate(4.0, 2.0)
drawer.scale(2.0)
```

## Applying transforms to vectors ##

```kotlin
    val x = Vector3(1.0, 2.0, 3.0)
    val m = transform {
        rotate(42.0)
    }
    val transformed = m * x
    val transformedTwice = m * m * x
```