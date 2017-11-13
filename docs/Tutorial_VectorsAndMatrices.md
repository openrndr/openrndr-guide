# Vectors and Matrices #

RNDR extensively uses vector and matrix classes to pass positions and transforms around.

RNDR's vector and matrix classes are immutable, that means once they are constructed its value cannot be changed. 

## Vectors ##

The `Vector2`, `Vector3` and `Vector4` classes are used for 2, 3 and 4 dimensional vector representations.

```java


// -- construct a Vector2 instance
Vector2 v2 = new Vector2(10, 10);

// -- alternative, shorter notation
Vector2 v2a = vector2(10, 10);

// -- vector algebra
Vector2 sum = vector2(10, 10).plus(vector2(20, 20));
Vector2 diff = vector2(10, 10).minus(vector2(20, 20));
Vector2 scaled = vector2(10, 10).scale(5.0);

// -- vector swizzling
Vector2 yx = vector2(1, 2).yx();
Vector2 xx = vector2(1, 2).xx();

// -- reassigning values
Vector2 v2b = vector2(10, 10).withX(4).withY(5);

// -- create Vector3 from Vector2 instance
Vector3 v3 = vector2(10, 10).xy0();

```

## Transforms ## 

The `Transforms` class contains a set of helpers to construct common transforms

 