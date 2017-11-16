# Render targets and color buffers # 

FIX ME: add intro sentence..

```java

RenderTarget rt;
ColorBuffer cb;

public void setup() {
    rt = RenderTarget.create(width, height);
    cb = ColorBuffer.create(width, height);  
    rt.attach(cb);
}

public void draw() {
    background(Color.BLACK);
    rt.bindTarget();
    drawer.fill(Color.WHITE);
    drawer.rectangle(40, 40, 80, 80);
    rt.unbindTarget();
 
    // draw the backing color buffer to the screen
    drawer.draw(cb, 0, 0);
}


```

## RenderTarget

A [RenderTarget](Topic_RenderTarget) defines where drawings takes place. A RenderTarget is not a target itself, it is a set of bindings with buffers. RenderTarget.attach() binds a buffer with the RenderTarget.

## ColorBuffer

FIX ME: add info..

## DepthBuffer

A RenderTarget can also have depth attachments. 

NEEDS MORE EXPLANATION??
