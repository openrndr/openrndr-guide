@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Pen plotters")
@file:ParentTitle("Use cases")
@file:Order("530")
@file:URL("useCases/penPlotterArt")

package docs.`95_Use_cases`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dialogs.saveFileDialog
import org.openrndr.dokgen.annotations.*
import org.openrndr.drawComposition
import org.openrndr.shape.draw
import org.openrndr.svg.saveToFile
import java.io.File

fun main() {
    @Text
    """
    # Generating pen plotter art
    
    A pen plotter is a computer output device that can draw
    with a pen (or a brush, marker, etc) on a paper.
    Some such devices move the pen in the X and Y axes, 
    while others can move the paper instead.
     
    Pen plotters were introduced in the 1950s and 60s
    and have become popular among artists since the mid 2000s. 

    Unlike other output devices like ink-jet and laser printers
    which accept pixels, pen plotters must be fed with vector
    data: lines and curves.
    
    OPENRNDR provides a rich toolset to generate and manipulate
    vector data.
        
    ## SVG vs g-code
    
    There are two main file formats used to send designs to pen plotters:
     
    - [SVG](https://en.wikipedia.org/wiki/SVG) (Scalable Vector Graphics), used with newer devices like the AxiDraw. 
    - [g-code](https://en.wikipedia.org/wiki/G-code), often supported by older plotters, CNC devices and laser cutters.
    
    OPENRNDR can easily load, manipulate, generate and save SVG files. There is a non-yet-merged contribution
    to add [g-code support](https://github.com/openrndr/orx/pull/285).
    
    The following sections focus on SVG.
    
    ## Hello world
    
    This is one of the simplest programs we can write
    to produce an SVG file containing just a circle.
    """

    @Code
    application {
        program {
            val design = drawComposition {
                circle(drawer.bounds.center, 200.0)
            }
            design.saveToFile(File("data/design.svg"))
        }
    }

    @Text
    """
    The API in the composition drawer is almost identical
    to the one of the standard drawer: we can use methods like
    `segment`, `contour`, `shape`, `circle`, `rectangle`, etc.
    
    Note: if you are using OPENRNDR / ORX version 0.4.5 you need to
    enable `orx-composition` and `orx-svg` in the `build.gradle.kts` file.

    ## Interaction
    
    Lets take our simple program a step further and make it interactive.
    We will generate a new design every time the mouse is clicked and
    save the design when we press the `s` key on the keyboard.
    """

    @Code
    application {
        program {
            // Create an empty composition
            val design = drawComposition { }

            // A function to draw concentric circles into the composition.
            fun generateDesign() {
                design.clear()
                val separation = 4.0
                val steps = (drawer.bounds.center.distanceTo(mouse.position) / separation).toInt()
                design.draw {
                    repeat(steps) {
                        circle(drawer.bounds.center, (it + 1) * separation)
                    }
                }
            }

            // Draw the composition onto the window
            extend {
                drawer.clear(ColorRGBa.WHITE)
                drawer.fill = null
                drawer.composition(design)
            }

            // Generate a new design every time we click the mouse
            mouse.buttonDown.listen {
                generateDesign()
            }

            // Show a save dialog when pressing the `s` key, then save the design
            // with the chosen file name into the selected folder.
            keyboard.keyDown.listen {
                if (it.name == "s") {
                    saveFileDialog(supportedExtensions = listOf("SVG" to listOf("svg"))) { file ->
                        design.saveToFile(file)
                    }
                }
            }
        }
    }

    @Text
    """
    With the last program we could create minimal designs by clicking
    the mouse button on different locations on the program window and
    then save the design when pressing the `s` key.
    
    If we open the resulting design in Inkscape we will notice that the
    document size is 640 pixels wide and 480 pixels height, matching the
    default OPENRNDR window size. In Inkscape we could choose a different
    document size (A4 for instance) then re-center and scale the design to
    match our preferences.
    
    Lets find out how to specify the SVG document dimensions, 
    and how to make what we see in the window match what gets exported
    as SVG. (Coming soon...)
    
           
    Find more tips on
    [using OPENRNDR with pen plotters](https://openrndr.discourse.group/t/openrndr-plotting-tricks-axidraw-etc/208)
    in the forum.
    
    """.trimIndent()
}
