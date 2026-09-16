package org.openrndr.dokgen.sourceprocessor

import KotlinLexer
import KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.TokenStreamRewriter
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTree
import org.openrndr.dokgen.examplesPackageDirective
import org.openrndr.extra.kotlinparser.verbatimText
import java.io.File


/** Fully qualified prefix the dokgen annotations may appear with. */
private const val ANNOTATIONS_PACKAGE = "org.openrndr.dokgen.annotations."

/**
 * A block of the generated documentation.
 *
 * The order of [elements] matches the order of the corresponding
 * annotated statements in the source file.
 */
data class Doc(val elements: List<Element> = listOf()) {
    sealed class Element {
        class Code(val value: String) : Element()
        class Markdown(val text: String) : Element()
        sealed class Media : Element() {
            class Image(val src: String) : Media()
            class Video(val src: String) : Media()
        }
    }

    fun add(element: Element): Doc = copy(elements = elements + element)
}


/* ----------------------------------------------------------------------- *
 * Generic ANTLR parse-tree helpers
 * ----------------------------------------------------------------------- */

/** Collects all descendants of [type]. When [stopAtMatch] is true, a matched
 *  node is collected but not descended into (yielding only the top-most matches). */
private fun <T : ParseTree> ParseTree.descendantsOfType(
    type: Class<T>,
    stopAtMatch: Boolean = false
): List<T> {
    val out = ArrayList<T>()
    fun rec(node: ParseTree) {
        for (i in 0 until node.childCount) {
            val c = node.getChild(i)
            if (type.isInstance(c)) {
                out.add(type.cast(c))
                if (!stopAtMatch) rec(c)
            } else {
                rec(c)
            }
        }
    }
    rec(this)
    return out
}

/** The first descendant of [type] in pre-order, or null. */
private fun <T : ParseTree> ParseTree.firstDescendantOfType(type: Class<T>): T? {
    for (i in 0 until childCount) {
        val c = getChild(i)
        if (type.isInstance(c)) return type.cast(c)
        c.firstDescendantOfType(type)?.let { return it }
    }
    return null
}

/** The closest enclosing [KotlinParser.StatementContext], or null. */
private fun ParseTree.enclosingStatement(): KotlinParser.StatementContext? {
    var p: ParseTree? = this.parent
    while (p != null) {
        if (p is KotlinParser.StatementContext) return p
        p = p.parent
    }
    return null
}


/* ----------------------------------------------------------------------- *
 * Annotation helpers
 * ----------------------------------------------------------------------- */

/** The (possibly dotted) annotation name, e.g. `Media.Image`, `Code.Block`,
 *  with the dokgen package prefix stripped. Null when it can't be determined. */
private fun KotlinParser.AnnotationContext.annotationName(): String? {
    val unescaped = singleAnnotation()?.unescapedAnnotation()
        ?: multiAnnotation()?.unescapedAnnotation()?.firstOrNull()
        ?: return null
    val userType = unescaped.userType() ?: unescaped.constructorInvocation()?.userType()
    return userType?.text?.removePrefix(ANNOTATIONS_PACKAGE)
}

/** Names of the leading annotations of a statement, in source order. */
private fun KotlinParser.StatementContext.annotationNames(): List<String> =
    annotation().mapNotNull { it.annotationName() }

/** Names of the leading prefix annotations of an expression, e.g. the `Text` in
 *  `@Text "..."`. Used to read the dokgen annotation off a property initializer
 *  (see [GuideBuilder.process]). */
private fun KotlinParser.ExpressionContext.prefixAnnotationNames(): List<String> =
    firstDescendantOfType(KotlinParser.PrefixUnaryExpressionContext::class.java)
        ?.unaryPrefix()?.mapNotNull { it.annotation()?.annotationName() }
        ?: emptyList()

/** The constructor invocation of an annotation like `@ProduceVideo(...)`, or null. */
private fun KotlinParser.AnnotationContext.constructorInvocation(): KotlinParser.ConstructorInvocationContext? =
    singleAnnotation()?.unescapedAnnotation()?.constructorInvocation()

/**
 * Returns the content of the first string literal found in this expression,
 * with the surrounding quotes removed. Handles both line strings (`"..."`)
 * and multiline strings (`"""..."""`), and ignores trailing calls such as
 * `.trimIndent()`.
 */
private fun KotlinParser.ExpressionContext.stringContent(): String {
    val literal = firstDescendantOfType(KotlinParser.StringLiteralContext::class.java) ?: return ""
    literal.multiLineStringLiteral()?.let { return it.verbatimText().removeSurrounding("\"\"\"") }
    literal.lineStringLiteral()?.let { return it.verbatimText().removeSurrounding("\"") }
    return ""
}


/* ----------------------------------------------------------------------- *
 * Source rendering (verbatim text + targeted surgery + dedent)
 * ----------------------------------------------------------------------- */

/**
 * Renders a region of the source as a self-contained, de-indented snippet.
 *
 * The dokgen annotations found inside the region are removed:
 * - `@Exclude` removes either just the annotation (keeping its target, used
 *   for runnable sources) or the whole annotated statement (used for the
 *   documentation and exported sources), depending on [dropExcludedBlocks].
 * - `@Text` / `@Media.*` remove the whole annotated statement.
 * - all other dokgen annotations are stripped, keeping their target.
 *
 * @param prependColumn spaces prepended to the first line before [String.trimIndent]
 *        so that the common indentation can be detected (the verbatim text of a
 *        rule starts at its first token and therefore has no leading indentation).
 */
private fun renderRegion(
    tokens: CommonTokenStream,
    fromTokenIndex: Int,
    toTokenIndex: Int,
    annotations: List<KotlinParser.AnnotationContext>,
    dropExcludedBlocks: Boolean,
    prependColumn: Int
): String {
    if (fromTokenIndex > toTokenIndex) return ""
    val rewriter = TokenStreamRewriter(tokens)
    annotations.forEach { ann ->
        when (ann.annotationName()) {
            "Exclude" ->
                if (dropExcludedBlocks) deleteAnnotatedStatement(rewriter, tokens, ann)
                else deleteAnnotation(rewriter, tokens, ann)

            "Text", "Media.Image", "Media.Video" ->
                deleteAnnotatedStatement(rewriter, tokens, ann)

            "Code", "Code.Block", "Application", "ProduceScreenshot", "ProduceVideo" ->
                deleteAnnotation(rewriter, tokens, ann)

            else -> Unit // not a dokgen annotation: leave untouched
        }
    }
    val text = rewriter.getText(Interval(fromTokenIndex, toTokenIndex))
    return (" ".repeat(prependColumn) + text).trimIndent()
}

/**
 * Index of the first token on the same line as [tokenIndex], i.e. [tokenIndex]
 * extended left over the leading indentation. This matters because the lexer
 * emits one whitespace token per character and the `@` token only captures the
 * single space directly preceding it, so the remaining indentation would
 * otherwise survive a deletion and corrupt the following line.
 */
private fun lineStartIndex(tokens: CommonTokenStream, tokenIndex: Int): Int {
    var start = tokenIndex
    while (start - 1 >= 0) {
        val prev = tokens.get(start - 1)
        if (prev.channel != Token.DEFAULT_CHANNEL && prev.text.isBlank()) start-- else break
    }
    return start
}

/** Deletes only the annotation and its line's indentation, keeping its target. */
private fun deleteAnnotation(
    rewriter: TokenStreamRewriter,
    tokens: CommonTokenStream,
    ann: KotlinParser.AnnotationContext
) {
    rewriter.delete(lineStartIndex(tokens, ann.start.tokenIndex), ann.stop.tokenIndex)
}

/** Deletes the whole statement the annotation belongs to, including its leading
 *  indentation and the following line separator, so no blank line is left. */
private fun deleteAnnotatedStatement(
    rewriter: TokenStreamRewriter,
    tokens: CommonTokenStream,
    ann: KotlinParser.AnnotationContext
) {
    val statement = ann.enclosingStatement() ?: run {
        deleteAnnotation(rewriter, tokens, ann)
        return
    }
    var endIndex = statement.stop.tokenIndex
    var i = endIndex + 1
    while (i < tokens.size()) {
        val t = tokens.get(i)
        when {
            t.channel != Token.DEFAULT_CHANNEL -> i++ // include trailing whitespace/comments
            t.type == KotlinParser.NL -> {
                endIndex = i
                break
            }

            else -> break
        }
    }
    rewriter.delete(lineStartIndex(tokens, statement.start.tokenIndex), endIndex)
}

/** Renders the expression of an annotated statement (e.g. `application { ... }`). */
private fun renderExpression(
    tokens: CommonTokenStream,
    expr: KotlinParser.ExpressionContext,
    dropExcludedBlocks: Boolean
): String = renderRegion(
    tokens = tokens,
    fromTokenIndex = expr.start.tokenIndex,
    toTokenIndex = expr.stop.tokenIndex,
    annotations = expr.descendantsOfType(KotlinParser.AnnotationContext::class.java),
    dropExcludedBlocks = dropExcludedBlocks,
    prependColumn = expr.start.charPositionInLine
)

/**
 * Renders the statements inside the `run { ... }` lambda of a `@Code.Block`,
 * unwrapping the `run {}` so only its body is shown.
 */
private fun renderCodeBlock(
    tokens: CommonTokenStream,
    expr: KotlinParser.ExpressionContext
): String {
    val callee = expr.firstDescendantOfType(KotlinParser.PrimaryExpressionContext::class.java)
        ?.simpleIdentifier()?.text
    require(callee == "run") {
        "@Code.Block annotation can only be applied to `run {}` blocks."
    }
    val lambda = expr.firstDescendantOfType(KotlinParser.LambdaLiteralContext::class.java)
        ?: error("@Code.Block `run {}` block has no lambda body.")
    // The lambda's first and last tokens are `{` and `}`; skip them to get the body.
    return renderRegion(
        tokens = tokens,
        fromTokenIndex = lambda.start.tokenIndex + 1,
        toTokenIndex = lambda.stop.tokenIndex - 1,
        annotations = lambda.descendantsOfType(KotlinParser.AnnotationContext::class.java),
        dropExcludedBlocks = true,
        prependColumn = 0
    )
}


/* ----------------------------------------------------------------------- *
 * Media-producing annotations
 * ----------------------------------------------------------------------- */

/**
 * In a source code String, finds the [annName] annotation using a Regex and
 * replaces it with calls to either
 * `System.setProperty` or `System.clearProperty` based on the number of
 * [properties] received. These properties are used by the Program class
 * pre-loader to set up screen recording or screenshot creation.
 * The configuration of these two tools is extracted from the sorted
 * annotation arguments found in the source code.
 *
 * @param annName The annotation name to search for
 * @param properties The desired property names to use in the replacement text
 */
private fun String.replaceMediaAnnotations(
    annName: String,
    properties: List<String>
) = Regex("""$annName\((.*)\)""").replace(this) {
    val args = it.groupValues[1].split(",")
    if (args.size > properties.size) {
        error("${it.value} has too many arguments")
    }
    properties.mapIndexed { i, property ->
        if (i <= args.lastIndex) {
            val trimmedArg = args[i].trim(' ', '"')
            """ System.setProperty("$property", "$trimmedArg")"""
        } else {
            """ System.clearProperty("$property")"""
        }
    }.joinToString("\n", postfix = "\n")
}

/** Ensures named arguments aren't used in media annotations, because they
 *  would let the user swap argument order and break the property mapping. */
private fun KotlinParser.AnnotationContext.checkNoNamedArguments(name: String) {
    constructorInvocation()?.valueArguments()?.valueArgument()?.forEach { arg ->
        check(arg.simpleIdentifier() == null) {
            "Named arguments not allowed in @$name. Please remove `${arg.simpleIdentifier()!!.text} =`"
        }
    }
}


/* ----------------------------------------------------------------------- *
 * Guide builder: walks the statements of main() in source order
 * ----------------------------------------------------------------------- */

private class GuideBuilder(
    private val tokens: CommonTokenStream,
    private val imports: List<String>,
    private val packageDirective: String,
    private val runnablePackageDirective: String,
    private val mkLink: ((Int) -> String)?
) {
    var doc = Doc()
        private set
    val appSources = mutableListOf<String>()
    val appSourcesForExport = mutableListOf<String>()
    private var applicationCount = 0

    fun build(statements: List<KotlinParser.StatementContext>) {
        statements.forEach { process(it, inApplication = false) }
    }

    private fun process(statement: KotlinParser.StatementContext, inApplication: Boolean) {
        // A `val` declaration such as `@Language("markdown") val a = @Text "..."` exists only
        // to attach an @Language annotation, which makes the IDE syntax-highlight the string's
        // content while the source is edited. The dokgen annotation (e.g. @Text) and its content
        // live on the property initializer, so unwrap to it: the `val a =` scaffolding and the
        // @Language annotation are dropped while the annotated content is kept.
        val initializer = statement.declaration()?.propertyDeclaration()?.expression()
        val names = statement.annotationNames() + (initializer?.prefixAnnotationNames() ?: emptyList())
        val isApplication = "Application" in names
        val expr = statement.expression() ?: initializer

        // 1. Validate media annotations.
        statement.annotation().forEach { ann ->
            when (ann.annotationName()) {
                "ProduceScreenshot", "ProduceVideo" ->
                    ann.checkNoNamedArguments(ann.annotationName()!!)
            }
        }

        // 2. Generate runnable / exportable sources for an @Application.
        if (isApplication && expr != null) {
            generateApplicationSources(statement, expr)
            applicationCount++
        }

        // 3. Produce a documentation element (at most one per statement).
        var rendered = true
        when {
            "Text" in names && expr != null ->
                doc = doc.add(Doc.Element.Markdown(expr.stringContent()))

            "Media.Image" in names && expr != null ->
                doc = doc.add(Doc.Element.Media.Image(expr.stringContent().trim()))

            "Media.Video" in names && expr != null ->
                doc = doc.add(Doc.Element.Media.Video(expr.stringContent().trim()))

            "Code.Block" in names && expr != null ->
                addCode(renderCodeBlock(tokens, expr), inApplication || isApplication)

            "Code" in names && expr != null ->
                addCode(renderExpression(tokens, expr, dropExcludedBlocks = true), inApplication || isApplication)

            else -> rendered = false
        }

        // 4. Recurse into nested statements unless this statement was shown as
        //    code (in which case its contents are already rendered verbatim).
        if (!rendered) {
            childStatements(statement).forEach {
                process(it, inApplication = inApplication || isApplication)
            }
        }
    }

    /** Adds a code block to the doc, optionally followed by a link to the full example. */
    private fun addCode(code: String, inApplication: Boolean) {
        doc = doc.add(Doc.Element.Code(code))
        if (mkLink != null && inApplication) {
            val link = mkLink.invoke(applicationCount - 1)
            doc = doc.add(
                Doc.Element.Markdown(
                    """
                        [Link to the full example]($link)
                    """.trimIndent()
                )
            )
        }
    }

    private fun generateApplicationSources(
        statement: KotlinParser.StatementContext,
        expr: KotlinParser.ExpressionContext
    ) {
        // Exported example: drop @Exclude blocks and all annotations.
        val exportBody = renderExpression(tokens, expr, dropExcludedBlocks = true)
        appSourcesForExport.add(appTemplate(packageDirective, imports, exportBody))

        // Runnable example: keep @Exclude blocks, convert media annotations
        // into System property setup so the Program preloader can act on them.
        val runBody = renderExpression(tokens, expr, dropExcludedBlocks = false)
        val mediaSetup = statement.annotation()
            .filter { it.annotationName() in listOf("ProduceScreenshot", "ProduceVideo") }
            .joinToString("\n") { it.verbatimText().trim() }
        val runnable = listOf(mediaSetup, runBody)
            .filter { it.isNotEmpty() }
            .joinToString("\n")
            .replaceMediaAnnotations(
                "@ProduceVideo",
                listOf("video_location", "video_duration", "video_frameRate", "video_multiSample")
            )
            .replaceMediaAnnotations(
                "@ProduceScreenshot",
                listOf("screenshot_location", "screenshot_multiSample")
            )
        appSources.add(appTemplate(runnablePackageDirective, imports, runnable))
    }

    /** Direct child statements of [statement] (nested in its lambda bodies),
     *  not descending into deeper statements. */
    private fun childStatements(
        statement: KotlinParser.StatementContext
    ): List<KotlinParser.StatementContext> =
        statement.descendantsOfType(KotlinParser.StatementContext::class.java, stopAtMatch = true)
}


/* ----------------------------------------------------------------------- *
 * Entry point
 * ----------------------------------------------------------------------- */

object SourceProcessor {
    // what will be produced
    data class Output(
        val doc: String,
        val appSources: List<String>,
        val appSourcesForExport: List<String>,
        val media: List<String>,
        val annotations: Map<String, String>
    )

    fun process(
        source: String,
        packageDirective: String,
        mkLink: ((Int) -> String)? = null
    ): Output {
        val tokens = CommonTokenStream(KotlinLexer(CharStreams.fromString(source)))
        val parser = KotlinParser(tokens)
        val root = parser.kotlinFile()
        tokens.fill()

        // File annotations: @file:Title("..."), @file:Order("..."), etc.
        val fileAnnotations = LinkedHashMap<String, String>()
        root.fileAnnotation().forEach { fileAnnotation ->
            fileAnnotation.unescapedAnnotation().forEach { unescaped ->
                val ctor = unescaped.constructorInvocation() ?: return@forEach
                val name = ctor.userType().text
                val value = ctor.valueArguments()?.valueArgument()
                    ?.firstOrNull()?.expression()?.stringContent()
                if (value != null) fileAnnotations[name] = value
            }
        }

        listOf("Title", "Order", "URL").forEach { required ->
            require(!fileAnnotations[required].isNullOrEmpty()) {
                """Required @file:$required("...") annotation not found"""
            }
        }

        // Imports, excluding the dokgen annotation import which doesn't exist
        // in the generated examples.
        val imports = root.importList().importHeader()
            .map { it.verbatimText().trim() }
            .filter { !it.contains("dokgen") }

        // The package directive of runnable examples is derived from @file:URL,
        // which is parsed above, so it cannot be passed in as an argument.
        val runnablePackageDirective = examplesPackageDirective(
            File(fileAnnotations["URL"]!!).parentFile.toPath()
        )

        // Walk the statements of every top-level function (i.e. main()) in
        // source order, building the documentation and example sources.
        val statements = root.topLevelObject()
            .mapNotNull { it.declaration()?.functionDeclaration() }
            .flatMap { it.functionBody()?.block()?.statements()?.statement() ?: emptyList() }

        val builder = GuideBuilder(
            tokens = tokens,
            imports = imports,
            packageDirective = packageDirective,
            runnablePackageDirective = runnablePackageDirective,
            mkLink = mkLink
        )
        builder.build(statements)

        val media = builder.doc.elements.filterIsInstance<Doc.Element.Media>().map {
            when (it) {
                is Doc.Element.Media.Image -> it.src
                is Doc.Element.Media.Video -> it.src
            }
        }

        return Output(
            doc = renderDoc(builder.doc),
            appSources = builder.appSources,
            appSourcesForExport = builder.appSourcesForExport,
            media = media,
            annotations = fileAnnotations
        )
    }
}
