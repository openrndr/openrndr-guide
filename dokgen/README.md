# DokGen

DokGen is a gradle plugin that takes kotlin source files such as [this](https://github.com/openrndr/openrndr-guide/blob/dev/src/main/kotlin/docs/04_Drawing_basics/C00_DrawingPrimitives.kt)  and turns it into documentation like [this](https://guide.openrndr.org/#/04_Drawing_basics/C00_DrawingPrimitives).
The kotlin sources are turned into markdown which are then used to generate a static site using [docsify](https://docsify.js.org/#/).
Using DokGen, you can take advantage of IDE features like auto-completion and refactoring while writing documentation. Moreover, your docs will always be up to date with your API, because invalid docs will just not compile.

Read [this medium article](https://medium.com/openrndr/improving-the-openrndr-guide-f98fba29c393) on how we've improved the [OPENRNDR](https://openrndr.org/) [guide](https://guide.openrndr.org/#/) using DokGen.


## Getting started

Add DokGen to your build.gradle through jitpack.

```
// add jitpack to repositories (both to the buildscript and top-level repositories)
maven { url 'https://jitpack.io' }


// add DokGen to the buildscript dependencies
classpath 'com.github.openrndr:dokgen:bdc8d110d4'


// add DokGen to the top-level dependencies
implementation 'com.github.openrndr:dokgen:bdc8d110d4'
```
Have a look at [this gradle file](https://github.com/krksgbr/dokgen-example/blob/master/build.gradle).
An example repository showing basic usage and setup can be found [here](https://github.com/krksgbr/dokgen-example).
A more complete setup can be seen in the [OPENRNDR guide](https://github.com/openrndr/openrndr-guide/tree/dev) repo.


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
Use this annotation to produce a video file
(a window recording).
The default duration is 10 seconds, 
30 frames per second and no multiSampling, 
although arguments can be specified to alter
those values.


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
The optional integer argument specifies 
a multiSampling value.


### Media.Image

```kotlin
@Media.Image "media/myimage.png"
```
Include an image in the documentation.


### Media.Video

```kotlin
@Media.Video "media/myvideo.mp4"
```
Include an video in the documentation.


## File names and titles

Titles from which the navigation sidebar is generated are derived from the file names.
To determine the order of files, include a number sequence in the beginning of the filename, for example `C00MyFile.kt, C01MyFile.mt`.
Words in the file names can be either snake or camel cased: both "My_File" and "MyFile" will become "My File" in the sidebar.
Additionally, file names can be defined in an `index.properties` file placed in the same directory as the files it belongs to.
The contents of the `index.properties` file is a simple mapping between the file name (without extension) and the desired title as it should appear in the sidebar.
For example the following would work when placed next to files `C00MyFile.kt` and `C01MyFile2.kt`:
```
C00MyFile = Title
C01MyFile2 = Title2
```

## Gradle tasks

DokGen provides the following gradle tasks under the group `dokgen`:

#### dokgen

Runs the build process.

#### serveDocs

Starts a development server and makes the docs available at `http://localhost:3000`.
You need docker installed for this to work.


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
[Example](https://github.com/krksgbr/dokgen-example/tree/master/docsify-assets)

## Jekyll assets

Include any of the following files in your jekyll assets:
- `index.html`: override the default one
- `index.js`:  override the default one
- `CNAME`: for setting up a [custom subdomain](https://help.github.com/articles/setting-up-a-custom-subdomain/) on GitHub.

[Example](https://github.com/krksgbr/dokgen-example/tree/master/docsify-assets)

## Publishing

DokGen doesn't come with a built-in publishing solution.
[gradle-git-publish](https://github.com/ajoberstar/gradle-git-publish) is an easy to use gradle plugin for this purpose.
