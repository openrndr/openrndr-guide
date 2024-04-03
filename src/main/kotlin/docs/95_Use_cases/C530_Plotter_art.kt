@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Pen plotters")
@file:ParentTitle("Use cases")
@file:Order("530")
@file:URL("useCases/penPlotterArt")

package docs.`95_Use_cases`

import org.openrndr.dokgen.annotations.*

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
    
    Take a look in the forum for tips on
    [using OPENRNDR with pen plotters](https://openrndr.discourse.group/t/openrndr-plotting-tricks-axidraw-etc/208).
    
    """.trimIndent()
}