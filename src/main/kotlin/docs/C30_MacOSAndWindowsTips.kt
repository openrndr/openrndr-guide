@file:Suppress("UNUSED_EXPRESSION")
@file:Title("MacOS and Windows tips")
@file:ParentTitle("What is OPENRNDR?")
@file:Order("30")
@file:URL("/macOSAndWindowsTips")

package docs

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.*

fun main() {
    @Language("markdown") val a =
    @Text
    """
    ## Tip for macOS users
    
    When running a program, the console will display 
    `Warning: Running on macOS without -XstartOnFirstThread JVM argument`.
    To clear this warning (and enable debugging):
    
    * Open the `Run > Edit Configurations...` menu.
    * Add `-XstartOnFirstThread` in the `VM Options` [text field](https://stackoverflow.com/a/44184837).
    * Click `Ok` to close the dialog.
    
    ## Tip for Windows multi-GPU users
    
    If your computer has multiple GPUs, you can choose which one OPENRNDR uses like this:
    
    * Run the openrndr-template program by clicking on the green triangle.
      In IntelliJ IDEA, look at the console in the bottom area and 
      note down the full path of the `java.exe` program being used. 
      It probably starts with something like `C:\Users\...`.
    * Open the Windows Graphics Settings.
    * Click Browse and find the exact same `java.exe` you noted down earlier.
    * Click Options and choose your preferred GPU, then click Save.
    """
}