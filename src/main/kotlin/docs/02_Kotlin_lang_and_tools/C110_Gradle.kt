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

    Gradle is a build tool used by various OPENRNDR components: the core, the extensions, the template and the guide.
    
    ## What is a build tool? 
    
    Maybe you are familiar with the concept of "compiling a program"? The process of compiling involves having
    the compiler read your source code (human readable texts files containing instructions for the computer) 
    and converting each line into instructions that can be executed by the machine. 
    Compiling often involves taking several source code files and outputting one executable program.
    
    Compiling programs is one of the steps performed by a build tool. Other important steps include downloading
    dependencies (third party libraries used by our source code) and packing those dependencies together 
    with our executable program.
    
    Gradle is a complex tool which supports multiple programming languages. Gradle has many plugins providing 
    all kinds of functionality needed by those building and distributing applications.
    
    Gradle is configured via tasks written in text files. Tasks are similar to recipes that describing the steps 
    required to get something done. 
        
    ## Do we always need to use a build tool?
    
    In theory one could write Kotlin or Java programs without using any build tool, but that would involve downloading
    all the dependencies manually and writing very long instructions in the command line to build your programs.
    Using Gradle makes things easier.
    
    ## How much do I need to know about Gradle when using OPENRNDR?
    
    In most cases one can happily write OPENRNDR programs without knowing that Gradle exists.
     
    One case in which we need to edit the `build.gradle.kts` file is to enable or disable ORX extensions.
    For example, if a program requires access to a MIDI hardware controller, that extension need to be enabled inside
    `build.gradle.kts`, followed by clicking on "Reload All Gradle Projects" in the IDE.
        
    ## What does Gradle do for OPENRNDR?
       
    - It converts the OPENRNDR source code into multiple distributable libraries.
    - It helps publish those libraries in an [online database](https://mvnrepository.com/artifact/org.openrndr) 
      making them accessible for everyone.
    - Similarly, it builds all the ORX extensions (known as addons or plugins in other frameworks)
      and helps publishing them in the same database.
    - It runs your OPENRNDR programs and builds an executable you can share with others.
    - It builds the OPENRNDR Guide, creating images and video files which get embedded in the documentation, then 
      publishes the guide online.
    - Gradle runs tests to make sure code changes have not broken any functionality.
    - Gradle updates readme.md files, for example to list all the ORX extensions in the root readme file.
    
    Thanks to Gradle and the automation it enables, the whole OPENRNDR project can be maintained by a very small team
    of developers.

    ## Are there alternatives to Gradle?
    
    Other build tools often used are Maven, Ant and cmake. 
    
    Gradle is a great fit for Kotlin-based projects because Kotlin itself can be used to configure Gradle, 
    avoiding the need to mix additional languages into the project. 

    """.trimIndent()
}
