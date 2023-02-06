## Contributing to the guide in 5 steps

It's very easy!

1. Clone this repository: `git@github.com:openrndr/openrndr-guide.git`.
2. Open it in IntelliJ Idea.
3. Edit or add markdown files under `src/main/kotlin/docs/`. The available
   [annotations are described here](https://github.com/openrndr/openrndr-guide/tree/main/dokgen).
4. Push the changes to your fork of `openrndr-guide`.
5. Create a pull request.

----

If you are making quick contributions to the guide you can ignore the rest of this document.

## Concept

The OPENRNDR guide is somewhat unique. The source code of the guide is made out of Kotlin programs arranged in various folders. Each Kotlin file contains blocks of text describing different aspects of the framework, but also containsblocks of code that serve two purposes: they are code examples for the learner, but they are actually executed by `dokgen` to produce screenshots and video files during the build process. The produced files are then embedded in the guide.

A program called `dokgen` (found in this repo) parses the Kotlin files. It creates markdown files for [Jekyll](jekyllrb.com/) (used by GitHub pages), it extracts the source code of small programs embedded in the guide's source code and runs those programs to generate media files.

Generating media files as part of the build process helps notice if the guide needs to be updated in some areas or if changes unexpectedly broke part of the framework.

For Jekyll, we use the [Just-the-docs](https://just-the-docs.github.io/just-the-docs/) Jekyll theme.

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

- Make sure your SSH password has been entered before opening IntelliJ Idea (`ssh-add`). Otherwise, the IDE will ask
  for the password two or three times when running the following actions.
- Run `openrndr-guide/Tasks/dokgen/publishDocs` to update the guide at https://guide.openrndr.org.
- Run `openrndr-guide/Tasks/dokgen/publishExamples` to update the [examples repo](https://github.com/openrndr/openrndr-examples).
