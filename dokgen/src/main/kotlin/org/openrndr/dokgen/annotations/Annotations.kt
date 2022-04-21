package org.openrndr.dokgen.annotations

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class Title(val title: String)

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class ParentTitle(val title: String)

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class Order(val order: String)

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class URL(val url: String)


@Target(
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.SOURCE)
annotation class Application


@Target(
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.SOURCE)
annotation class ProduceScreenshot(
    val path: String,
    val multiSample: Int = 0
)


@Target(
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.SOURCE)
annotation class ProduceVideo(
    val path: String,
    val duration: Double = 10.0,
    val frameRate: Int = 30,
    val multiSample: Int = 0
)


@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FILE,
    AnnotationTarget.TYPEALIAS
)
@Retention(AnnotationRetention.SOURCE)
annotation class Code {
    @Target(AnnotationTarget.EXPRESSION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Block
}


@Target(
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.SOURCE)
annotation class Text


class Media {
    @Target(
        AnnotationTarget.EXPRESSION,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FIELD,
        AnnotationTarget.LOCAL_VARIABLE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Video

    @Target(
        AnnotationTarget.EXPRESSION,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FIELD,
        AnnotationTarget.LOCAL_VARIABLE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Image
}


@Target(
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.SOURCE)
annotation class Exclude

