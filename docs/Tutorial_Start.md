# Getting Started with OPENRNDR #

OPENRNDR provides an application framework that allows its users to write applications that run on Microsoft Windows, macOS and Linux platforms. For future versions we want to include the Android and Raspberry PI platforms as well.

## Requirements ##
 
 * A computer running Windows 7+ or OSX 10.10+
 * Java 8 JDK installed
 * [IntelliJ Idea](Tools_IntelliJIdea) Community or Enterprise edition

## Creating a new project

In Intellij: File -> New Project. Pick Maven project

If you are unfamiliar with Maven take some time to read our notes on [Maven](Tools_Maven)

## Configure OPENRNDR library

Edit the `pom.xml` file and add the following:

```xml
    <dependencies>
        <dependency>
            <groupId>net.lustlab.rndr</groupId>
            <artifactId>rndr</artifactId>
            <version>0.2.8-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>lustlab</id>
            <url>http://server.lustlab.net/edwin/maven</url>
            <name>lustlab</name>
        </repository>
    </repositories>
```

After saving `pom.xml` IntelliJ Idea will ask you to import the changes made to the pom.xml, import the changes. In the background Maven will download RNDR and the libraries it depends on.

## Writing your first RNDR program

Add a new Java class to the project, name it `program.FirstProgram`

```java

import net.lustlab.rndr.program.Program;
public class FirstProgram extends Program {

    public void draw() {
          background(ColorRGBa.BLACK);
          drawer.fill(ColorRGBa.WHITE);
          drawer.circle(width/2, height/2, 100);
    }   

    public static void main(String[] args) {
        Application.run(new FirstProgram(), new Configuration().size(600,600));
    }
}

```
### Run the sketch

Hover your mouse over the `main()` method, right click and pick the `Run FirstSketch.main()` option in the pop-up menu. The sketch will now start.
