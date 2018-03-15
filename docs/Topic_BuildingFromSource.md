# Building OPENRNDR from source

This topic can be skipped by the novice reader as it explains how to build OPENRNDR from sources which is a fully optional step. Users are adviced to use the publically available builds on Maven Central.

Building OPENRNDR from source should be an easy an straight-forward process.

## Obtaining OPENRNDR sources

The OPENRNDR sources are hosted on [Github](https://github.com/openrndr/openrndr). The work in the master branch should usually work.
```sh
git clone https://github.com/openrndr/openrndr.git
```

## Building OPENRNDR

On a system that has JDK 1.8.x or 9.0.x installed one can run the following commands from a terminal:

```sh
cd <path-to-checkout>
./gradlew build
```

This should start the build process, which will take less than a minute to complete.

Note that OPENRNDR does not depend on anything that is not on Maven Central, builds should be easy and predictable.

## Installing OPENRNDR as Maven artifacts

In order to use the OPENRNDR build from your applications one has to install OPENRNDR's Maven artifacts in the local Maven repository.

```sh
./gradlew -Prelease.version=0.4.0-SNAPSHOT publishToMavenLocal
```

## Building OPENRNDR from IntelliJ

This should be as easy as importing the Gradle project into IntelliJ.

On a MacOS system that has IntelliJ's command line tools installed one can run

```sh
cd <path-to-checkout>
idea .
```

### IntelliJ+Gradle composite builds

In case it is desired to have a single IntelliJ project that contains both the OPENRNDR and a depending module one can enable configure composite builds for the depending module. The composite build is enabled in the Gradle tool panel by selecting the depending module and using right click.
