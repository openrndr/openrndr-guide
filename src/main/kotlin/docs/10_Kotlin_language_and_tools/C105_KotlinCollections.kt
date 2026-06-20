@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Kotlin collections")
@file:Order("105")
@file:ParentTitle("Kotlin language and tools")
@file:URL("kotlinLanguageAndTools/kotlinCollections")

package docs.`10_Kotlin_language_and_tools`

import org.intellij.lang.annotations.Language
import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.dokgen.annotations.*
import org.openrndr.math.Vector2
import org.openrndr.shape.LineSegment

fun main() {
    @Language("markdown") val a =
        @Text
        """
    # Collection in Kotlin
        
    The Collections in Kotlin's standard library are powerful tools 
    for computational design and creative purposes.
    It provides Lists, Maps and Sets with mutable and immutable variants.
    
    Each of these data structures has many methods for querying and manipulating its elements,
    which can then be made visible using OPENRNDR's drawing capabilities.
    
    ## List
    
    A list is a collection of elements of the same type.
    Let's see some immutable `List` examples:
    """

    @Code.Block
    run {
        // Create an immutable list with specific Integers
        val list = listOf(1, 2, 3, 5, 7)

        // Accessing elements
        println(list[0]) // 1
        println(list.first()) // 1
        println(list.last()) // 7

        println(list.size) // 5
        println(list.lastIndex) // 4
    }

    @Text
    """
    We can also create lists using the `List` constructor:    
    """

    @Code.Block
    run {
        // Generate a list of 10 random numbers
        val randomNumbers = List(10) {
            Math.random().toInt()
        }

        // Generate a list of even numbers
        val evenNumbers = List(10) {
            it * 2
        }
    }

    @Text
    """
    Once we have a list, we can iterate over its elements:    
    """

    @Code.Block
    run {
        val list = listOf(1, 2, 3, 4, 5)
        for (item in list) {
            println(item)
        }
    }

    @Text
    """
    `forEach` and `forEachIndexed` can also be used 
    to iterate over a list, although breaking 
    out of the loop is not possible with them.    
    """

    @Code.Block
    run {
        val list = listOf(2, 7, 7, 1, 100)

        list.forEach { println(it) }

        list.forEachIndexed { index, item ->
            println("$index: $item")
        }
    }

    @Text
    """
    Mutable lists behave mostly like immutable lists, but 
    they can be modified:    
    """

    @Code.Block
    run {
        val mutableList = mutableListOf(1, 2, 3, 4, 5)
        mutableList.add(6) // 1, 2, 3, 4, 5, 6
        mutableList.remove(0) // 2, 3, 4, 5, 6
    }

    @Text
    """
    Lists provide many useful methods, such as `filter`, `map`, 
    `sortedBy`, `groupBy`, `chunked`, and others. We will not
    discuss them all in detail in this introduction, but 
    here some examples:
    """

    @Code.Block
    run {
        val list = List(16) { it } // Integers between 0 and 15
        val evenNumbers = list.filter { it % 2 == 0 }
        val squares = list.map { it * it }
        val shuffled = list.shuffled()
        val first5 = list.take(5)
        val last5 = list.takeLast(5)
        val asDouble = list.map { it.toDouble() }
    }

    @Text
    """
    ## Visual examples
        
    We haven't yet taken a look at any drawing operations, 
    so don't worry if you don't yet fully understand how that
    works, it will be introduced in next chapters.
    
    These examples are just to give you a taste of how
    lists can be used to create visual output.
    
    Here a visual example with a list containing the
    radii of a 20 circles:
    """

    @Media.Image "../media/collections-001.jpg"

    @Application
    @ProduceScreenshot("media/collections-001.jpg")
    @Code
    application {
        @Exclude
        configure {
            height = 300
        }
        program {
            // A list containing 1.0, 2.0, 5.0, 10.0, 17.0 ...
            val radii = List(20) { i -> 1.0 + i * i }
            extend {
                drawer.clear(ColorRGBa.WHITE)
                drawer.fill = null
                drawer.stroke = ColorRGBa.PINK
                radii.forEach { radius ->
                    drawer.strokeWeight = radius * 0.1
                    drawer.circle(drawer.bounds.center, radius)
                }
            }
        }
    }
    @Text
    """
    And one final example populating a list of `Double`
    values, then filtering out some items, and finally mapping
    the remaining elements into `LineSegment` instances.
    """

    @Media.Image "../media/collections-002.jpg"

    @Application
    @ProduceScreenshot("media/collections-002.jpg")
    @Code
    application {
        @Exclude
        configure {
            height = 300
        }
        program {
            // Generate a list containing horizontal pixel positions
            val x = List(width) { it.toDouble() }

            // Drop list items with an increasing probability,
            // so high `x` values are unlikely
            val filtered = x.filter {
                // `xNormalized` is 0.0 on the left edge and 1.0 on
                // the right edge of the window
                val xNormalized = it / width
                // Math.random() returns values between 0.0 and 1.0
                Math.random() > xNormalized
            }

            // Map the remaining items into drawable LineSegment instances
            val segs = filtered.map {
                LineSegment(it, 0.0, it, height.toDouble())
            }
            extend {
                drawer.clear(ColorRGBa.WHITE)
                drawer.stroke = ColorRGBa.PINK
                drawer.lineSegments(segs)
            }
        }
    }

    @Text
    """
    ## Map
    
    Maps and Sets are arguably more niche data structures than lists,
    but it's still worth mentioning them.
    
    A map is a collection of key-value pairs, sometimes known as a dictionary.
    There are two main constructors for maps: `mapOf()` and `mutableMapOf()`.
    """

    @Code.Block
    run {
        // Create maps from integers to strings
        val map = mapOf(1 to "one", 2 to "two", 3 to "three")
        val mutableMap = mutableMapOf(1 to "one", 2 to "two", 3 to "three")

        println(map[2]) // two
        mutableMap[4] = "four" // add `4 to "four"` to the mutable map
        mutableMap.remove(2) // remove a pair from the mutable map

        // Create a map from a list of numbers to their squares
        val squares = listOf(1, 3, 7).associateWith { it * it }
        println(squares) // {1=1, 3=9, 7=49}
    }

    @Text
    """
    An example with a mutable map containing positions to colors.
    """

    @Media.Image "../media/collections-003.jpg"

    @Application
    @ProduceScreenshot("media/collections-003.jpg")
    @Code
    application {
        @Exclude
        configure {
            height = 300
        }
        program {
            // Notice how we can specify the key and value types
            val data = mutableMapOf<Vector2, ColorRGBa>()
            // Populate the map with 10 positions and colors
            repeat(10) {
                val pos = Vector2(60.0 + 50.0 * it, height * 0.5)
                val color = ColorHSVa(it * 36.0, 0.7, 1.0).toRGBa()
                data[pos] = color
            }
            extend {
                drawer.clear(ColorRGBa.WHITE)
                // Read the map using the positions and colors
                data.forEach { (pos, c) ->
                    drawer.fill = c
                    drawer.circle(pos, 50.0)
                }
            }
        }
    }

    @Text
    """
    ## Set
    
    A set is a collection of *unique* elements.
    There are two main constructors for sets:
    `setOf()` and `mutableSetOf()`.
    """

    @Code.Block
    run {
        // Immutable set
        val s = setOf(1, 2, 3, 4, 5, 5, 5)
        println(s) // [1, 2, 3, 4, 5]
        println(s.contains(6)) // false

        // Mutable set
        val mutableS = mutableSetOf(1, 2, 3, 4, 5)
        mutableS.add(6) // [1, 2, 3, 4, 5, 6]
        mutableS.remove(3) // [1, 2, 4, 5, 6]
    }

    @Text
    """
    One can also convert a list into a set to make sure
    its elements are unique:
    """

    @Code.Block
    run {
        val nonUnique = List(20) {
            (Math.random() * 10).toInt()
        }
        val unique = nonUnique.toSet()
        println(nonUnique) // [4, 1, 4, 1, 0, 1, 4, 1, 1, 0]
        println(unique) // [4, 1, 0]
    }

    @Text
    """
    There are endless possibilities with Lists, Sets, and Maps.
    Learn more about them in the [Kotlin standard library documentation](https://kotlinlang.org/docs/collections-overview.html).
    """
}