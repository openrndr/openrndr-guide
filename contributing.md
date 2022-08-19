## Clone the guide

```
$ git clone git@github.com:openrndr/openrndr-guide.git
$ cd openrndr-guide
```

## Open the openrndr-guide project in IntelliJ Idea

## Run the `dokgen` Gradle task

The first time it will take a while since it will run over 100 kotlin programs
to produce the guide's screenshots and videos. If a program gets stuck running
for over
10 seconds it can be closed and the build will continue with the next program.

Once the build is complete the guide will be found at
`build/dokgen/jekyll/docs/` in markdown format.

## Preview the guide in a web browser

1. Install [Docker](https://www.docker.com/get-started/).
2. Run the `openrndr-guide/Tasks/dokgen/webServerStart` gradle task
   (it can take some time to download jekyll during the first run).
3. Open [http://0.0.0.0:4000](http://0.0.0.0:4000) in a browser.

## Improve the guide

1. Make changes to the markdown files under `src/main/kotlin/docs/`.
   Read about
   the [available annotations here](https://github.com/openrndr/openrndr-guide/tree/main/dokgen)
   .
2. Run the `dokgen` task (either in the command line `./gradlew dokgen` or in
   the Gradle pane on the right edge of the IDE).
3. Reload the page in the browser to observe your changes.
4. Repeat.

## Stop the web server before closing

If you try closing Idea without running `openrndr-guide/Tasks/webServerStop`
the IDE will get stuck waiting for the web server to shut down. If that
happens, run `./data/jekyll-assets/webServerStop.sh` from the command line.

## Search and replace pitfall

If you use search and/or replace **make sure the results are inside the `src`
folder** and not inside `build/`.
It is easy to mistakenly spend time editing files inside `build/`. Those
files will be overwritten when running `dokgen` again.

## Faster build

By default building the guide generates many videos and images which can take
more than 15 minutes. Media generation can be disabled by setting
`skipMediaGeneration=true` as an `Environment variable` in the `dokgen/dokgen`
Gradle task. This can be useful to focus on editing and organizing
texts. Generating the media files before submitting changes can still 
be a good practice to make sure the programs still run successfully.

## Submit your changes

After improving the guide you can push the changes to your fork of
`openrndr-guide` and send a pull request.

Thank you for improving the guide! :-)

### Notes for maintainers

If you are here to change the code that produces the guide, note that there are
two nested projects: `DokGen` and the guide itself.

You can read about DokGen in its [readme](dokgen/README.md).
If you make changes to it, build it by running the `dokgen/Tasks/build/build`
Gradle task first, then build the guide by running 
`openrndr-guide/Tasks/dokgen/dokgen` to see the effects of your changes to DokGen

To publish the guide:

- Make sure your SSH has been entered before opening IntelliJ Idea (`ssh-add`)
- Run `openrndr-guide/Tasks/dokgen/publishDocs`
- Run `openrndr-guide/Tasks/dokgen/publishExamples`
