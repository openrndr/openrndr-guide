## Playing videos ##

The VideoPlayer class is used to play videos. Please note that it is only tested on OSX with ffmpeg 2.8.x. The current version of ffmpeg offered by brew is 3.3 and is not compatible. If you already installed ffmpeg with brew, please do:

`brew update`

`brew install ffmpeg@2.8`

`brew unlink ffmpeg`

`brew link ffmpeg@2.8 --force`

and you're set!

if you installed ffmpeg differently, move it out of the way before installing/linking ffmpeg@2.8


```java
import net.lustlab.colorlib.ColorRGBa;
import net.lustlab.rndr.Configuration;
import net.lustlab.rndr.application.Application;
import net.lustlab.rndr.sketch.Program;
import net.lustlab.rndr.video.VideoPlayer;

public class VideoPlayerExample extends Program {

    VideoPlayer videoPlayer;

    public void setup() {
        videoPlayer = new VideoPlayer().source("data/path/to/video.mp4").start().play();
    }

    public void draw() {
        drawer.background(ColorRGBa.GRAY);

        if (videoPlayer.height() > 0) {
            videoPlayer.draw(drawer, 0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        VideoPlayerExample app = new VideoPlayerExample();

        Application.run(app, new Configuration()
                .size(1280, 960)
                .title("VideoPlayerExample")
                .hideWindowDecorations(false)
                .debug(true)
        );
    }

}
```
