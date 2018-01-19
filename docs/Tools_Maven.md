# Maven #

Maven is a build automation tool used primarily for Java projects. In Yiddish, the word maven means "accumulator of knowledge". Maven addresses two aspects of building software: first, it describes how software is built, and second, it describes its dependencies. Maven takes a project definition (written in a pom.xml file) and compiles the code for the project.

A barebones maven project for an OPENRNDR project will look like the following:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.openrndr</groupId>
    <artifactId>openrndr-maven-template</artifactId>
    <version>0.3</version>

    <properties>
        <openrndr-version>0.3</openrndr-version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.openrndr</groupId>
            <artifactId>openrndr-core</artifactId>
            <version>${openrndr-version}</version>
        </dependency>
        <dependency>
            <groupId>org.openrndr</groupId>
            <artifactId>openrndr-gl3</artifactId>
            <version>${openrndr-version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.openrndr</groupId>
            <artifactId>openrndr-gl3-natives-macos</artifactId>
            <version>${openrndr-version}</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```

A prepared [full example](https://bitbucket.org/rndrnl/openrndr-maven-template) is avaiable.
