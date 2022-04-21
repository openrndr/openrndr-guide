package kastree.ast.psi

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiErrorElement
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType

class DummyCollector: MessageCollector {
    override fun clear() {
    }
    override fun hasErrors(): Boolean {
        return false
    }
    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?) {
    }
}

open class Parser(val converter: Converter = Converter) {
    protected val proj by lazy {
        val disposer = Disposer.newDisposable()
        val compilerConfiguration = CompilerConfiguration()
        val messageCollector = DummyCollector()

        compilerConfiguration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
        val kce =
                try {
                    KotlinCoreEnvironment.createForProduction(
                            disposer,
                            compilerConfiguration,
                            EnvironmentConfigFiles.JVM_CONFIG_FILES
                    )
                } catch(e:Throwable) {
                    e.printStackTrace()
                    error("kce init failed")
                }
        kce.project
    }

    fun parseFile(code: String, throwOnError: Boolean = true) = converter.convertFile(parsePsiFile(code.let {
        it.replace("\\r\\n", "\\n" ).replace("\\r", "\\n").replace("\\t"," ")
    }).also { file ->
        if (throwOnError) file.collectDescendantsOfType<PsiErrorElement>().let {
            if (it.isNotEmpty()) {
                throw ParseError(file, it)
            }
        }
    })

    fun parsePsiFile(code: String) =
        PsiManager.getInstance(proj).findFile(LightVirtualFile("temp.kt", KotlinFileType.INSTANCE, code)) as KtFile

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