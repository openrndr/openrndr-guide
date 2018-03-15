# Getting Started with OPENRNDR #

OPENRNDR provides an application framework that allows its users to write applications that run on Microsoft Windows and MacOS platforms. For future versions we want to include Desktop Linux, Android and Raspberry PI platforms as well.

## Requirements ##
 * A computer running Windows 10 (older versions may work but are untested) or MacOSX 10.10+
 * A graphics adapter and drivers that support OpenGL 3.3
 * Java 8 or 9 JDK installed
 * [IntelliJ Idea 2017.3](Tools_IntelliJIdea) Community or Enterprise edition

## Getting OPENRNDR

OPENRNDR is obtained by adding the OPENRNDR dependencies to your Gradle or Maven project. We offer [ready-to-use artifacts](http://dl.bintray.com/openrndr/openrndr/org/openrndr/) through Bintray.

When using Gradle add this custom repository to your `build.gradle`
```groovy
repositories {
   maven {
        url="https://dl.bintray.com/openrndr/openrndr/"
    }
}
```

Then add the following dependencies:
```groovy

project.ext.openrndrVersion = "0.3.3"

switch (org.gradle.internal.os.OperatingSystem.current()) {
    case org.gradle.internal.os.OperatingSystem.WINDOWS:
        project.ext.openrndrOS = "windows"
        break
    case org.gradle.internal.os.OperatingSystem.LINUX:
        project.ext.openrndrOS = "linux"
        break
    case org.gradle.internal.os.OperatingSystem.MAC_OS:
        project.ext.openrndrOS = "macos"
        break
}

dependencies {
  runtime "org.openrndr:openrndr-gl3:$openrndrVersion"
  runtime "org.openrndr:openrndr-gl3-natives-$openrndrOS:$openrndrVersion"
  compile "org.openrndr:openrndr-core:$openrndrVersion"
}
```

## Getting the tutorial code

A good start for working with OPENRNDR is found in the [tutorial repository](https://github.com/openrndr/openrndr-tutorials).

```sh
git clone https://github.com/openrndr/openrndr-tutorials.git
```

Also check out the section on [using the tutorial repository](Topic_TutorialRepository.md) for further explanations.

## Writing your first RNDR program

The easiest way to start a new project is to start from the provided template project
```sh
git clone --depth 1 https://github.com/openrndr/openrndr-gradle-template.git
```

This contains everything needed to start, notably the `build.gradle` with the right dependencies and the Gradle wrappers.


Add a new Kotlin class to the project, name it `program.FirstProgram`

```kotlin

import org.openrndr.Application
import org.openrndr.Configuration
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2

class FirstProgram: Program(){

    override fun draw() {
        drawer.background(ColorRGBa.BLACK)
        drawer.fill = ColorRGBa.WHITE
        drawer.circle(Vector2( width / 2.0, height / 2.0 ), 100.0)
    }
}

fun main(args:Array<String>) {
    Application.run(FirstProgram(), Configuration())
}

```
### Run your first program

In IntelliJ, hover your mouse over the `main()` method, right click and pick the `Run FirstSketch.main()` option in the pop-up menu. The sketch will now start.

On MacOS you will find that the program exits immediately with an error. To resolve this edit the run configuration (left of the play button in the main toolbar) and add
`-XstartOnFirstThread` to the VM arguments. The program should now work.