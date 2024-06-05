@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Pen plotters")
@file:ParentTitle("Use cases")
@file:Order("530")
@file:URL("useCases/penPlotterArt")

package docs.`95_Use_cases`

import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.drawComposition
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
        
    Find more tips on
    [using OPENRNDR with pen plotters](https://openrndr.discourse.group/t/openrndr-plotting-tricks-axidraw-etc/208)
    in the forum.
    
    """.trimIndent()
}