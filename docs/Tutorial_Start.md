# Getting Started with OPENRNDR #

OPENRNDR provides an application framework that allows its users to write applications that run on Microsoft Windows, MacOS and Linux platforms. For future versions we want to include the Android and Raspberry PI platforms as well.

## Requirements ##
 
 * A computer running Windows 10 (older versions may work but are untested) or MacOSX 10.10+
 * Java 8 or 9 JDK installed
 * [IntelliJ Idea](Tools_IntelliJIdea) Community or Enterprise edition

## Creating a new project

In Intellij: File -> New Project. Pick Maven project

If you are unfamiliar with Maven take some time to read our notes on [Maven](Tools_Maven)

## Configure OPENRNDR library

Edit the `pom.xml` file and add the following:

```xml
    <dependencies>
        <dependency>
            <groupId>org.openrndr</groupId>
            <artifactId>rndr</artifactId>
            <version>0.3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

After saving `pom.xml` IntelliJ Idea will ask you to import the changes made to the pom.xml, import the changes. In the background Maven will download RNDR and the libraries it depends on.

## Writing your first RNDR program

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
### Run the sketch

Hover your mouse over the `main()` method, right click and pick the `Run FirstSketch.main()` option in the pop-up menu. The sketch will now start.
