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

0. Install [Docker](https://www.docker.com/get-started/).
1. Run the `dokgen/webServerStart` gradle task (it can take some time 
   to download jekyll during the first run).
2. Open [http://0.0.0.0:4000](http://0.0.0.0:4000) in a browser.

## Improve the guide

0. Make changes to the markdown files under `src/main/kotlin/docs/`.
1. Run the `dokgen` task (either in the command line `./gradlew dokgen` or in
   the Gradle pane on the right edge of the IDE).
2. Reload the page in the browser to observe your changes.
3. Go to point 3.

Gotcha: if using the search/replace features in your IDE, make sure you are 
editing files inside the `src/` folder. If by accident you edit any files inside 
the `build/` folder those changes will be overwritten the next time the dokgen task 
is run. It's easy to make this mistake.

When done improving the guide you can push the changes to your fork of 
`openrndr-guide` and send a pull request.

To make it easier to review try to keep pull requests reasonably small.

Thank you for improving the guide! :-)


