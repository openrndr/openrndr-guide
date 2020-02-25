# Getting Started with OPENRNDR #

Let's get it started!


## Hardware requirements ##

Before you start check if your computer meets all of the below requirements.

 * A computer running any of our supported platforms; Windows 10, macOS 10.8+ or Linux/x64
 * A graphics adapter with drivers that support at least OpenGL 3.3

This covers most desktop computers made after 2010. 

## Setting up software prerequisites

Now, to be able to edit and run OPENRNDR programs we have to install some software tools. 

 * Check if your computer has `git` installed, if it has not install from the [Git website](https://git-scm.com/) 

 * Download [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download) and install it. On the first run 
 it will offer the option to pick default settings and continue, use the default settings.
 
Installing an additional JDK is no longer required as since version 2019.3 IntelliJ IDEA ships with a built-in version of JDK 11.  
 
## Clone the template

The adviced and time-economic way to start any OPENRNDR-based project is to use the [`openrndr-template`](https://github.com/openrndr/openrndr-template).
This template contains a ready-to-go project from which you can quickly start running your first programs.

 * Find the "New project from version control" item in the "File" menu. Pick the "Git" option.
 * Enter or (copy/paste) `https://github.com/openrndr/openrndr-template` in the URL-field and validate that the directory is OK. 
 * When asked where the project should be opened click on the new window button. Make sure to link to the gradle project when
prompted.

## Run your first program

After cloning the template you will likely see IntelliJ IDEA indexing your project, this may take some time on the first run. You can see its progress
in the statusbar at the bottom of the IntelliJ window.

You may have noticed that IntelliJ has opened the `README.md` in the `openrndr-template` project. Likely this file will
contain some hints to help you navigating the template project. 
 
Once IntelliJ has finished indexing find the `src/main/kotlin/TemplateProgram.kt` file in the project view. The project view is collapsed
by default, you can open it by clicking on the vertical tab on the left side of the screen. 
 
Once the template program is opened a small green triangle should appear next to the line that starts with `fun main()`. Click on the triangle,
the program should now run.

*Lo' and behold!*

At this point you are likely interested in how this program is structured. The guide explains more in the [Program basics](03_Program_basics/C00_ApplicationProgram) chapter.  
