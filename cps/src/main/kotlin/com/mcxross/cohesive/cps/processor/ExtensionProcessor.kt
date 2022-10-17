package com.mcxross.cohesive.cps.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.validate
import com.mcxross.cohesive.cps.Extension
import java.io.OutputStream
import kotlin.reflect.KClass

class ExtensionProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private var listOfExtensions = ArrayList<String>()
    private fun Resolver.findAnnotations(kClass: KClass<*>) = getSymbolsWithAnnotation(kClass.qualifiedName.toString())
        .filterIsInstance<KSClassDeclaration>()

    operator fun OutputStream.plusAssign(str: String) {
        this.write(str.toByteArray())
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols = resolver
            .findAnnotations(Extension::class)

        if (!symbols.iterator().hasNext()) return emptyList()

        i("Init ${ExtensionProcessor::class.simpleName}")

        val file: OutputStream = getCodeGenerator().createNewFile(
            dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
            packageName = "com.mcxross.cohesive.r",
            fileName = "Extensions"
        )
        file += "//Generated by Cohesive Plugin System Processor\n"
        file += "//Don't edit\n"
        file += "package com.mcxross.cohesive.r\n\n"
        file += "import com.mcxross.cohesive.cps.ExtensionIndex\n\n"
        file += "class DefaultExtensionIndex : ExtensionIndex {\n"
        file += "//List of indexed Extensions\n"
        symbols.forEach { it.accept(Visitor(file = file, ep = this), Unit) }
        file += "override var extensions = listOf(\n"
        listOfExtensions.forEach { file += "\"$it\",\n" }
        file += ")\n\n"
        file += "}\n"
        i("Done ${ExtensionProcessor::class.simpleName}")
        file.close()

        return symbols.filterNot { it.validate() }.toList()
    }
    fun cache(string: String) {
        listOfExtensions.add(string)
    }

    private fun getCodeGenerator(): CodeGenerator {
        return environment.codeGenerator
    }

    private fun getLogger(): KSPLogger {
        return environment.logger
    }

    fun i(message: String, symbol: KSNode? = null) {
        getLogger().info(message = message, symbol = symbol)
    }

    fun e(message: String, symbol: KSNode? = null) {
        getLogger().error(message = message, symbol = symbol)
    }
}