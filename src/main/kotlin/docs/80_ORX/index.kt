@file:Suppress("UNUSED_EXPRESSION")
@file:Title("ORX")
@file:Order("1100")
@file:URL("ORX/index")

package docs.`10_ORX`

import org.openrndr.dokgen.annotations.*

fun main() {

    @Text
    """
    # ORX (OPENRNDR Extras)

    The ORX project is a library of tools that can be used in OPENRNDR based programs. ORX contains implementations of
    datastructures, algorithms and utilities for (mostly) computational graphics. You can find the ORX source code and
    additional documentation in the [ORX repository](https://github.com/openrndr/orx)

    ## Using ORX

    Using the OPENRNDR extras is a matter of adding an additional Maven repository and selected dependencies to your
    Gradle project.
    
    The [openrndr-template](https://github.com/openrndr/openrndr-template) project makes this simple as 
    the repositories are already set up and one only has to 
     
    1. Open the `build.gradle.kts` file
    2. Uncomment the desired module names under `orxFeatures`
    3. Save
    4. Reimport Gradle projects: if using Idea choose 🔄 _Reimport All Gradle Projects_ from the Gradle panel.
    """
}