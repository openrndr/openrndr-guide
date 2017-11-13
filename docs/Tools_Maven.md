# Maven #

Maven is a build system for Java. Maven takes a project definition (written in a pom.xml file) and compiles the code for the project.

We use Maven because it makes it easy to add libraries to a project. For example the RNDR library dependency is added to a project as follows:

```xml
<dependencies>
    <dependency>
        <groupId>net.lustlab.rndr</groupId>
        <artifactId>rndr</artifactId>
        <version>0.2.1</version>
    </dependency>
</dependencies>
```