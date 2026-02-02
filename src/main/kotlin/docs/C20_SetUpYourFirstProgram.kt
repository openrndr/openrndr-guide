@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Set up your first program")
@file:ParentTitle("What is OPENRNDR?")
@file:Order("20")
@file:URL("/setUpYourFirstProgram")

package docs

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.*

fun main() {
    @Language("markdown") val a =
    @Text
    """
    # Getting Started with OPENRNDR
    
    ## Download the code editor
    
    To edit and run OPENRNDR programs, we will install an IDE (Integrated Development Environment)
    called IntelliJ IDEA. 
    
     * Download and install [IntelliJ IDEA](https://www.jetbrains.com/idea/download). 
     * Start IntelliJ. On the first run it will offer the option to pick 
       your default settings and continue. Use the default settings.

    ## Download the template program
    
    Instead of creating a project from scratch, the simplest way to start an OPENRNDR-based project is to 
    use the [`openrndr-template`](https://github.com/openrndr/openrndr-template):
    
     * If you are running IntelliJ IDEA for the first time, the program menus
        are hidden. In that case click "Clone Repository" at the top-right area.
       * If program menus are visible, select "File > New > Project from version control...".
     * In the dialog that appears:
       * Version Control: choose "Git".
       * URL: enter `https://github.com/openrndr/openrndr-template`
       * Directory: ensure it looks OK. 
     * When asked where the project should be opened, click on "new window".
    
    If downloading the template fails:

      * Make sure [Git](https://git-scm.com/downloads) is installed.
      * Did you enter the repository URL correctly?
    
    ## Run the template program
    
    After downloading the template, IntelliJ IDEA will download the necessary dependencies.
    See the progress in the status bar at the bottom of the window.
    
    When IntelliJ IDEA opens a project for the first time, 
    its `README.md` file is presented automatically. 
    This file contains hints to help you navigate the template project. 
     
    When the status bar shows no more activity,
    go to the Project view (left panel) and
    click on `src/main/kotlin/TemplateProgram.kt` to open that file 

    Note: the Project view can be shown and hidden by clicking the Project
    button in the top-left area of the window.
     
    Once the source code of TemplateProgram.kt is visible, a small green triangle will appear 
    before the words `fun main()`. Click that triangle to run the program.
    
    *Ta-da!*
    """
}
