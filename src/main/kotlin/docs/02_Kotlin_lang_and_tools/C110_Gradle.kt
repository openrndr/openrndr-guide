@file:Suppress("UNUSED_EXPRESSION")
@file:Title("What is Gradle?")
@file:ParentTitle("Kotlin language and tools")
@file:Order("110")
@file:URL("kotlinLanguageAndTools/gradle")

package docs.`02_Kotlin_lang_and_tools`

import org.openrndr.dokgen.annotations.*

fun main() {
    @Text
    """
    # What is Gradle?

    Gradle is a build tool used by OPENRNDR components like the core, its extensions, the template and the guide.
    
    ## What is a build tool? 
    
    One of the core tasks performed by a build tool is telling the compiler to compile programs. 
    Compiling involves reading your source code files (human readable text files describing algorithms) 
    and converting each line into instructions that can be executed by the machine. 
    A compiler program usually takes several source code files and outputs one executable program.
    Compilers often have complex command-line arguments, and build tools take care of such details so we don't
    need to.
    
    Other important steps performed by build tools include downloading dependencies 
    (third party libraries used by our source code) and packing those dependencies  
    into our executable program.
    
    Gradle is a complex tool which supports multiple programming languages. It has many plugins providing 
    all kinds of functionality needed by people building and distributing applications.
    
    Gradle is configured via Tasks written in text files. Tasks are similar to recipes that describing the steps 
    required to get something done. 
        
    ## Do we always need to use a build tool?
    
    In theory one could write Kotlin or Java programs without using any build tool, but that would involve downloading
    all the dependencies manually and writing long instructions in the command line to build your programs.
    Using Gradle makes our life easier.
    
    ## How much do I need to know about Gradle when using OPENRNDR?
    
    In most cases one can happily write OPENRNDR programs without knowing that Gradle exists.
     
    One case in which we need to edit the `build.gradle.kts` file is to enable or disable ORX extensions.
    For example, if a program requires access to a MIDI hardware controller, that extension needs to be enabled inside
    `build.gradle.kts`, followed by clicking on "Reload All Gradle Projects" in the IDE.
        
    ## What else does Gradle do for OPENRNDR?
       
    - It converts the OPENRNDR source code into multiple distributable libraries.
    - It helps publish those libraries to an [online database](https://mvnrepository.com/artifact/org.openrndr) 
      making them accessible for everyone.
    - Similarly, it builds all the ORX extensions (known as addons or plugins in other frameworks)
      and helps publishing them to the same database.
    - It runs your OPENRNDR programs and builds executables you can share with others.
    - It builds the OPENRNDR Guide, creating images and video files which get embedded in the documentation, then 
      publishes the guide online.
    - Gradle runs tests to make sure code changes did not break any functionality.
    - Gradle updates readme.md files, for example to list all the ORX extensions in the root readme file.
    
    Thanks to Gradle and the automation it enables, the whole OPENRNDR project can be maintained by a small team
    of developers.

    ## Are there alternatives to Gradle?
    
    Other build tools often used are Maven, Ant and cmake. 
    
    Gradle is a great fit for Kotlin-based projects because Kotlin itself can be used to write Gradle tasks, 
    avoiding the need to work with additional languages. 

    """.trimIndent()
}
