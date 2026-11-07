# DokGen

DokGen is a Gradle plugin that takes Kotlin source files such as
[this](https://github.com/openrndr/openrndr-guide/blob/main/src/main/kotlin/docs/30_Drawing/C100_CirclesRectanglesLines.kt)
and turns it into documentation like
[this](https://guide.openrndr.org/drawing/circlesRectanglesLines/).
The Kotlin sources are turned into Markdown, which is then used to generate 
a static website using [Zola](https://www.getzola.org/).
Using DokGen, you can take advantage of IDE features like auto-completion and 
refactoring while writing documentation. Moreover, your docs will always be 
up to date with your API, because invalid docs will just not compile.

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
Strings annotated with `@Text` may contain arbitrary Markdown. The annotated strings are copied unmodified to the target file.

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

The Kotlin documents processed by DokGen are organized into folders
representing sections. Folders can be nested.
Folder names start with a number to control the order of the sections,
which would otherwise be alphabetical. Similarly, file names start with
a number as well, but are prefixed with the letter `C` because Kotlin program
names must not start with a digit.

Each folder contains:
- an `index.kt` program: the page of the section.
- one or more `C<number>_<Words>.kt` files, one for each document in that section.
- optionally, sub-folders.

The order, the URL and the place of each page in the navigation
are derived from the folder and file names:

| Source file                          | Zola content file          | URL                       | Weight |
|--------------------------------------|----------------------------|---------------------------|--------|
| `index.kt`                           | `_index.md`                | `/`                       | 0      |
| `C10_Requirements.kt`                | `requirements.md`          | `/requirements/`          | 10     |
| `30_Drawing/index.kt`                | `drawing/_index.md`        | `/drawing/`               | 30     |
| `30_Drawing/C305_Render_targets.kt`  | `drawing/renderTargets.md` | `/drawing/renderTargets/` | 305    |

Words separated by `_` or `-` are converted to camel case. The first letter is
lower case unless the second one is upper case (acronyms like `OSC` or `ORX`).

## File annotations

```kotlin
@file:Suppress("UNUSED_EXPRESSION")
@file:Title("Render targets")
@file:Aliases("advancedDrawing/renderTargets.html")
```

The first annotation is used to ignore unused expression warnings in the IDE.
Those warnings would otherwise be common in documentation programs because
they contain isolated code that is seen as "unused".

`Title` is required. It is visible in the navigation and in the browser tab.

`Aliases` is optional. It lists former URLs of the page, relative to the site root.
Zola generates pages at those URLs which redirect to the page.
The URLs the pages had when the guide was built with Jekyll (`drawing/renderTargets.html`)
are added automatically.

`URL` is optional. It overrides the URL derived from the file name, for example
`@file:URL("drawing/renderTargets")`, or `@file:URL("drawing/index")` for a section.

DokGen converts these annotations into the
[front matter](https://www.getzola.org/documentation/content/page/#front-matter)
of the generated Markdown files.

## Gradle tasks

DokGen provides the following gradle tasks under the group `dokgen`:

#### dokgen

Runs the build process: generates Markdown and example programs, runs the
programs to produce media files, and assembles the Zola site in `build/dokgen/zola`.

#### zolaServe

Runs `zola serve` to preview the guide at `http://127.0.0.1:1111`.
Needs [Zola](https://www.getzola.org/documentation/getting-started/installation/)
in the PATH, or its location passed with `-Pzola=/path/to/zola`.

## Configuration example

```kotlin
dokgen {
    runner {
        if (System.getProperty("os.name") == "Mac OS X") {
            // pass jvm arguments to the process that will run the exported examples
            jvmArgs = mutableListOf("-XstartOnFirstThread")
        }
    }

    examples {
        // set the web url of where the examples will be accessible
        webRootUrl = "https://github.com/openrndr/openrndr-examples/blob/master/src/main/kotlin"
    }

    zola {
        // folders with media files, copied into static/media
        media = listOf(file("$projectDir/media"), file("$projectDir/static-media"))
        // Zola site: config.toml, templates and static files
        assets = listOf(file("$projectDir/data/zola-site"))
    }
}
```

## Publishing

The `.github/workflows/publishGuide.yml` workflow builds `build/dokgen/zola` with
[getzola/github-pages](https://github.com/getzola/github-pages) and deploys it to GitHub Pages.
The custom domain is configured in the repository's Pages settings.
