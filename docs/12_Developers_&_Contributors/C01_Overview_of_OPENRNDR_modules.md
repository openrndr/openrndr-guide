# Overview of OPENRNDR modules

OPENRNDR is organized in modules. The listing below describes the hierarchy and functionality per module.

 - `openrndr-core`, the core of OPENRNDR
   - `openrndr-binpack`, a binpacker, used internally for creating texture atlases.
   - `openrndr-color`, code related to working with color
   - `openrndr-event`, a simple event library
   - `openrndr-math`, code related to math, mostly computer graphics math
   - `openrndr-shape`, code related to 2d shapes
   - `openrndr-svg`, code related to reading and writing SVG files
 - `openrndr-gl3`, the OpenGL3 backend for OPENGL
   - `openrndr-gl3-natives-windows`, Windows specific native libraries
   - `openrndr-gl3-natives-macos`, MacOS specific native libraries
 - `openrndr-animatable`, optional animation library
 - `openrndr-ffmpeg`, code related to optional FFMPEG based video playback
 - `openrndr-filter`, optional library of filtering and post processing related code
