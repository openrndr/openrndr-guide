 
 # Compute shaders 
 
 Since version 0.3.36 OPENRNDR comes with compute shader functionality for select platforms. Compute 
shader support only works on systems that support OpenGL 4.3 or higher. This excludes all versions of MacOS. 
 
 ## Example use 
 
 Here we provide a quick break-down on compute shader use. This text is preliminary and will be improved
shortly. 
 
 In the example we will work with the following compute shader in `data/compute-shaders/fill.cs`. 
 
 ```glsl
#version 430
layout(local_size_x = 1, local_size_y = 1) in;
uniform writeonly image2D outputImg;
uniform vec4 fill;

layout(rgba8) uniform readonly image2D inputImg;
void main() {
    ivec2 coords = ivec2(gl_GlobalInvocationID.xy);
    float v = cos(coords.x * 0.01 + coords.y * 0.01) * 0.5 + 0.5;

    vec4 inxel = imageLoad(inputImg, coords);
    vec4 pixel = vec4(v, 0.0, 0.0, 1.0);
    imageStore(outputImg, coords, pixel+inxel+fill);
}        
``` 
 
 <img src="media/compute-shaders-001.png"/> 
 
 ```kotlin
application {
    program {
        val cs = ComputeShader.fromFile(File("data/compute-shaders/fill.cs"))
        val result = colorBuffer(width, height)
        val testRGBa = colorBuffer(640, 480)
        val test = loadImage("data/images/cheeta.jpg")
        test.copyTo(testRGBa)
        
        extend {
            cs.uniform("fill", ColorRGBa.PINK.shade(0.1))
            cs.image("inputImg", 1, testRGBa, ImageAccess.READ)
            cs.image("outputImg", 0, result, ImageAccess.WRITE)
            cs.execute(result.width, result.height, 1)
            drawer.image(result)
        }
    }
}
``` 
 
 [Link to the full example](https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin/examples/11_Advanced_Topics/C06_Compute_shaders000.kt) 
