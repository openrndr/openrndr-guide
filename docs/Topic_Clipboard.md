# Clipboard

OPENRNDR programs can access the clipboard. Currently only text snippets can be read from and written to the clipboard.

##### Setting the clipboard content

```kotlin
clipboard.contents = "this is the new clipboard content"
```

##### Getting the clipboard content

Note that `clipboard.contents` is optional and its value can be `null`. The clipboard contents are reported `null` in case the clipboard contents are non-text or the clipboard is empty.

```kotlin
clipboard.contents?.let {
    println("the clipboard contents: $it")
}
```