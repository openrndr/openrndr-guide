# Should I use OPENRNDR ?

Yes!!

.. but—

## Target audience
If you are new to both computer graphics and programming itself we suggest to first get your feet wet in the
_Processing_ language. Processing is specifically developed for the beginning programmer and offers a likely less steep
learning curve. That said, we believe OPENRNDR is developed with an aim on simplicity and consistency and should not be
super hard to learn.

## Supported platforms
OPENRNDR currently targets desktop platforms including macOS, Windows and Linux.

For macOS we support versions 10.10 to 10.14. Older versions may work, but this is hard to verify in a structural
fashion.

For Windows we support Windows 10. Windows 8.1 may work, but we have no means to verify this.

For Linux we support Ubuntu 18.04 LTS. Other versions and distributions may work.

OPENRNDR requires a graphics accelerator that supports at least OpenGL 3.3. This includes relatively old GPUs like
nVidia 320M, Intel HD4000 but excludes Intel HD3000 for example.

## Supported programming languages
OPENRNDR is written in Kotlin and intended to run on the JVM. We believe Kotlin offers a well balanced programming
language that is both expressive and easy to read.

The library can likely be used from Java 8+ as that's one of the promises of Kotlin's Java-interop, however the APIs
that OPENRNDR provides are making extensive use of Kotlin-specific features that may not translate well to Java.

## Supported development environments

OPENRNDR is environment agnostic, however all our tutorial and reference material assumes Gradle and IntelliJ are used.

## Long-term support
We have not reached the point at which we can make promises regarding API stability. OPENRNDR is pre-1.0 software, which
imples we try not to break things, but at times we have to. We break in cases in which it is better to break than to continue
with inconsistencies in or incompleteness of the API.

