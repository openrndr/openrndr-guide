# Creating Distributable Programs

Creating distributable programs in OPENRNDR is slightly harder than using for example Processing. We can configure maven to produce packages that can be distributed.

## Shaded jar (or "überjar")

A good starting point for creating distributable versions is to create a single executable .jar file that contains your OPENRNDR based program plus all its dependencies.

Such a program can then be invoked through the command:
```sh
java -jar my-openrndr-program.jar
```

## Using Gradle


## Using Maven

```

```