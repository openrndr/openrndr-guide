# Writing to video files #


```java

VideoWriter videoWriter;
RenderTarget renderTarget;

public void setup() {
    videoWriter = new VideoWriter().width(width).height(height).output("output.mp4").start();
    renderTarget = RenderTarget.create(width, height).attach(ColorBuffer.create(width, height));
}

int frame = 0;
public void draw() {
    renderTarget.bind();
    drawer.background(Color.RED);
    drawer.rectangle(40 + frame, 40, 100,1 00 ); 
    renderTarget.unbind();
    videoWriter.frame(renderTarget.colorBuffer(0));

    // also draw the result to the screen
    drawer.draw(renderTarget.colorBuffer(0), 0, 0);
    frame++;
    if (frame == 100) {
        videoWriter.stop();
        quit();
    }
}

```

If you are interested in the progress of the encoding process you can look at the output file `ffmpeg_output_msg.txt`. The most convenient way to this is using `tail -f ffmpeg_output_msg.txt` from the terminal.
