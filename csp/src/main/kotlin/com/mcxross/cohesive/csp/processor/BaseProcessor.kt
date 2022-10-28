package com.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import java.io.OutputStream
import kotlin.reflect.KClass

open class BaseProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

  var extensions = ArrayList<String>()

  fun Resolver.findAnnotations(kClass: KClass<*>) =
    getSymbolsWithAnnotation(kClass.qualifiedName.toString())
      .filterIsInstance<KSClassDeclaration>()

  operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    return emptyList()
  }

  fun cache(string: String) {
    extensions.add(string)
  }

  fun getCodeGenerator(): CodeGenerator {
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
