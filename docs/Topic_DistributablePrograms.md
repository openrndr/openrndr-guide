# Distributable Programs

Creating distributable programs is slightly harder than using e.g. Processing. We can configure maven to produce packages that can be distributed.

There are two routes to do this

## Appassembler

Appassembler 
```xml
<plugin>
</plugin>
```

## App bundler 

If you plan to distribute for OSX only, it is likely more appealing to use app bundler. App bundler produces .app packages that feel more native to the OSX environment.

