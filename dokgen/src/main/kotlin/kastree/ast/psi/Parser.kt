package kastree.ast.psi

import KotlinLexer
import KotlinParser
import kastree.ast.Node
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.psi.PsiErrorElement
import org.jetbrains.kotlin.psi.KtFile
import org.openrndr.extra.kotlinparser.ImportsExtractor

class DummyCollector : MessageCollector {
    override fun clear() {
    }

    override fun hasErrors(): Boolean {
        return false
    }

    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?) {
    }
}

open class Parser(val converter: Converter = Converter) {
    // From https://youtrack.jetbrains.com/issue/KT-76504/Find-and-deprecate-actively-used-parts-of-K1-API
    //val disposable = Disposer.newDisposable()
//
//    init {
//        IdeaStandaloneExecutionSetup.doSetup()
//        println("good so far")
//    }
//
//    val applicationEnvironment = KotlinCoreApplicationEnvironment.create(
//        disposable,
//        KotlinCoreApplicationEnvironmentMode.Production,
//    ).also {
//        it.registerParserDefinition(KotlinParserDefinition())
//        // Needed this as well to parse the .kt files
//        it.registerFileType(KotlinFileType.INSTANCE, "kt")
//    }
//
//    init {
//        println("this doesn't run")
//    }

//    val project by lazy {
//        try {
//            KotlinCoreProjectEnvironment(disposable, applicationEnvironment)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            error("** KotlinCoreProjectEnvironment failed")
//        }.project
//    }

    fun parseFile(code: String, throwOnError: Boolean = true): Node.File {
        val cleanedCode = code
            .replace("\\r\\n", "\\n")
            .replace("\\r", "\\n")
            .replace("\\t", " ")

        val parser = KotlinParser(
            CommonTokenStream(
                KotlinLexer(CharStreams.fromString(cleanedCode))
            )
        )

//        val parsed = (getInstance(project).findFile(
//            LightVirtualFile("temp.kt", INSTANCE, cleanedCode)
//        ) as KtFile).also { ktFile ->
//            if (throwOnError) ktFile.collectDescendantsOfType<PsiErrorElement>().let {
//                if (it.isNotEmpty()) {
//                    throw ParseError(ktFile, it)
//                }
//            }
//        }
//        return converter.convertFile(parsed)
        return Node.File(emptyList(), null, emptyList(), emptyList())
    }

    data class ParseError(
        val file: KtFile,
        val errors: List<PsiErrorElement>
    ) : IllegalArgumentException("Failed with ${errors.size} errors, first: ${errors.first().errorDescription}")

    companion object : Parser() {
        init {
            // To hide annoying warning on Windows
            System.setProperty("idea.use.native.fs.for.win", "false")
        }
    }
}