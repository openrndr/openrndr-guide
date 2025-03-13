@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Drawing SVG")
@file:ParentTitle("Drawing")
@file:Order("160")
@file:URL("drawing/drawingSVG")

package docs.`30_Drawing`

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.math.Vector2
import org.openrndr.shape.*
import org.openrndr.svg.loadSVG
import org.openrndr.svg.saveToFile
import java.io.File

fun main() {
    @Text 
    """
    # Drawing SVG
    
    Loading a composition and drawing it can be done as follows:
    """

    @Code
    application {
        program {
            val composition = loadSVG("data/drawing.svg")
            extend {
                drawer.composition(composition)
            }
        }
    }

    @Text 
    """
    Note that OPENRNDR's support for SVG files works best with SVG files that are saved in the Tiny SVG 1.x profile .
    
    
    When drawing a composition from SVG you will notice that fill and stroke colors from the SVG file are used over the `Drawer` colors.
    
    ## Compositions
    
    Here a `Composition` is a tree structure with `Composition.root` as its root node.
    
    ##### CompositionNode types
    
    Node Type         | Function
    ------------------|-----------------------------------------
    `GroupNode`       | Holds multiple child nodes in `children`
    `ShapeNode`       | Holds a a single shape in `shape`
    `TextNode`        | Holds text (currently not implemented)
    `ImageNode`       | Holds an image (currently not implemented)
    `CompositionNode` | The base class for composition node
    
    ##### CompositionNode properties
    
    Property name                | Property type      | Description
    -----------------------------|--------------------|------------
    `transform`                  | `Matrix44`         | local transformation
    `fill`                       | `CompositionColor` | fill color
    `stroke`                     | `CompositionColor` | stroke color
    `id`                         | `String?`          | node id
    `parent`                     | `CompositionNode?` | node parent
    `effectiveFill` (read-only)  | `ColorRGBa?`       | the effective fill color, potentially inherited from ancestor
    `effectiveStroke`(read-only) | `ColorRGBa?`       | the effective stroke color, potentially inherited from ancestor
    
    ##### GroupNode properties
    
    Property name   | Property type                   | Description
    ----------------|---------------------------------|---------------
    `children`      | `MutableList<CompositionNode>`  | child nodes
    
    ##### ShapeNode properties
    
    Property name   | Property type  | Description
    ----------------|----------------|---------------
    `shape`         | `Shape`        | a single shape
    
    ## Querying the composition
    
    ### Finding all shapes in the composition
    
    ```kotlin
    val shapeNodes = composition.findShapes()
    ```
    
    ## Modifying the composition
    
    Since a `Composition` contains many immutable parts it is easier to (partially) replace parts of the composition.
    
    ```kotlin
    val m = translate(1.0, 0.0, 0.0);
    composition.root.map {
      if (it is ShapeNode) {
        it.copy(shape=it.shape.transform(m))
      } else {
        it
      }
    }
    ```
    ## Creating compositions manually
    
    Compositions are tree structures that can be constructed manually. Below you find an example of constructing a `Composition` 
    in code.
    """

    @Code.Block
    run {
        val root = GroupNode()
        val composition = Composition(root)
        val shape = Circle(200.0, 200.0, 100.0).shape
        val shapeNode = ShapeNode(shape)
        shapeNode.fill = ColorRGBa.PINK
        shapeNode.stroke = ColorRGBa.BLACK
        // -- add shape node to root
        root.children.add(shapeNode)
    }

    @Text 
    """
       ## Composition Drawer
       
       OPENRNDR has a much more convenient interface for creating Compositions. The idea behind this
       interface is that it works in a similar way to `Drawer`. 

       Below we use `drawComposition {}` to reproduce the same composition as in the previous example.
    """

    @Code.Block
    run {
        val composition = drawComposition {
            fill = ColorRGBa.PINK
            stroke = ColorRGBa.BLACK
            circle(Vector2(100.0, 100.0), 50.0)
        }
    }

    @Text 
    """
    ### Transforms
        
    Transforms work in the same way as in `Drawer`
    """
    @Code.Block
    run {
        val composition = drawComposition {
            fill = ColorRGBa.PINK
            stroke = ColorRGBa.BLACK
            isolated {
                for (i in 0 until 100) {
                    circle(Vector2(0.0, 0.0), 50.0)
                    translate(50.0, 50.0)
                }
            }
        }
    }

    @Text 
    """
    Saving compositions to SVG
    
    Compositions can be saved to SVG using the `saveToFile` function.
    """

    @Code.Block
    run {
        // -- load in a composition
        val composition = loadSVG("data/example.svg")
        composition.saveToFile(File("output.svg"))
    }

}