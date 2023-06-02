package xyz.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Provider for the `CohesiveProcessor`.
 *
 * This class is responsible for creating instances of the `CohesiveProcessor`.
 */
class CohesiveProcessorProvider : SymbolProcessorProvider {

  /**
   * Create a new instance of the `CohesiveProcessor` with the given environment.
   *
   * @param environment The environment in which the processor will be run.
   * @return A new instance of the `CohesiveProcessor`.
   */
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return CohesiveProcessor(environment)
  }
}
