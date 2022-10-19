@file:Suppress("UNUSED_EXPRESSION")
@file:Title("File Input / Output")
@file:Order("1095")
@file:URL("fileIO/index")

package docs.`10_FileIO`

import org.openrndr.dokgen.annotations.*

fun main() {

    @Text
    """
    # File Input / Output

    ## Loading files
     
    ### Load file content into a `String`
    
    ```
    val fileContent = File("/path/to/file.txt").readText()
    ```
    
    ### Load file content into a `List<String>`
    
    ```
    val lines = File("/path/to/file.txt").readLines()
    ```
    
    ### Load binary content into a `ByteArray`
    
    ```
    val bytes = File("/path/to/file.txt").readBytes()
    ```
    
    For loading large text files one can use
    `File.bufferedReader()`.

    ## Saving files
    
    ### Save `String`
    
    ```
    File("/path/to/file.txt").writeText("Hello")
    ```
    
    ### Save `ByteArray`
    
    ```
    File("/path/to/file.txt").writeBytes(myByteArray)
    ```

    For saving large text files one can use
    `File.bufferedWriter()`.

    ## Loading a JSON file
    
    ## Saving a JSON file
    
    ## Loading a CSV file
    
    ## Saving a CSV file
    
    ## Loading an XML file
    
    ## Saving an XML file
    
    """
}