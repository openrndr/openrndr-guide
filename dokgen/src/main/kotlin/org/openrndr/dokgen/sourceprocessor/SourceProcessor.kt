package org.openrndr.dokgen.sourceprocessor

import kastree.ast.MutableVisitor
import kastree.ast.Node
import kastree.ast.Writer
import kastree.ast.psi.Converter
import kastree.ast.psi.Parser
import org.openrndr.dokgen.examplesPackageDirective
import java.io.File


// the state of folding the syntax tree
private data class State(
    val doc: Doc = Doc(),
    val applications: List<String> = listOf(),
    val applicationsForExport: List<String> = listOf(),
    val imports: List<String> = listOf(),
    val inApplication: InApplication? = null
) {
    data class InApplication(val node: Node)

    fun addApplicationToRun(text: String): State {
        return copy(applications = applications + text)
    }

    fun addApplicationForExport(text: String): State {
        return copy(applicationsForExport = applicationsForExport + text)
    }

    fun addImport(import: String): State {
        return copy(imports = imports + import)
    }

    fun updateDoc(newDoc: Doc): State {
        return copy(doc = newDoc)
    }
}

// Some nodes need to be removed from the document
// but I couldn't find a way to do it.
// As a workaround I replace them with a string node
// representing garbage to remove at the end.
val garbage = Node.Expr.StringTmpl(
    elems = listOf(
        Node.Expr.StringTmpl.Elem.Regular("GARBAGE")
    ), raw = true
)

private fun String.removeGarbage(): String {
    return this.split("\n").filter {
        !it.contains("GARBAGE")
    }.joinToString("\n")
}


data class Doc(val elements: List<Element> = listOf()) {
    sealed class Element {
        class Code(val value: String) : Element()
        class Markdown(val text: String) : Element()
        sealed class Media : Element() {
            class Image(val src: String) : Media()
            class Video(val src: String) : Media()
        }
    }

    fun add(element: Element): Doc {
        return copy(elements = elements + element)
    }
}

fun <A, B, C> Pair<A?, B?>.map2(fn: (A, B) -> C): C? {
    val maybeA = this.first
    val maybeB = this.second
    return maybeA?.let { a ->
        maybeB?.let { b ->
            fn(a, b)
        }
    }
}

// some helpers
private fun Node.Modifier.AnnotationSet.Annotation.getName(): String {
    return this.names.joinToString(".").normalizeAnnotationName()
}

private fun String.normalizeAnnotationName(): String {
    return this.replace("org.openrndr.dokgen.annotations.", "")
}


/**
 * Tries to convert an Annotation Node into a String
 */
private fun stringExpr(expr: Node.Expr): String {
    when (expr) {
        is Node.Expr.StringTmpl -> {
            return expr.elems.joinToString("") {
                when (it) {
                    is Node.Expr.StringTmpl.Elem.Regular -> it.str
                    is Node.Expr.StringTmpl.Elem.ShortTmpl -> "$${it.str}"
                    else -> throw RuntimeException("unexpected string type: $it")
                }
            }
        }
        else -> throw RuntimeException("cannot convert expression $expr to string")
    }
}

/**
 * Tries to convert a Const Expr Annotation Node into a string
 */
private fun constExpr(expr: Node.Expr) = if (expr is Node.Expr.Const)
    expr.value
else
    throw RuntimeException("cannot convert expression $expr to string")

/**
 * Get only annotations matching certain types, defined by [fn].
 */
private fun Node.filterAnnotated(fn: (Node.Expr.Annotated) -> Boolean): Node {
    return MutableVisitor.postVisit(this) { node, _ ->
        when (node) {
            is Node.Expr.Annotated -> if (fn(node)) node else garbage
            else -> node
        }
    }
}

private fun Node.mapAnnotated(fn: (Node.Expr.Annotated) -> Node): Node {
    return MutableVisitor.postVisit(this) { node, _ ->
        when (node) {
            is Node.Expr.Annotated -> fn(node)
            else -> node
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <N : Node> dropExcluded(n: N): N {
    return n.filterAnnotated { child ->
        val annotated = child.anns.firstOrNull()?.anns?.firstOrNull()?.names?.firstOrNull()
        annotated != "Exclude"
    } as N
}

private class ProcessAnnotatedNode(
    val printNode: (Node) -> String,
    val maybeMkLink: ((Int) -> String)?
) {

    private fun Node.Expr.Annotated.withoutAnnotations(): Node.Expr.Annotated {
        return copy(anns = listOf())
    }

    private fun Node.Expr.Annotated.withMediaAnnotations(): Node.Expr.Annotated {
        val mediaAnnotations = this.anns.filter {
            val annotated = it.anns.firstOrNull()?.names?.firstOrNull()
            annotated in listOf("ProduceScreenshot", "ProduceVideo")
        }
        return copy(anns = mediaAnnotations)
    }

    operator fun invoke(node: Node.Expr.Annotated, state: State): State {
        val doc = state.doc
        val annotation = node.anns.firstOrNull()?.anns?.firstOrNull() ?: return state
        val annotationName = annotation.getName()
        return when (annotationName) {

            "Application" -> {

                // 1. Generate runnable source to produce media.
                val appSourceToRun = node.withMediaAnnotations().run {
                    filterAnnotated { node ->
                        val annotated = node.anns.firstOrNull()?.anns?.firstOrNull()?.names?.firstOrNull()
                        annotated !in listOf("Text", "Media")
                    }.mapAnnotated { node ->
                        node.withMediaAnnotations()
                    }
                }.run(printNode)
                    .replaceMediaAnnotations(
                        "@ProduceVideo", listOf(
                            "video_location",
                            "video_duration",
                            "video_frameRate",
                            "video_multiSample"
                        )
                    )
                    .replaceMediaAnnotations(
                        "@ProduceScreenshot", listOf(
                            "screenshot_location",
                            "screenshot_multiSample"
                        )
                    )

                // 2. Generate source for export.
                // Similar to 1. but with @Exclude annotations dropped.
                val appSourceForExport = node.withoutAnnotations().run {
                    filterAnnotated { node ->
                        val annotated = node.anns.firstOrNull()?.anns?.firstOrNull()?.names?.firstOrNull()
                        annotated !in listOf("Exclude", "Text", "Media")
                    }.mapAnnotated { node ->
                        node.withoutAnnotations()
                    }
                }.run(printNode)

                val newState = state.copy(
                    inApplication = State.InApplication(node)
                ).addApplicationToRun(appSourceToRun)
                    .addApplicationForExport(appSourceForExport)

                val nextAnnotations = node.anns.drop(1)
                if (nextAnnotations.isEmpty())
                    newState
                else
                    this(node.copy(anns = nextAnnotations), newState)
            }

            // Ensure named arguments are not used because they
            // allow the user to swap argument order breaking the logic.
            "ProduceVideo", "ProduceScreenshot" -> {
                annotation.args.forEach {
                    check(it.name == null) {
                        "Named arguments not allowed in @$annotationName. Please remove `${it.name} =`"
                    }
                }

                val nextAnnotations = node.anns.drop(1)
                if (nextAnnotations.isEmpty())
                    state
                else
                    this(node.copy(anns = nextAnnotations), state)
            }

            "Text" -> {
                val text = stringExpr(node.expr)
                val newDoc = doc.add(Doc.Element.Markdown(text))
                state.updateDoc(newDoc)
            }

            "Code", "Code.Block" -> {
                val mkDoc = { text: String ->
                    doc.add(Doc.Element.Code(text)).let { doc ->
                        Pair(maybeMkLink, state.inApplication).map2 { mkLink, _ ->
                            val appCount = state.applications.size
                            val link = mkLink(appCount - 1)
                            doc.add(
                                Doc.Element.Markdown("""
                                                [Link to the full example]($link)
                                            """.trimIndent())
                            )
                        } ?: doc
                    }
                }
                val text = when (annotationName) {
                    "Code" -> {
                        val cleaned = node.run {
                            dropExcluded(this)
                        }.mapAnnotated {
                            it.withoutAnnotations()
                        }
                        printNode(cleaned)
                    }
                    "Code.Block" -> {
                        val call = node.run { dropExcluded(this) }
                            .mapAnnotated {
                                it.withoutAnnotations()
                            }.run { this as Node.Expr.Annotated }.expr
                        if (call is Node.Expr.Call && (call.expr as Node.Expr.Name).name == "run") {
                            call.lambda!!.func.block!!.stmts.joinToString("\n") { stmt ->
                                printNode(stmt)
                            }
                        } else {
                            throw RuntimeException("@Code.Block annotation can only be applied to `run {}` blocks.")
                        }
                    }
                    else -> {
                        throw  IllegalStateException()
                    }
                }
                val newDoc = mkDoc(text)
                state.updateDoc(newDoc)
            }

            "Media.Image" -> {
                val link = stringExpr(node.expr)
                val newDoc = doc.add(
                    Doc.Element.Media.Image(link.trim())
                )
                state.updateDoc(newDoc)
            }

            "Media.Video" -> {
                val link = stringExpr(node.expr)
                val newDoc = doc.add(
                    Doc.Element.Media.Video(link.trim())
                )
                state.updateDoc(newDoc)
            }
            else -> state
        }
    }
}

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

private class AstFolder(
    val printNode: (Node) -> String,
    maybeMkLink: ((Int) -> String)?
) : Folder<State> {
    val processAnnotatedNode = ProcessAnnotatedNode(
        printNode,
        maybeMkLink
    )
    override val pre: (State, Node) -> State =
        { state, node ->
            when (node) {
                is Node.Expr.Annotated -> {
                    processAnnotatedNode(node, state)
                }
                // some annotated nodes are not showing up as Node.Expr.Annotated but
                // as Node.WithModifiers where the annotations are in node.mods
                is Node.WithModifiers -> {
                    if (node.anns.isNotEmpty()) {
                        val annotation = node.anns.first().anns.first()
                        when (annotation.getName()) {
                            "Code" -> {
                                // here node is not a data class so cannot just copy if with the annotations left out
                                // couldn't find a better way of obtaining the same node without annotations,
                                // so mutating it with reflection before writing it to a string
                                val field = node.javaClass.getDeclaredField("mods")
                                field.isAccessible = true
                                val modsWithoutAnnotations = node.mods.filter { it !is Node.Modifier.AnnotationSet }
                                field.set(node, modsWithoutAnnotations)
                                field.isAccessible = false
                                val codeText = printNode(node)
                                val newDoc = state.doc.add(Doc.Element.Code(codeText))
                                state.updateDoc(newDoc)
                            }
                            else -> state
                        }
                    } else {
                        state
                    }
                }
                is Node.Import -> {
                    if (node.names.contains("dokgen")) {
                        state
                    } else {
                        state.addImport(printNode(node))
                    }
                }
                else -> {
                    state
                }
            }
        }
    override val post: ((State, Node) -> State) = { state, node ->
        // if we're inside a node annotated with @Application
        // and the the node is the same as what we've saved in state, then we've exited the node
        if (state.inApplication != null && node == state.inApplication.node) {
            state.copy(inApplication = null)
        } else {
            state
        }
    }
}


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

        val initialState = State()

        val extrasMap = Converter.WithExtras()
        val ast = Parser(extrasMap).parseFile(source)

        // Parse @file annotations
        val fileAnns = mutableMapOf<String, String>()
        ast.anns.filter { it.target == Node.Modifier.AnnotationSet.Target.FILE }
            .forEach {
                it.anns.forEach { ann ->
                    val expr = ann.args.first().expr
                    fileAnns[ann.names.first()] = stringExpr(expr)
                }
            }

        // Make sure required @file annotations are found
        listOf("Title", "Order", "URL").forEach {
            val str = fileAnns[it]
            require(str != null && str.isNotEmpty()) {
                """Required @file:$it("...") annotation not found"""
            }
        }

        val printNode = { node: Node ->
            Writer.write(node, extrasMap)
        }

        val folder = AstFolder(printNode, mkLink)

        val result = ast.run {
            fold(initialState, folder)
        }

        // `packageDirective` is received as an argument but
        // `runnablePackageDirective` is calculated here
        // because the @file:URL("") annotation is needed
        // which is only parsed by `ast.run {}` above.
        val runnablePackageDirective = examplesPackageDirective(
            File(fileAnns["URL"]!!).parentFile.toPath()
        )
        val renderedDoc = renderDoc(result.doc).removeGarbage()
        val appSourcesProducingMedia = result.applications.map {
            appTemplate(
                runnablePackageDirective,
                result.imports,
                it
            ).removeGarbage()
        }

        val appSourcesForExport = result.applicationsForExport.map {
            appTemplate(
                packageDirective,
                result.imports,
                it
            ).removeGarbage()
        }

        val mediaLinks = result.doc.elements.filterIsInstance<Doc.Element.Media>().map { it }
                .map {
                    when (it) {
                        is Doc.Element.Media.Image -> it.src
                        is Doc.Element.Media.Video -> it.src
                    }
                }

        return Output(
            doc = renderedDoc,
            appSources = appSourcesProducingMedia,
            appSourcesForExport = appSourcesForExport,
            media = mediaLinks,
            annotations = fileAnns
        )
    }
}
