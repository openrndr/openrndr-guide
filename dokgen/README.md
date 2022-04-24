# DokGen

DokGen is a gradle plugin that takes kotlin source files such as
[this](https://github.com/openrndr/openrndr-guide/blob/main/src/main/kotlin/docs/04_Drawing_basics/C00_DrawingPrimitives.kt)
and turns it into documentation like
[this](https://guide.openrndr.org/drawingBasics/drawingPrimitives.html).
The kotlin sources are turned into markdown which are then used to generate 
a static website using [jekyll](https://jekyllrb.com/).
Using DokGen, you can take advantage of IDE features like auto-completion and 
refactoring while writing documentation. Moreover, your docs will always be 
up-to-date with your API, because invalid docs will just not compile.

Read [this medium article](https://medium.com/openrndr/improving-the-openrndr-guide-f98fba29c393) 
on how we've improved the
[OPENRNDR](https://openrndr.org/) [guide](https://guide.openrndr.org/#/) using DokGen.

In the past DokGen was a [separate project](https://github.com/openrndr/dokgen),
but it was merged into the `openrndr-guide` repository to ease 
development and contributions to the guide.

## Annotations

### Text

```kotlin
@Text
"""
# Some Arbitrary Markdown
"""
```
Strings annotated with `@Text` may contain arbitrary markdown. The annotated strings are copied unmodified to the target file.

### Application

```kotlin
@Application
application {
  println("Hello World")
}
```
Code annotated with `Application` is wrapped in a `main` function and exported to its own file `src/generated/examples`.
DokGen will run these application examples during the build process to make sure they work.

This will create a file roughly:
```kotlin
fun main() = application {
    println("Hello World")
}
```


### Code

```kotlin
@Code
fun foo(){
  println("Hello World")
}
```
Use this annotation to show code snippets in the documentation.


### Code.Block

```kotlin
@Code.Block
run {
  println("Hello World")
}
```
Use this annotation to include the contents of a run block in the documentation.
This will produce the following:
```kotlin
println("Hello World")
```


### Exclude

```kotlin
run {
  println("hello")

  @Exclude
  println("world")
}
```

Use this annotation to exclude parts of the code from the documentation.
This will produce the following:

```kotlin
run {
   println("hello")
}
```

### ProduceVideo

```kotlin
@ProduceVideo("media/myVideo.mp4")
application {
    program {
        extend {
            drawer.circle(seconds, 0.0, 200.0)
        }
    }
}
```
Use this annotation to produce a video file (a window recording).
The full syntax looks like this:
`@ProduceVideo(path: String, duration: Double = 10.0, frameRate: Int = 30, multiSampling: Int = 0)`.


### ProduceScreenshot

```kotlin
@ProduceScreenshot("media/myImage.png", 4)
application {
    program {
        extend {
            drawer.circle(seconds, 0.0, 200.0)
        }
    }
}
```
Use this annotation to produce a screenshot. 
The optional integer argument specifies a multiSampling value.


### Media.Image

```kotlin
@Media.Image "media/myimage.png"
```
Display an image in the documentation.


### Media.Video

```kotlin
@Media.Video "media/myvideo.mp4"
```
Display a video in the documentation.


## Folder structure and file names

The Kotlin documents processed by DokGen should be organized into 
non-nested folders representing sections.
Folder names start with a number to be able to control the sorting in file
system managers, otherwise folders would be sorted alphabetically. 
Similarly, file names start with a number as well, 
but are prefixed with the letter `C` because Kotlin program 
names must not start with a digit. 

Each folder contains:
- an `index.kt` program specifying the title, order and url for a section.
- one or more `.kt` files, one for each document in that section.

## Front-matter

[Just-the-Docs](https://just-the-docs.github.io/just-the-docs/),
the Jekyll theme we are using for the OPENRNDR guide, expects
[front-matter](https://jekyllrb.com/docs/front-matter/) at the beginning of each markdown file to configure various properties. 
DokGen uses file-wide annotations at the beginning of each
Kotlin file to generate such front-matter.

### Example `index.kt` front-matter annotations

```
@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Getting started with OPENRNDR")
@file:Order("1020")
@file:URL("gettingStartedWithOPENRNDR/index")
```

The first annotation is used to ignore unused expression warnings in the IDE.
Those warnings would otherwise be common in documentation programs because
they contain isolated code that is seen as "unused".

The `Title` will be visible in the left navigation pane. It is also displayed by web browsers and search engines.

The `Order` is used by Jekyll to sort the pages. We recommend using 
non-consecutive integers, so it's possible to insert new documents between 
existing ones.

The `URL` controls the file path and the URL that will be visible
in the browser and by search engines. The reason URL generation is not
automated is so that we can control when to use upper/lower case, 
dashes and underscores for readability.

### Example `CXX_document.kt` front-matter annotation

```
@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Set up your first program")
@file:ParentTitle("Getting started with OPENRNDR")
@file:Order("100")
@file:URL("gettingStartedWithOPENRNDR/setUpYourFirstProgram")
```

Document programs have one additional annotation called
`ParentTitle`. The value should exactly match the title found in
the `index.kt` file in the same folder. It is used to organize
documents in sections.

## Gradle tasks

DokGen provides the following gradle tasks under the group `dokgen`:

#### dokgen

Runs the build process.

#### webServerStart

Starts a development server and makes the docs available at `http://0.0.0.0:4000`. You need docker installed for this to work.

#### webServerStop

Stops the web server.


## Configuration example

```
dokgen {
    runner {
        if (System.properties['os.name'] == "Mac OS X") {
            // pass jvm arguments to the process that will run the exported examples
            jvmArgs = ["-XstartOnFirstThread"]
        }
    }

    examples {
        // set the web url of where the examples will be accessible
        webRootUrl = "https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin"
    }

    jekyll {
        // media files to copy
        media = [file("$projectDir/media"), file("$projectDir/static-media")]

        // assets to copy
        assets = [file("$projectDir/data/jekyll-assets")]
    }
}
```
[Example](https://github.com/openrndr/openrndr-guide/tree/main/data/jekyll-assets)

## Jekyll assets

A `CNAME` file can be included for setting up a [custom subdomain](https://help.github.com/articles/setting-up-a-custom-subdomain/) on GitHub.

[Example](https://github.com/openrndr/openrndr-guide/tree/main/data/jekyll-assets)

## Publishing

DokGen doesn't come with a built-in publishing solution.
[gradle-git-publish](https://github.com/ajoberstar/gradle-git-publish) 
is an easy to use gradle plugin for this purpose.
