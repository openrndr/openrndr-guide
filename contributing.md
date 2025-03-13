## Contributing to the guide in 5 steps

It's very easy!

1. Clone this repository: `git@github.com:openrndr/openrndr-guide.git`.
2. Open it in IntelliJ Idea.
3. Edit or add kotlin files under `src/main/kotlin/docs/`. The available
   [annotations are described here](https://github.com/openrndr/openrndr-guide/tree/main/dokgen).
4. Push the changes to your fork of `openrndr-guide`.
5. Create a pull request.

----

If you are making quick contributions to existing pages in the guide you can ignore the rest of this document.

## Concept

The OPENRNDR guide is somewhat unique. The source code of the guide is made out of Kotlin programs arranged in various folders. Each Kotlin file contains blocks of text describing different aspects of the framework, but also containsblocks of code that serve two purposes: they are code examples for the learner, but they are actually executed by `dokgen` to produce screenshots and video files during the build process. The produced files are then embedded in the guide.

A program called `dokgen` (found in this repo) parses the Kotlin files. It creates markdown files for [Jekyll](https://jekyllrb.com/) (used by GitHub pages), it extracts the source code of small programs embedded in the guide's source code and runs those programs to generate media files.

Generating media files as part of the build process helps notice if the guide needs to be updated in some areas or if changes unexpectedly broke part of the framework.

For Jekyll, we use the [Just-the-docs](https://just-the-docs.github.io/just-the-docs/) Jekyll theme.

## Annotations

The [front-matter annotations](https://github.com/openrndr/openrndr-guide/tree/main/dokgen#front-matter) 
in each Kotlin file are now managed automatically by a Gradle Task called
`Update just-the-docs annotations`. The only annotation that needs to
be set manually is the Title. If you run the task, annotations will be
added when missing, and updated based on the file / folder names.

These annotations are passed to Jekyll to build the guide.

Please follow these conventions:

- Name folders with this pattern: `\d+_[a-zA-Z_-]+`. That's digits, an underscore
  and one or more words using upper or lower case ASCII characters, underscores and dashes.
  The digits define the order of the folders. It is suggested to not use sequential numbers
  to allow inserting new folders between existing ones.
- In the root and in each folder there should be an `index.kt` file.
- Add sibling documents named with this pattern: `C\d+_[a-zA-Z_-]+.kt`. That's a `C`
  followed by digits, an underscore, one or more words using upper or lower case ASCII
  characters, underscores and dashes, ending with `.kt`. The digits define the order
  of the documents. It is suggested to not use sequential numbers
  to allow inserting new folders between existing ones.

The `Order` annotation is generated from the number in file and folder names.
The `ParentTitle` in sibling documents is made to match the `Title` in the `index.kt`
file in the same folder.
The `URL` annotation is generated from the folder and file names, by removing the
numbers and converting to camel case, respecting sequences of upper case characters
which are assumed to be acronyms (like `OSC` or `ORX`).

Suggestions:

- Always commit git changes before running the task to be able to undo what it does.
- If creating new folders and files
    - Run the Gradle Task to generate the annotations for the new files.
    - Update manually the automatically generated `Title` annotations.
- If changing the `Title` of an Index file
    - Run the Gradle Task to update the siblings' `ParentTitle`.
- If renaming files or folders,
    - Run the Gradle Task to update the annotations.
    - Delete the build folder before running `dokgen` to avoid duplicate documents.


## If you use search / replace...

When editing the guide with Idea, make sure your *search results* are located under `src/`, NOT under `build/` because it contains
automatically generated content, and it gets overwritten by the build process. It is easy
to end up editing the wrong files when using search.

## Build the guide

Run the `Tasks/dokgen/dokgen` Gradle task to update the local Jekyll website.

The first time can take a while since it will run over 100 kotlin programs
to produce the guide's screenshots and videos. If one of the programs
gets stuck running for over 10 seconds it can be closed and the build
will continue with the next program.

Once the build process is complete the guide will be found at
`build/dokgen/jekyll/docs/` in markdown format and viewable in your
web browser if the Docker/Jekyll container is running (next step).

## Preview the guide locally

### With Docker

To preview the guide we need Jekyll in our system. One way to install Jekyll is by using a Docker container.

1. Install [Docker](https://www.docker.com/get-started/).
2. Start the web server
    - Run the `openrndr-guide/Tasks/dokgen/webServerStart` Gradle task
    - or
    - Run `sudo build/dokgen/jekyll/docs/webServerStart.sh` script.
3. Open [http://0.0.0.0:4000](http://0.0.0.0:4000) in a browser.

## Stop the web server before closing Idea

If you try closing Idea without stopping the Jekyll web server the IDE will hang
waiting for Jekyll to finish. To stop Jekyll:

- Run the `openrndr-guide/Tasks/dokgen/webServerStop` Gradle task
- or
- Run `sudo build/dokgen/jekyll/docs/webServerStop.sh` script.

### Directly with Ruby (without Docker)

If you have Ruby configured in your system, you can follow these steps to regenerate
the website when changes are saved and automatically reload your local guide in a web browser:

1. In one terminal run `./gradlew dokgen --continuous`
2. In another terminal
  - `cd build/dokgen/jekyll/docs/`
  - `bundle install`
  - `bundle exec jekyll server -d /tmp/guide --livereload`
3. Open [http://0.0.0.0:4000](http://0.0.0.0:4000) in a browser.

The `-d /tmp/guide` argument makes sure the generated html is not created under the build folder, which would trigger dokgen in a loop. Adjust this path making it valid for your OS.

## Fast builds

By default, building the guide generates many videos and images which can take
a while. Media generation can be disabled by setting
`skipMediaGeneration=true` as an `Environment variable` in the `dokgen/dokgen`
Gradle task. This can be useful to focus on editing and organizing
texts. Generating the media files before submitting changes can still
be a good practice to make sure the programs run successfully.

Thank you for improving the guide! :-)

### Notes for maintainers

If you are here to change the code that produces the guide, note that there are
two nested projects: `DokGen` and the guide itself.

You can read about DokGen in its [readme](dokgen/README.md).
If you make changes to it, build it by running the `dokgen/Tasks/build/build`
Gradle task first, then build the guide by running
`openrndr-guide/Tasks/dokgen/dokgen` to see the effects of your changes to DokGen

#### To publish the guide changes:

Two GitHub actions that take care of publishing the guide and the examples:
https://github.com/openrndr/openrndr-guide/tree/main/.github/workflows
They can be triggered manually or automatically when a commit is tagged
using this pattern: `v[0-9].[0-9]+.[0-9]+-[0-9]+`, for instance `v0.4.4-0`.
The first three digits should match the openrndr/orx version, and the last
digit is incremented each time the guide is updated.

## Troubleshooting

- On some Linux distributions, the version of `ffmpeg` available via their respective package 
  managers have not been compiled with the necessary encoders to generate videos for the 
  guide - `libx264` in particular. One option to resolve this is to uninstall the package 
  manager version and install a [Linux static build](https://ffmpeg.org/download.html#build-linux).
