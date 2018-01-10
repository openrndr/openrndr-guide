# Vectors and Matrices #

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

### Vector Arithmetic

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

```kotlin
    val m = mix(Vector3(1.0, 2.0, 3.0), Vector3(3.0, 2.0, 1.0), 0.5)
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

