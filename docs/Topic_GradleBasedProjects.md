# Gradle based projects

In this guide it is assumed the reader works on projects that are build using Gradle. Gradle may seem a bit overwhelming at first so here are some instructions to get things going.

## OPENRNDR project from template

By far the easiest way is to clone the template project
```sh
git clone --depth 1 https://github.com/openrndr/openrndr-gradle-template.git
```

This contains everything needed to start, notably the `build.gradle` with the right dependencies and the Gradle wrappers.

## OPENRNDR project from scratch

If you desire to not use the template but rather start from scratch or add OPENRNDR to an existing project.

### Adding the OPENRNDR bintray repository

When using Gradle add this custom repository to your `build.gradle`
```groovy
repositories {
   maven {
        url = "https://dl.bintray.com/openrndr/openrndr/"
    }
}
```

### Adding OPENRNDR dependencies
```groovy

project.ext.openrndrVersion = "0.3.4"

switch (org.gradle.internal.os.OperatingSystem.current()) {
    case org.gradle.internal.os.OperatingSystem.WINDOWS:
        project.ext.openrndrOS = "windows"
        break
    case org.gradle.internal.os.OperatingSystem.LINUX:
        project.ext.openrndrOS = "linux"
        break
    case org.gradle.internal.os.OperatingSystem.MAC_OS:
        project.ext.openrndrOS = "macos"
        break
}

dependencies {
  runtime "org.openrndr:openrndr-gl3:$openrndrVersion"
  runtime "org.openrndr:openrndr-gl3-natives-$openrndrOS:$openrndrVersion"
  compile "org.openrndr:openrndr-core:$openrndrVersion"
}
```

You can also add all `openrndr-gl3-natives-<platform>` libraries to create universal distributions, at the cost of a somewhat larger
runtime.

## Importing Gradle projects in IntelliJ

New project from existing sources. Be sure to use the Gradle wrapper.
