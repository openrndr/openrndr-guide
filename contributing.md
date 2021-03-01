## Clone and install dokgen

[DokGen](https://github.com/openrndr/dokgen) 
is a gradle plugin that takes kotlin source files and turns them into documentation.

1. `$ git clone git@github.com:openrndr/dokgen.git`
1. `$ cd dokgen`
1. `$ ./gradlew publishToMavenLocal -Prelease.version=2.0-SNAPSHOT`

## Clone the guide

1. `$ git clone git@github.com:openrndr/openrndr-guide.git`
1. `$ cd openrndr-guide`
1. `$ git checkout dev`

## Open the openrndr-guide project in IntelliJ Idea

## Run the Gradle task called `docsify`

It can take a while. It will run many kotlin programs from the guide
to produce screenshots. If a program gets stuck running for over
10 seconds it can be closed and the build will continue with the next program.

Once the build is complete the guide will be found at 
`build/dokgen/docsify/docs/index.html`.

## Workflow

Running a [local web server](https://developer.mozilla.org/en-US/docs/Learn/Common_questions/set_up_a_local_testing_server) 
can be useful. Launching such a server for `build/dokgen/docsify/docs/`
makes the OPENRNDR guide available at [http://localhost:8080](http://localhost:8080) or [http://localhost:8000](http://localhost:8000).

Once the web server is running:

1. Make changes to the markdown files under `src/main/kotlin/docs/`
2. Run the `docsify` task (either in the command line `./gradlew docsify` or in
   the Gradle pane on the right border of Idea).
3. Reload the page in the browser to observe the changes in the html guide.
4. Repeat.

When done improving one of the guide's pages you can push the changes to the
dev branch of your fork of openrndr-guide and send a pull request.

To make it easier to review try to keep pull requests reasonably small.

Thank you for improving the guide! :-)


