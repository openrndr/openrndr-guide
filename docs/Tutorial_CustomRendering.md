A simple custom rendering sketch will look like this. In this sketch we draw a single rectangle, that is formed by drawing two triangles.

```java

VBOLayout layout = new VBOLayout().color(4).position(2);
Shader shader;
VBO vbo;

public void setup() {
    shader = Shader.fromUrls("cp:myshader.vert", "cp:myshader.frag");
    ByteBuffer bb = ByteBuffer.allocateDirect(layout.size() * 6);
    BufferWriter bw = new BufferWriter(bb);
    bw.write(Color.WHITE).write(new Vector2(0, 0));
    bw.write(Color.WHITE).write(new Vector2(0, 0));
    bw.write(Color.WHITE).write(new Vector2(0, 0));
    bw.write(Color.WHITE).write(new Vector2(0, 0));
    bw.write(Color.WHITE).write(new Vector2(0, 0));
    bw.write(Color.WHITE).write(new Vector2(0, 0));

    vbo = VBO.createStatic(bb, bb.capacity);
}

public void draw() {
    background(Color.BLACK);
    shader.begin();
    shader.setUniform("modelViewMatrix", drawer.modelView().transpose());
    shader.setUniform("projectionMatrix", drawer.projection().transpose());

    vbo.bind();
    VBODrawer.draw(gl3, vbo, vboLayout, shader, GL.GL_TRIANGLES, 6);
    vbo.unbind();
    shader.end();
}

```

Here we see a number of new RNDR classes pop up, let's look at them one by one.

## VBO

A VBO is an OpenGL concept. A VBO holds vertex data.

## VBOLayout

VBOLayout objects describe the vertices stored in a VBO.

## ByteBuffer

ByteBuffer is class found in Java's standard library. ByteBuffers allow you to write data into memory in a rather low level way.

## BufferWriter

BufferWriter is a helper class that makes writing things into ByteBuffers slightly nicer. BufferWriter can write RNDRs Vector2, Vector3, Vector3 and Color objects into a ByteBuffer.

## Shader, FragmentShader and VertexShader

Shaders are an OpenGL concept.

## VBODrawer

The VBODrawer is a RNDR utility class that makes rendering from VBOs easy.

