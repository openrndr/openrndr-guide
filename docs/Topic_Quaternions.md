# Quaternions

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
