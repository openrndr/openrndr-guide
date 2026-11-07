## Contributing to the guide in 5 steps

It's very easy!

1. Clone this repository: `git@github.com:openrndr/openrndr-guide.git`.
2. Open it in IntelliJ IDEA.
3. Edit or add Kotlin files under `src/main/kotlin/docs/`. The available
   [annotations are described here](https://github.com/openrndr/openrndr-guide/tree/main/dokgen).
4. Push the changes to your fork of `openrndr-guide`.
5. Create a pull request.

----

If you are making quick contributions to existing pages in the guide, you can ignore the rest of this document.

## Concept

The OPENRNDR guide is somewhat unique. The source code of the guide is made out of Kotlin programs arranged in various folders. Each Kotlin file contains blocks of text describing different aspects of the framework but also contains blocks of code that serve two purposes: they are code examples for the learner, but they are actually executed by `dokgen` to produce screenshots and video files during the build process. The produced files are then embedded in the guide.

A program called `dokgen` (found in this repo) parses the Kotlin files. It creates Markdown files for [Zola](https://www.getzola.org/), the static site generator that builds the website, it extracts the source code of small programs embedded in the guide's source code and runs those programs to generate media files.

Generating media files as part of the build process helps notice if the guide needs to be updated in some areas or if changes unexpectedly broke part of the framework.

The templates, styles and configuration of the website are found in `data/zola-site`. Its design is based on the [Just the Docs](https://just-the-docs.com/) theme.

## Folders, file names and annotations

The structure of the guide comes from the folders and file names under `src/main/kotlin/docs`:

- Name folders with this pattern: `\d+_[a-zA-Z_-]+`. That's digits, an underscore
  and one or more words using upper or lower case ASCII characters, underscores and dashes.
  The digits define the order of the folders. Folders can be nested to create sub-sections.
- In the root and in each folder there should be an `index.kt` file. It is the
  page of that section.
- Add sibling documents named with this pattern: `C\d+_[a-zA-Z_-]+.kt`. That's a `C`
  followed by digits, an underscore, one or more words using upper or lower case ASCII
  characters, underscores and dashes, ending with `.kt`. The digits define the order
  of the documents.
- Pages and sub-folders of a folder are sorted together by their number.
- It is suggested to not use sequential numbers to allow inserting new files or
  folders between existing ones. Use the same amount of digits for all the files
  in a folder (`C090`, `C100`, not `C90`, `C100`) so the IDE and GitHub list
  them in the same order as the guide.

The URL of a page is generated from the folder and file names by removing the
numbers and converting to camel case, respecting sequences of upper case characters
which are assumed to be acronyms (like `OSC` or `ORX`). For example
`30_Drawing/C305_Render_targets.kt` is published at `/drawing/renderTargets/`.

The only annotation each file needs is the title, shown in the navigation,
in the browser tab and by search engines:

```kotlin
@file:Title("Render targets")
```

Optional annotations:

- `@file:Aliases("drawing/oldName", "advancedDrawing/renderTargets.html")`: former
  URLs of the page, relative to the root of the site. Visiting them redirects to the page.
  Add one when renaming or moving a file so existing links keep working.
- `@file:URL("drawing/renderTargets")`: overrides the URL generated from the file name.

### Links between pages

Link to other pages of the guide using the path of their Markdown file, starting with `@/`:

```markdown
See [render targets](@/drawing/renderTargets.md#creating-a-render-target)
and the [ORX section](@/ORX/_index.md).
```

`index.kt` files become `_index.md`. These links are checked when the website is built:
a link to a page or a heading that doesn't exist makes the build fail.

## If you use search / replace...

When editing the guide with Idea, make sure your *search results* are located under `src/`, NOT under `build/` because it contains
automatically generated content, and it gets overwritten by the build process. It is easy
to end up editing the wrong files when using search.

## Build the guide

Run the `Tasks/dokgen/dokgen` Gradle task to update the local website.

The first time can take a while since it will run over 100 Kotlin programs
to produce the guide's screenshots and videos. If one of the programs
gets stuck running for over 10 seconds, it can be closed and the build
will continue with the next program.

Once the build process is complete, the website sources will be found at
`build/dokgen/zola/`: the generated Markdown in `content/`, the media files
in `static/media/` and a copy of `data/zola-site`.

## Preview the guide locally

1. [Install Zola](https://www.getzola.org/documentation/getting-started/installation/),
   version 0.23 or newer. It is a single executable without dependencies.
2. Start the web server
    - Run `zola serve` in the `build/dokgen/zola/` folder
    - or
    - Run the `openrndr-guide/Tasks/dokgen/zolaServe` Gradle task. If `zola` is
      not in your PATH, pass its location: `./gradlew zolaServe -Pzola=/path/to/zola`.
3. Open [http://127.0.0.1:1111](http://127.0.0.1:1111) in a browser.

To update the website automatically while editing, run `./gradlew dokgen --continuous`
in another terminal. `zola serve` reloads the browser when the files change.

To check that all the links between pages are valid, run `zola check --skip-external-links`
in `build/dokgen/zola/`.

## Fast builds

By default, building the guide generates many videos and images that can take
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

Two GitHub Actions that take care of publishing the guide and the examples:
https://github.com/openrndr/openrndr-guide/tree/main/.github/workflows
They can be triggered manually or automatically when a commit is tagged
using this pattern: `v[0-9].[0-9]+.[0-9]+-[0-9]+`, for instance `v0.4.4-0`.
The first three digits should match the openrndr/orx version, and the last
digit is incremented each time the guide is updated.

The guide is built with [getzola/github-pages](https://github.com/getzola/github-pages)
and deployed to GitHub Pages by GitHub Actions (Settings > Pages > Source: GitHub Actions).
After it is published, other workflows check the external links and build the PDF version.

## Troubleshooting

- On some Linux distributions, the version of `ffmpeg` available via their respective package 
  managers has not been compiled with the necessary encoders to generate videos for the 
  guide - `libx264` in particular. One option to resolve this is to uninstall the package 
  manager version and install a [Linux static build](https://ffmpeg.org/download.html#build-linux).
