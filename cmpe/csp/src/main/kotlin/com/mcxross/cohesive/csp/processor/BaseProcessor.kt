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

/**
 * Base class for symbol processors.
 *
 * @param environment The environment in which the symbol processor is being run.
 */
open class BaseProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

  /** List to hold extensions. */
  var extensions = ArrayList<String>()

  /**
   * Find annotations of the given class in the given resolver.
   *
   * @param kClass The class of the annotations to find.
   * @return A list of class declarations annotated with the given class.
   */
  fun Resolver.findAnnotations(kClass: KClass<*>) =
    getSymbolsWithAnnotation(kClass.qualifiedName.toString()).filterIsInstance<KSClassDeclaration>()

  /**
   * Append the given string to the output stream.
   *
   * @param str The string to append.
   */
  operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
  }

  /**
   * Process the given resolver and return a list of annotated nodes.
   *
   * @param resolver The resolver to process.
   * @return An empty list of annotated nodes.
   */
  override fun process(resolver: Resolver): List<KSAnnotated> {
    return emptyList()
  }

  /**
   * Add the given string to the list of extensions.
   *
   * @param string The string to add to the list of extensions.
   */
  fun cache(string: String) {
    extensions.add(string)
  }

  /**
   * Get the code generator from the processor environment.
   *
   * @return The code generator from the processor environment.
   */
  fun getCodeGenerator(): CodeGenerator {
    return environment.codeGenerator
  }

  /**
   * Get the logger from the processor environment.
   *
   * @return The logger from the processor environment.
   */
  private fun getLogger(): KSPLogger {
    return environment.logger
  }

  /**
   * Log an info message with the given symbol.
   *
   * @param message The message to log.
   * @param symbol The symbol to associate with the message.
   */
  fun i(message: String, symbol: KSNode? = null) {
    getLogger().info(message = message, symbol = symbol)
  }

  /**
   * Log an error message with the given symbol.
   *
   * @param message The message to log.
   * @param symbol The symbol to associate with the message.
   */
  fun e(message: String, symbol: KSNode? = null) {
    getLogger().error(message = message, symbol = symbol)
  }
}
