 
 # orx-noise 
 
 A collection of noise generator functions. Source and extra documentation can be found in the [orx-noise sourcetree](https://github.com/openrndr/orx/tree/master/orx-noise). 
 
 ## Uniform noise 
 
 The library provides extension methods for `Double`, `Vector2`, `Vector3`, `Vector4` to create random vectors easily. To create
scalars and vectors with uniformly distributed noise you use the `uniform` extension function. 
 
 ```kotlin
val d1 = Double.uniform(0.0, 640.0)
val v2 = Vector2.uniform(0.0, 640.0)
val v3 = Vector3.uniform(0.0, 640.0)
val v4 = Vector4.uniform(0.0, 640.0)
``` 
 
 To create multiple samples of noise one uses the `uniforms` function. 
 
 ```kotlin
val v2 = Vector2.uniforms(100, Vector2(0.0, 0.0), Vector2(640.0, 640.0))
val v3 = Vector3.uniforms(100, Vector3(0.0, 0.0, 0.0), Vector3(640.0, 640.0, 640.0))
``` 
 
 ## Uniform ring noise 
 
 ```kotlin
val v2 = Vector2.uniformRing(0.0, 300.0)
val v3 = Vector3.uniformRing(0.0, 300.0)
val v4 = Vector4.uniformRing(0.0, 300.0)
``` 
 
 <img src="media/orx-noise-001.png"/> 
 
 ```kotlin
application {
    program {
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            drawer.translate(width / 2.0, height / 2.00)
            for (i in 0 until 1000) {
                drawer.circle(Vector2.uniformRing(150.0, 250.0), 10.0)
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise000.kt) 
 
 ## Perlin noise 
 
 <img src="media/orx-noise-002.png"/> 
 
 ```kotlin
application {
    program {
        
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            val scale = 0.005
            for (y in 16 until height step 32) {
                for (x in 16 until width step 32) {
                    val radius = perlinLinear(100, x * scale, y * scale) * 16.0 + 16.0
                    drawer.circle(x * 1.0, y * 1.0, radius)
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise001.kt) 
 
 ## Value noise 
 
 <img src="media/orx-noise-003.png"/> 
 
 ```kotlin
application {
    program {
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            val scale = 0.0150
            for (y in 16 until height step 32) {
                for (x in 16 until width step 32) {
                    val radius = valueLinear(100, x * scale, y * scale) * 16.0 + 16.0
                    drawer.circle(x * 1.0, y * 1.0, radius)
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise002.kt) 
 
 ## Simplex noise 
 
 <img src="media/orx-noise-004.png"/> 
 
 ```kotlin
application {
    program {
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            val scale = 0.004
            for (y in 16 until height step 32) {
                for (x in 16 until width step 32) {
                    val radius = simplex(100, x * scale, y * scale) * 16.0 + 16.0
                    drawer.circle(x * 1.0, y * 1.0, radius)
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise003.kt) 
 
 ## Fractal/FBM noise 
 
 <video controls>
    <source src="media/orx-noise-005.mp4" type="video/mp4"></source>
</video>
 
 
 ```kotlin
application {
    program {
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            val s = 0.0080
            val t = seconds
            for (y in 4 until height step 8) {
                for (x in 4 until width step 8) {
                    val radius = when {
                        t < 3.0 -> abs(fbm(100, x * s, y * s, t, ::perlinLinear)) * 16.0
                        t < 6.0 -> billow(100, x * s, y * s, t, ::perlinLinear) * 2.0
                        else -> rigid(100, x * s, y * s, t, ::perlinLinear) * 16.0
                    }
                    drawer.circle(x * 1.0, y * 1.0, radius)
                }
            }
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise004.kt) 
 
 ## Noise filters 
 
 The library contains a number of Filters with which noise image can be generated efficiently on the GPU. 
 
 ### Hash noise 
 
 A white-noise-like noise generator. 
 
 Parameter            | Default value                 | Description
---------------------|-------------------------------|-------------------------------------------
`seed`               | `0.0`                         | Noise seed
`gain`               | `Vector4(1.0, 1.0, 1.0, 0.0)` | Noise gain per channel      
`bias`               | `Vector4(0.0, 0.0, 0.0, 1.0)` | Value to add to the generated noise       
`monochrome`         | `true`                        | Outputs monochrome noise if true
`premultipliedAlpha` | `true`                        | Outputs premultiplied alpha if true                 
 
 <img src="media/orx-noise-filter-001.png"/> 
 
 ```kotlin
application {
    program {
        val cb = colorBuffer(width, height)
        val hn = HashNoise()
        extend {
            hn.seed = seconds
            hn.apply(emptyArray(), cb)
            drawer.image(cb)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise005.kt) 
 
 ### Cell noise 
 
 A cell, Worley or Voronoi noise generator 
 
 Parameter            | Default value                 | Description
---------------------|-------------------------------|-------------------------------------------
`seed`               | `Vector2(0.0, 0.0)`           | Noise seed / offset
`scale`              | `Vector2(1.0, 1.0)`           | The noise scale at the first octave
`octaves`            | `4`                           | The number of octaves
`gain`               | `Vector4(1.0, 1.0, 1.0, 0.0)` | Noise gain per channel per octave      
`decay`              | `Vector4(0.5, 0.5, 0.5, 0.5)` | Noise decay per channel per octave
`bias`               | `Vector4(0.0, 0.0, 0.0, 1.0)` | Value to add to the generated noise       
`lacunarity`         | `Vector4(2.0, 2.0, 2.0, 2.0)` | Multiplication of noise scale per octave
`premultipliedAlpha` | `true`                        | Outputs premultiplied alpha if true                 
 
 <img src="media/orx-noise-filter-002.png"/> 
 
 ```kotlin
application {
    program {
        val cb = colorBuffer(width, height)
        val cn = CellNoise()
        extend {
            cn.octaves = 4
            cn.apply(emptyArray(), cb)
            drawer.image(cb)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise006.kt) 
 
 ### Speckle noise 
 
 A speckle noise generator. 
 
 Parameter            | Default value                 | Description
---------------------|-------------------------------|-------------------------------------------
`color`              | `ColorRGBa.WHITE`             | Speckle color
`density`            | `1.0`                         | Speckle density
`seed`               | `0.0`                         | Noise seed
`noise`              | `0.0`                         | Speckle noisiness      
`premultipliedAlpha` | `true`                        | Outputs premultiplied alpha if true                 
 
 <img src="media/orx-noise-filter-003.png"/> 
 
 ```kotlin
application {
    program {
        val cb = colorBuffer(width, height)
        val sn = SpeckleNoise()
        extend {
            sn.seed = seconds
            sn.apply(emptyArray(), cb)
            drawer.image(cb)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise007.kt) 
 
 ### Value noise 
 
 The `ValueNoise` filter generates a simple fractal noise. Value noise is a computationally cheap form of creating
        'smooth noise' by interpolating random values on a lattice. 
 
 Parameter            | Default value                 | Description
---------------------|-------------------------------|-------------------------------------------
`seed`               | `Vector2(0.0, 0.0)`           | Noise seed / offset
`scale`              | `Vector2(1.0, 1.0)`           | The noise scale at the first octave
`octaves`            | `4`                           | The number of octaves
`gain`               | `Vector4(1.0, 1.0, 1.0, 0.0)` | Noise gain per channel per octave      
`decay`              | `Vector4(0.5, 0.5, 0.5, 0.5)` | Noise decay per channel per octave
`bias`               | `Vector4(0.0, 0.0, 0.0, 1.0)` | Value to add to the generated noise       
`lacunarity`         | `Vector4(2.0, 2.0, 2.0, 2.0)` | Multiplication of noise scale per octave
`premultipliedAlpha` | `true`                        | Outputs premultiplied alpha if true                 
 
 <img src="media/orx-noise-filter-004.png"/> 
 
 ```kotlin
application {
    program {
        val cb = colorBuffer(width, height)
        val vn = ValueNoise()
        extend {
            vn.scale = Vector2.ONE * 4.0
            vn.gain = Vector4.ONE * 0.5
            vn.octaves = 8
            vn.apply(emptyArray(), cb)
            drawer.image(cb)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/10_OPENRNDR_Extras/C01_Noise008.kt) 
