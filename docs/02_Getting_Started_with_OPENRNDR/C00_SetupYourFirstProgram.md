# Getting Started with OPENRNDR #

OPENRNDR provides an application framework that allows its users to write applications that run on Microsoft Windows, MacOS platforms and Linux/x64 systems.

## Requirements ##
 * A computer running any of our supported platforms.
 * A graphics adapter and drivers that supports OpenGL 3.3
 * JDK 8, 9, 10, 11, 12 installed. Latest (12) recommended. Both OpenJDK and OracleJDK work.
 * [IntelliJ IDEA 2019.2](https://www.jetbrains.com/idea/download/) Community or Enterprise edition
 * A recent version of [Git](https://git-scm.com/)

## Setting up prerequisites
Before you start working with OPENRNDR please follow these instructions.
 * Install a Java Development KIT (JDK): [JDK installer for Windows](https://cdn.azul.com/zulu/bin/zulu12.1.3-ca-jdk12-win_x64.msi) [JDK installer for macOS](https://cdn.azul.com/zulu/bin/zulu12.1.3-ca-jdk12-macosx_x64.dmg)
 * Download [IntelliJ Idea Community Edition](https://www.jetbrains.com/idea/download) and install it.

## Getting OPENRNDR

OPENRNDR is obtained by adding the OPENRNDR dependencies to your Gradle project. You don't have to download anything manually.

## Setting up OPENRNDR in IntelliJ IDEA

The easiest way to get started is to use the [`openrndr-template`](https://github.com/openrndr/openrndr-template) repository.
This template is based on a gradle build file that includes dependencies on the core OPENRNDR libraries. In the template
you will also find hints on how to add additional OPENRNDR and ORX libraries.

After starting IntelliJ IDEA. Find the "New project from version control" item in the "File" menu. Pick the "Git" option.
Enter `https://github.com/openrndr/openrndr-template` in the url field and validate that the directory is ok.
When asked where the project should be opened click on the new window button. Make sure to click on the link gradle
option.

Once IntelliJ has finished indexing find the `src/main/kotlin/TemplateProgram.kt` file in the project view and open it.
Once opened a small green triangle should appear next to the line that starts with `fun main()`. Click on the triangle,
the program should now run.
