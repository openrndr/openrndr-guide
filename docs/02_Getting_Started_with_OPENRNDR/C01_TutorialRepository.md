# Using the tutorial repository

The [tutorial repository](https://github.com/openrndr/openrndr-tutorials) can be browsed online.
The repository contains a multi-module Gradle project in which each tutorial is a sub-module. The Gradle project can easily be imported into IntelliJ.

## List of tutorials

In alphabetical order

##### Animation tutorials
 - [`animation-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/animation-001)

##### Camera tutorials
 - [`camera-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/camera-001) using the orbital camera provided by the Debug3D extension

##### Clip tutorials
 - [`clip-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/clip-001) using rectangular clipping masks

##### Color tutorials
 - [`color-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/color-001) converting from HSV to RGB using ColorHSVa

##### Complex shape tutorials
 - [`complex-shapes-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/complex-shapes-001) constructing contour using the contour builder
 - [`complex-shapes-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/complex-shapes-002) constructing shapes using the shape builder
 - [`complex-shapes-003`](https://github.com/openrndr/openrndr-tutorials/tree/master/complex-shapes-002) constructing using Circle.contour, Rectangle.contour and LineSegment.contour
 - [`complex-shapes-004`](https://github.com/openrndr/openrndr-tutorials/tree/master/complex-shapes-002) tessellating shapes from SVG and instanced drawing

##### Cubemap tutorials
 - [`cubemaps-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/cubemaps-001) loading and drawing cubemaps
 - [`cubemaps-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/cubemaps-002) drawing cubemaps and using shade styles for normal mapping

##### Custom rendering tutorials
 - [`custom-rendering-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/custom-rendering-001) rendering custom geometry using vertex buffers
 - [`custom-rendering-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/custom-rendering-002) shading of custom geometry
 - [`custom-rendering-003`](https://github.com/openrndr/openrndr-tutorials/tree/master/custom-rendering-003) rendering instanced geometry

##### Filter examples
 - [`filters-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/filters-001) applying blur to the contents of a render target
 - [`filters-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/filters-002) creating custom filters using the Filter class and fragment shaders
 - [`filters-003`](https://github.com/openrndr/openrndr-tutorials/tree/master/filters-003) using hot-swappable fragment shaders for editable filters

##### Image examples
 - [`image-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/image-001) loading and drawing an image
 - [`image-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/image-002) using `tint` to adjust colors
 - [`image-003`](https://github.com/openrndr/openrndr-tutorials/tree/master/image-003) drawing portions of an image many times

##### Manual presentation
 - [`manual-presentation-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/manual-presentation-001) disabling automatic swapping to safe cpu time

##### Mouse tutorials
 - [`mouse-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/mouse-001) using the position of the mouse cursor
 - [`mouse-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/mouse-002) using mouse events

##### Render target tutorials
 - [`render-targets-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/render-targets-001) rendering to a render target
 - [`render-targets-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/render-targets-002) compositing using multiple render targets

##### Slit scan tutorials
 - [`slit-scan-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/slit-scan-001) using a webcam to create a slitscan

##### SVG tutorials
 - [`svg-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/slit-scan-001) loading and drawing an SVG file

##### Text tutorials
 - [`text-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/text-001) writing text using the drawer
 - [`text-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/text-002) using the Writer to write multiline and boxed text

##### Video player tutorials
 - [`video-001`](https://github.com/openrndr/openrndr-tutorials/tree/master/video-001) video playback example
 - [`video-002`](https://github.com/openrndr/openrndr-tutorials/tree/master/video-002) video playback with post-processing example

## Importing the tutorial repository in IntelliJ
Follow the instructions for importing the [gradle template project](https://github.com/openrndr/openrndr-gradle-template) but use `https://github.com/openrndr/openrndr-tutorials` when prompted for the repository address.

## Running tutorials from the command line
You can run an individual example from the tutorial repository from the command line using `./gradlew :<project-name>:run`