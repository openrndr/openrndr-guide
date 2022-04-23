## Clone the guide

```
$ git clone git@github.com:openrndr/openrndr-guide.git
$ cd openrndr-guide
```

## Open the openrndr-guide project in IntelliJ Idea

## Run the `dokgen` Gradle task

The first time it will take a while since it will run over 100 kotlin programs
to produce the guide's screenshots and videos. If a program gets stuck running for over
10 seconds it can be closed and the build will continue with the next program.

Once the build is complete the guide will be found at 
`build/dokgen/jekyll/docs/` in markdown format.

## Preview the guide in a web browser

1. Install [Docker](https://www.docker.com/get-started/).
2. Run the `dokgen/webServerStart` gradle task (it can take some time 
   to download jekyll during the first run).
3. Open [http://0.0.0.0:4000](http://0.0.0.0:4000) in a browser.

## Improve the guide

1. Make changes to the markdown files under `src/main/kotlin/docs/`.
   Read about the [available annotations here](https://github.com/openrndr/openrndr-guide/tree/main/dokgen).
2. Run the `dokgen` task (either in the command line `./gradlew dokgen` or in
   the Gradle pane on the right edge of the IDE).
3. Reload the page in the browser to observe your changes.
4. Repeat.

Gotcha: running the `dokgen` task will produce a `build` folder. When using
search and/or replace **make sure the results are inside the `src` folder**.
It is easy to mistakenly spend time editing files inside the `build` folder.
Such changes will be overwritten when running `dokgen` again.

After improving the guide you can push the changes to your fork of 
`openrndr-guide` and send a pull request.

To make it easier to review please try to keep pull requests reasonably small.

Thank you for improving the guide! :-)
