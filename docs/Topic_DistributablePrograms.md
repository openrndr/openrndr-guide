# Creating Distributable Programs

Creating distributable programs in OPENRNDR is slightly harder than using for example Processing. However, the ability to create
easy-to-use distributable progams is one of OPENRNDR's top priorities.

## Shaded jar (or "überjar")

A good starting point for creating distributable versions is to create a single executable .jar file that contains your OPENRNDR based program plus all its dependencies.

Such a program can then be invoked through the command:
```sh
java -jar my-openrndr-program.jar
```

or on OSX:
```sh
java -XstartsOnFirstThread -jar my-openrndr-program.jar
```

Creating such a jar in a Gradle based project is quite simple.

```groovy
jar {

}
```

Running the jar task will now produce an executable jar.

## Stand-alone executables

A next step up from the überjar is the stand-alone executable. Stand alone executables are platform specific and can only be generated on the target platform. The stand-alone executable includes a full copy of the Java runtime and can be ran without installing Java.

Stand-alone executables are produced with a JDK 9.0 tool called `javapackager`.

First we have to change our überjar task a bit such that `javapackager` can work with the generated jar.

```groovy
jar {

}
```

Then add (copy+paste) the following task to your gradle build.

```groovy
task standAloneExecutable {

}
```

Running the standAloneExecutable task will produce a distribution containing an .exe on Windows or an .app bundle on MacOS.

## Small stand-alone executables

In the previous section we have created a stand-alone executable that can be distributed and ran without any further dependencies. This is great, but since the full Java runtime is included the generated distribution is quite large. As it turns out, the included Java runtime contains many parts that are not used in most OPENRNDR applications.

Since Java 9.0 the Java standard library is modularized which makes it possible to create partial runtime environments tailored to our application's needs. A JDK 9.0 tool called `jlink` provides the functionality to create these customized runtimes.

However, `jlink` only works on Java 9 projects that are modularized. Modularized projects require dependencies that are modularized. At this point in time, most Java libraries are not modularized so it looks like we are out of luck.

Not so! we can work around this limitation by creating a dummy modularized program, use `jlink` to create the customized runtime and manually replace the full runtime with the generated partial runtime.

This process should be performed by gradle at some point.