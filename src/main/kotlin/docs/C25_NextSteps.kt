@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Next steps")
@file:ParentTitle("What is OPENRNDR?")
@file:Order("25")
@file:URL("/nextSteps")

package docs

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.*

fun main() {
    @Language("markdown") val a =
    @Text
    """
    # Next steps
    
    ## Simplifying the Project View
    
    By default, the Project View can look intimidating.
    The list contains many files and folders, and you haven't even started writing a program! 
    But we can simplify it by hiding files that are not Kotlin programs.
    
    First, let's enable some new scopes for the Project View: 
    
    * Make the Gradle panel visible by clicking on the Gradle elephant on the right side of the screen.
    * In the panel that shows up, double-click on `openrndr-template > Tasks > openrndr > "add IDE file scopes"`.
    * Hide the Gradle panel by clicking on the elephant again.
    
    After running the `add IDE file scopes` task, let's enable the scope that shows code files:
    
    * Click on `Project ⌵` at the top-left of the Project View.
    * Choose `★ Code` at the bottom of the list.
    
    Only two Kotlin files should be listed, everything else is now hidden. Isn't that a relief?
    
    You can always switch back to the `Project` scope to see all files or switch to one of the others.
    For instance, the `★ Media` scope will display only sound, image and video files from your project.
    
    ## Multiple programs in one project
     
    By default, the `openrndr-template` project comes with two programs.
    One is called `TemplateProgram.kt` and the other `TemplateLiveProgram.kt`.
    
    The second one provides live-coding! If you run that program by clicking
    on the green triangle next to `fun main()`, any changes you make to the code
    will be visible in the running program after you save your changes. No need to 
    stop and start the program.
    
    But you are not limited to two programs, of course. In the Project View on the
    left, you can copy and paste `.kt` files (using keyboard shortcuts or the mouse).
    IntelliJ IDEA will ask you to name the new file (the name must start with a letter).
    It will also ask whether you want to add the new file to Git. 
    It's fine to click Cancel.
    
    If you created a third `.kt` file, make some changes to it and then click the 
    green triangle next to `fun main()` to run it.
    
    All your Kotlin programs must be placed under `src/main/kotlin`, otherwise 
    they won't run.
    
    Note: at the top of the window, there is also a green triangle to run programs.
    By default, clicking that green triangle will re-run the last program you ran, 
    no matter which file you are editing now. This can be surprising when you
    start editing a different program in your project. For a more intuitive behavior, 
    click on the drop-down on the left of the triangle and select `Current File`.
    After this change, the top green triangle will run whatever program you are 
    currently editing.

    ## `import` statements
    
    OPENRNDR is very modular. Not every possible feature is available by default:
    to make classes available, they need to be "imported". For example,
    if we want to create a `Circle` object, we need to add a line at the top of
    the program that imports the `Circle` class. After doing this, the compiler
    will understand what a `Circle` is. Fortunately, we do not need to
    type such import lines by hand. Let's see how IntelliJ IDEA helps with this.
    
    Inside the `extend { ... }` block, go ahead and type `val v = Vector3`. 
    When you do this, a floating dialog should appear. What this dialog is doing 
    is asking you "which Vector3 do you mean?". See, different classes may
    provide something that sounds like a Vector3. Some of these classes can be
    part of OPENRNDR, and other classes may be provided by Java or other frameworks.
    
    The one we are looking for is "Vector3 (org.openrndr.math)". 
    If it is highlighted in that floating dialog, press the `ENTER` key. 
    This will add the necessary `import` statement to the beginning
    of the file, making `Vector3` available for you.
    If the highlighted item was not the one we were looking for, press the
    `UP` and `DOWN` arrow keys to choose the right one, then press `ENTER`.
    
    If you accidentally import the wrong one, you can `undo`, or you can also 
    manually delete the import from the top of the file. 

    When an import is missing, the unrecognized word will be highlighted in red.
    Let's try this. Delete any `Vector3` related imports from the top of the file.
    The `Vector3` word will be highlighted as an error. When this happens, we can
    automatically import the right class by clicking the word (highlighted in red), 
    then press `ALT+ENTER` (or `Command+ENTER` on Mac). A tool tip will show up 
    letting you choose the right import to add.

    ## IntelliJ IDEA cleanup tips

    In IntelliJ IDEA, you can double-tap the `SHIFT` key to open a global search tool
    that will find whatever you type in your source code, but also in the program
    settings, menus and actions you can perform.
    
    Let's try it:
    
    * Double-tap `SHIFT` and type `optimize`. In the search results choose `optimize imports`.
      This will remove no longer used import statements from the beginning of your program.
    
    * Double-tap `SHIFT` and type `reformat`. In the search results choose `reformat code`. 
      This will tidy up the spacing and indentation of your program.

    ## What's next?
    
    At this point you are likely interested in how this program is structured. 
    The guide explains more in the 
    [Program basics](https://guide.openrndr.org/programBasics/application.html) chapter.  
    
    If you are more interested in reading source code, you can find the code 
    for the examples in this guide in the 
    [`openrndr-examples` repository](https://github.com/openrndr/openrndr-examples). 

    If you are interested in more advanced examples, we recommend checking out 
    the demo programs in the [`orx` repository](https://github.com/openrndr/orx); 
    most `orx` modules have demos in their `src/demo/kotlin` folder.
    """
}
