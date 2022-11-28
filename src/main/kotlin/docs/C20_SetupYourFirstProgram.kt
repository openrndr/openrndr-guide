@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Set up your first program")
@file:ParentTitle("What is OPENRNDR?")
@file:Order("20")
@file:URL("/setUpYourFirstProgram")

package docs

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    # Getting Started with OPENRNDR
    
    Let's get it started!
    
    ## Setting up software prerequisites
    
    Now, to be able to edit and run OPENRNDR programs we have to install some 
    software tools. 
    
     * Check if your computer has `git` installed. If it's missing, install it 
       from the [Git website](https://git-scm.com/) 
    
     * Download [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download) 
     and install it. On the first run it will offer the option to pick 
     default settings and continue, use the default settings.
     
    ## Clone the template
    
    The advised and time-economic way to start any OPENRNDR-based project is to 
    use the [`openrndr-template`](https://github.com/openrndr/openrndr-template).
    This template contains a ready-to-go project from which you can quickly 
    start running your first programs.
    
     * If program menus are visible select "File > New > Project from version control...".
       If you are running Idea for the first time or no project is open, the program menus
        are not visible. In that case click "Get from VCS" at the top-right area instead. 
     * In the dialog that appears:
       * Version Control: choose "Git".
       * URL: enter `https://github.com/openrndr/openrndr-template`
       * Directory: validate that it looks OK. 
     * When asked where the project should be opened, click on "new window".
    
    If cloning fails, check if you have Git installed and if the repository URL 
    was typed correctly.
    
    ## Run your first program
    
    After cloning the template you will likely see IntelliJ IDEA download 
    dependencies and index your project, this may take some time on the 
    first run. You can see its progress in the status bar at the bottom 
    of the IntelliJ window.
    
    You may have noticed that IntelliJ has opened the `README.md` in the 
    `openrndr-template` project. Likely this file will
    contain some hints to help you navigate the template project. 
     
    Once IntelliJ has finished indexing 
    click the `src/main/kotlin/TemplateProgram.kt` file in the Project view. 
    On some versions of IntelliJ the Project view is collapsed by default, 
    you can open it by clicking on the vertical tab on the left side of the screen. 
     
    Once the template program is opened a small green triangle should appear 
    next to the line that starts with `fun main()`. Click on the triangle,
    the program should now run.
    
    *Lo' and behold!*
    
    ## Tip for macOS users
    
    When running a program the console will display 
    `Warning: Running on macOS without -XstartOnFirstThread JVM argument`.
    To clear this warning (and enable debugging):
    - Open the `Run > Edit Configurations...` menu.
    - Add `-XstartOnFirstThread` in the `VM Options` [text field](https://stackoverflow.com/a/44184837).
    - Click `Ok` to close the dialog.
    
    ## What's next?
    
    At this point you are likely interested in how this program is structured. 
    The guide explains more in the 
    [Program basics](https://guide.openrndr.org/programBasics/applicationProgram.html) chapter.  
    
    If you are more interested in reading source code you can find the code 
    for the examples in this guide in the 
    [`openrndr-examples` repository](https://github.com/openrndr/openrndr-examples). 
    If you are interested in more advanced examples we recommend checking out 
    the demo programs in the [`orx` repository](https://github.com/openrndr/orx), 
    most `orx` modules have demos in the `src/demo/kotlin` folder.
    """
}