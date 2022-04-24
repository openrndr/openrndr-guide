@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Set up your first program")
@file:ParentTitle("Getting started with OPENRNDR")
@file:Order("100")
@file:URL("gettingStartedWithOPENRNDR/setUpYourFirstProgram")

package docs.`02_Getting_Started_with_OPENRNDR`

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    # Getting Started with OPENRNDR
    
    Let's get it started!
    
    ## Setting up software prerequisites
    
    Now, to be able to edit and run OPENRNDR programs we have to install some software tools. 
    
     * Check if your computer has `git` installed. If it's missing, install it 
       from the [Git website](https://git-scm.com/) 
    
     * Download [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download) and install it. On the first run 
     it will offer the option to pick default settings and continue, use the default settings.
     
    ## Clone the template
    
    The advised and time-economic way to start any OPENRNDR-based project is to use the [`openrndr-template`](https://github.com/openrndr/openrndr-template).
    This template contains a ready-to-go project from which you can quickly start running your first programs.
    
     * Find the "New project from version control" item in the "File" menu. Pick the "Git" option.
     * Enter or (copy/paste) `https://github.com/openrndr/openrndr-template` in the URL-field and validate that the directory is OK. 
     * When asked where the project should be opened, click on the new window 
       button. Make sure to link to the gradle project when prompted.
    
    If cloning fails, check if you have Git installed and if the repository url was typed correctly.
    
    ## Run your first program
    
    After cloning the template you will likely see IntelliJ IDEA download dependencies and index your project, this may take some time on the first run. You can see its progress
    in the status bar at the bottom of the IntelliJ window.
    
    You may have noticed that IntelliJ has opened the `README.md` in the `openrndr-template` project. Likely this file will
    contain some hints to help you navigate the template project. 
     
    Once IntelliJ has finished indexing find the `src/main/kotlin/TemplateProgram.kt` file in the project view. 
    On some versions of IntelliJ the project view is collapsed by default, you can open it by clicking on the vertical tab on the left side of the screen. 
     
    Once the template program is opened a small green triangle should appear next to the line that starts with `fun main()`. Click on the triangle,
    the program should now run.
    
    *Lo' and behold!*
    
    ## What's next?
    
    At this point you are likely interested in how this program is structured. The guide explains more in the [Program basics](https://guide.openrndr.org/programBasics/applicationProgram.html) chapter.  
    
    If you are more interested in reading source code you can find the code for the examples in this guide in the [`openrndr-examples` repository](https://github.com/openrndr/openrndr-examples). If you are interested in more advanced examples we recommend checking out the demo programs in the [`orx` repository](https://github.com/openrndr/orx), most `orx` modules have demos in the `src/demo/kotlin` folder.
    """
}