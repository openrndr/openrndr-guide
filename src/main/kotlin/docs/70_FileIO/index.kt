@file:Suppress("UNUSED_EXPRESSION")
@file:Title("File Input / Output")
@file:Order("1070")
@file:URL("fileIO/index")

package docs.`70_FileIO`

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.openrndr.application
import org.openrndr.dokgen.annotations.*
import org.openrndr.extra.noise.uniform
import org.openrndr.math.Vector2
import java.io.File

fun main() {

    @Text
    """
    # File Input / Output
    
    ## Loading files
    
    ### Load a text file into a `String`
    
    ```kotlin
    val fileContent = File("/path/to/file.txt").readText()
    ```
    
    ### Load a text file into a `List<String>`
    
    ```kotlin
    val lines = File("/path/to/file.txt").readLines()
    ```
    
    ### Load binary content into a `ByteArray`
    
    ```kotlin
    val bytes = File("/path/to/file.txt").readBytes()
    ```
    
    For loading large text files one can use
    `File.bufferedReader()`.
    
    ## Saving files
    
    ### Save a `String`
    
    ```kotlin
    File("/path/to/file.txt").writeText("Hello")
    ```
    
    ### Save a `ByteArray`
    
    ```kotlin
    File("/path/to/file.txt").writeBytes(myByteArray)
    ```
    
    For saving large text files one can use
    `File.bufferedWriter()`.
    
    ## JSON
    
    The `kotlinx-serialization` library makes it possible to convert 
    JSON files to Kotlin objects and back. The library is enabled
    by default in recent versions of the openrndr-template.
    
    ### Loading / converting JSON to an object
    """

    @Code.Block
    run {
        // An object matching the JSON file.
        // Notice how optional properties are nullable.
        @Serializable
        data class Entry(
            var time: Double,
            var easing: String,
            var rotx: Double?,
            var roty: Double?,
            var x: Double?,
            var y: Double?,
            var scale: Double?,
            var jitter: Double?
        )

        // from disk
        // val json = File("/path/to/a/file.json").readText()

        // from a string
        val json = """
        [
            {
                "time": 0.0,
                "easing": "cubic-in-out",
                "rotx": 0.0,
                "roty": 30.0,
                "x": 0.0,
                "y": 0.0,
                "scale": 1.0,
                "jitter": 0.0
            },
            {
                "time": 1.0,
                "easing": "cubic-in-out",
                "rotx": 0.0,
                "x": 20.0,
                "y": 0.0,
                "scale": 1.0
            },
            {
                "time": 1.3,
                "easing": "cubic-in-out"
            }
        ]
        """.trimIndent()

        fun main() = application {
            program {
                val entries = Json.decodeFromString<List<Entry>>(json)

                entries.forEachIndexed { i, entry ->
                    println(i)
                    println(entry)
                }
            }
        }
    }

    @Text
    "### Saving an object as a JSON file"

    @Code
    application {
        program {
            val points = List(20) { Vector2.uniform(drawer.bounds) }
            val json = Json.encodeToString(points)
            File("data/points.json").writeText(json)
        }
    }

    @Text
    """
    ## CSV
    
    The [kotlin-csv](https://github.com/doyaaaaaken/kotlin-csv) library
    enables loading and saving [CSV](https://en.wikipedia.org/wiki/Comma-separated_values) 
    files. To enable the library:
    1. Find the `build.gradle.kts` file in your openrndr-template.
    2. Uncomment the line `implementation(libs.csv)`.
    3. Reload gradle.
    
    ### Load a CSV file
    """

    @Code
    application {
        program {
            // read from `String`
            val csvData = "a,b,c\nd,e,f"
            val rowsA = csvReader().readAll(csvData)

            // read from `java.io.File`
            val file = File("test.csv")
            val rowsB = csvReader().readAll(file)
        }
    }

    @Text
    """### Save a CSV file"""

    @Code
    application {
        program {
            val rows = listOf(listOf("a", "b", "c"), listOf("d", "e", "f"))
            csvWriter().writeAll(rows, "data/test.csv")
        }
    }

    @Text
    """
    ## XML / HTML
    
    The [jsoup](https://jsoup.org/) library enables loading and parsing XML and HTML files. 
    To enable the library:
    
    1. Find the `build.gradle.kts` file in your openrndr-template.
    2. Uncomment the line `implementation(libs.jsoup)`.
    3. Reload gradle.
    
    ### Get Wikipedia home page and parse news headlines
    
    """

    @Code
    application {
        program {
            val doc = Jsoup.connect("https://en.wikipedia.org/").get()
            println(doc.title())

            val newsHeadlines = doc.select("#mp-itn b a")
            newsHeadlines.forEach {
                println(it.attr("title"))
                println(it.absUrl("href"))
            }
        }
    }
}