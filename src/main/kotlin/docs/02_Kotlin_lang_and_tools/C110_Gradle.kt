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

    Gradle is a build tool used by OPENRNDR components like the core, extensions, template, and guide.

    ## What is a build tool? 

    A core task a build tool performs is telling the compiler to compile programs. 
    Compiling involves reading your source code files (human-readable text files describing algorithms) and converting
    each line into instructions the machine can execute. 
    A compiler program usually takes several source code files and outputs one executable program.
    Compilers often have complex command-line arguments, and build tools take care of such details so we don't need to.

    Other essential steps built tools perform include downloading dependencies (third-party libraries used by our
    source code) and packing those dependencies into our executable program.

    Gradle is a complex tool that supports multiple programming languages. It has many plugins providing all kinds
    of functionality needed by people building and distributing applications.

    To configure Gradle, one uses Tasks which are text files describing the steps required to complete something. 
        
    ## Do we always need to use a build tool?

    Theoretically, one could write Kotlin or Java programs without using any build tool, but that would involve
    manually downloading all the dependencies and writing long instructions in the command line to build your programs.
    Using Gradle makes our life easier.

    ## Do I need to know Gradle to use OPENRNDR?

    In most cases, one can happily write OPENRNDR programs without knowing that Gradle exists, although we may need
    to edit Gradle configuration files in some cases.

    ### Enable or disable extensions

    Suppose a program requires access to a MIDI hardware controller. In that case, we need to edit the
    `build.gradle.kts` file to uncomment a line to enable MIDI; then, we need to click "Reload All Gradle Projects"
    in the IDE to trigger the downloading of the MIDI libraries.

    ### Add or remove dependencies

    Thousands of JAVA libraries are available to our programs. We only need to add one line to the `build.gradle.kts`
    file to add a dependency. 

    Three such dependencies (JSON, CSV, and XML) are predefined and we only need to uncomment a line if we need them. 
    See [fileIO](https://guide.openrndr.org/fileIO/) in the guide for details.

    Other dependencies can be easily added in this format:
    `implementation("org.jbox2d:jbox2d-library:2.2.1.1")`. You can find such dependencies in
    [www.mvnrepository.com](https://mvnrepository.com/). Once found, choose the
    `Gradle (Kotlin)` tab in that website, copy the `implementation(...)` code and paste it 
    into `build.gradle.kts` inside the `dependencies { ... }` block. Remember to reload Gradle!
        
    ## What else does Gradle do for OPENRNDR?
       
    - It converts the OPENRNDR source code into multiple distributable libraries.
    - It helps publish those libraries to an [online database](https://mvnrepository.com/artifact/org.openrndr),
      making them accessible to everyone.
    - Similarly, it builds all the ORX extensions (called add-ons or plugins in other frameworks) and helps publish
      them to the same database.
    - It runs your OPENRNDR programs and builds executables you can share with others.
    - It builds the OPENRNDR Guide, creates images and video files, embeds them in the documentation, and then
      publishes the guide online.
    - Gradle runs tests to ensure code changes did not break any functionality.
    - Gradle updates readme.md files, for example, to list all the ORX extensions in the root readme file.

    Thanks to Gradle and the automation it enables, the whole OPENRNDR project can be maintained by a small team
    of developers.

    ## Are there alternatives to Gradle?

    Other build tools often used are Maven, Ant, and cmake. 

    Gradle is an excellent fit for Kotlin-based projects because Kotlin itself can be used to write Gradle tasks,
    avoiding the need to work with additional languages. 

    """.trimIndent()
}
