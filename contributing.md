## Clone and install DokGen

[DokGen](https://github.com/openrndr/dokgen) 
is a gradle plugin that takes kotlin source files and turns them into documentation.

```
$ git clone git@github.com:openrndr/dokgen.git
$ cd dokgen
$ ./gradlew publishToMavenLocal -Prelease.version=2.0-SNAPSHOT
```

## Clone the guide

```
$ git clone git@github.com:openrndr/openrndr-guide.git
$ cd openrndr-guide
```

## Open the openrndr-guide project in IntelliJ Idea

## Run the `jekyll` Gradle task

The first time it will take a while since it will run over 100 kotlin programs
to produce the guide's screenshots and videos. If a program gets stuck running for over
10 seconds it can be closed and the build will continue with the next program.

Once the build is complete the guide will be found at 
`build/dokgen/jekyll/docs/` in markdown format.

## Workflow

1. View the website in a web browser:
   - Run the `dokgen/webServerStart` gradle task (takes a minute to run).
   - Open [http://0.0.0.0:4000](http://0.0.0.0:4000) in a browser.
2. Make changes to the markdown files under `src/main/kotlin/docs/`
3. Run the `jekyll` task (either in the command line `./gradlew jekyll` or in
   the Gradle pane on the right edge of the IDE).
4. Reload the page in the browser to observe your changes.
5. Repeat.

When done improving one of the guide's pages you can push the changes to the
dev branch of your fork of `openrndr-guide` and send a pull request.

To make it easier to review try to keep pull requests reasonably small.

Thank you for improving the guide! :-)


