@file:Suppress("UNUSED_EXPRESSION")
@file:Title("MacOS and Windows tips")
@file:ParentTitle("What is OPENRNDR?")
@file:Order("30")
@file:URL("/macOSAndWindowsTips")

package docs

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    ## macOS: debugging and profiling
    
    Trying to *debug* or *profile* in macOS may display a warning or error message mentioning `-XstartOnFirstThread`.
    The solution is simple:
    
    * Open the `Run > Edit Configurations...` menu.
    * Add `-XstartOnFirstThread` in the `VM Options` [text field](https://stackoverflow.com/a/44184837).
    * Click `Ok` to close the dialog.
    
    ## Windows: multiple GPUs
    
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