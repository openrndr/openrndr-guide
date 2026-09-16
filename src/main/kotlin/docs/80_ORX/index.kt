@file:Suppress("UNUSED_EXPRESSION")
@file:Title("ORX")
@file:Order("1080")
@file:URL("ORX/index")

package docs.`80_ORX`

import org.intellij.lang.annotations.Language
import org.openrndr.dokgen.annotations.Order
import org.openrndr.dokgen.annotations.Text
import org.openrndr.dokgen.annotations.Title
import org.openrndr.dokgen.annotations.URL

fun main() {

    @Language("markdown") val a =
    @Text
    """
    # ORX (OPENRNDR Extras)

    The ORX project is a library of tools that can be used in OPENRNDR based programs. ORX contains implementations of
    datastructures, algorithms and utilities for (mostly) computational graphics. You can find the ORX source code and
    additional documentation in the [ORX repository](https://github.com/openrndr/orx)

    ## Using ORX

    Using the OPENRNDR extras is a matter of adding a Maven repository and selected dependencies to your Gradle project.
    
    The [openrndr-template](https://github.com/openrndr/openrndr-template) project makes this simple as 
    the repositories are already set up and one only has to 
     
    1. Open the `build.gradle.kts` file.
    2. Find the `dependencies { ... }` block.
    3. Add one line per desired dependency, in this format: `implementation(orx.marching.squares)`. Notice how the 
       dashes from `orx-marching-squares` are converted to periods. Tip: once you type `orx.` the IDE autocomplete 
       should show you the available extensions. 
    4. Save `build.gradle.kts`.
    5. Reimport Gradle projects: if using IDEA choose 🔄 _Sync All Gradle Projects_ from the Gradle panel.
    
    ### Bundles
    
    The `build.gradle.kts` makes use of dependency bundles, for instance `orx.bundles.basic`. These bundles are defined
    under `orx-module-catalog` in `orx` and `openrndr`. At the time of writing, the basic orx bundle includes
    the following:
    `orx-camera`, `orx-color`, `orx-composition`, `orx-compositor`, `orx-fx`, `orx-image-fit`, `orx-panel`, 
    `orx-video-profiles`, `orx-math`, `orx-mesh-generators`, `orx-no-clear`, `orx-noise`, `orx-shade-styles`, 
    `orx-shader-phrases`, `orx-shapes`, `orx-svg`, `orx-text-on-contour`, `orx-text-writer`
    
    """
}
