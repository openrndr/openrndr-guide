package kastree.ast.psi

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.IdeaStandaloneExecutionSetup
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreApplicationEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreApplicationEnvironmentMode
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreProjectEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiErrorElement
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.parsing.KotlinParserDefinition
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType

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
    val disposable = Disposer.newDisposable()

    init {
        IdeaStandaloneExecutionSetup.doSetup()
        println("good so far")
    }

    val applicationEnvironment = KotlinCoreApplicationEnvironment.create(
        disposable,
        KotlinCoreApplicationEnvironmentMode.Production,
    ).also {
        it.registerParserDefinition(KotlinParserDefinition())
        // Needed this as well to parse the .kt files
        it.registerFileType(KotlinFileType.INSTANCE, "kt")
    }

    init {
        println("this doesn't run")
    }

    val project by lazy {
        try {
            KotlinCoreProjectEnvironment(disposable, applicationEnvironment)
        } catch (e: Exception) {
            e.printStackTrace()
            error("** KotlinCoreProjectEnvironment failed")
        }.project
    }

    fun parseFile(code: String, throwOnError: Boolean = true) = converter.convertFile(parsePsiFile(code.let {
        it.replace("\\r\\n", "\\n")
            .replace("\\r", "\\n")
            .replace("\\t", " ")
    }).also { file ->
        if (throwOnError) file.collectDescendantsOfType<PsiErrorElement>().let {
            if (it.isNotEmpty()) {
                throw ParseError(file, it)
            }
        }
    })

    fun parsePsiFile(code: String) =
        PsiManager.getInstance(project).findFile(LightVirtualFile("temp.kt", KotlinFileType.INSTANCE, code)) as KtFile

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