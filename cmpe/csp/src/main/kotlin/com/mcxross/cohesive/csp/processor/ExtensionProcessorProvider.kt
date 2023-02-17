package com.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Provider for the `ExtensionProcessor`.
 *
 * This class is responsible for creating instances of the `ExtensionProcessor`.
 */
class ExtensionProcessorProvider : SymbolProcessorProvider {

  /**
   * Create a new instance of the `ExtensionProcessor` with the given environment.
   *
   * @param environment The environment in which the processor will be run.
   * @return A new instance of the `ExtensionProcessor`.
   */
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return ExtensionProcessor(environment)
  }
}
