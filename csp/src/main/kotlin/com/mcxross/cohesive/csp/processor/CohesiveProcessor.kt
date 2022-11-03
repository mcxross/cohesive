package com.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
import com.mcxross.cohesive.csp.annotation.Cohesive
import java.io.OutputStream

class CohesiveProcessor(private val environment: SymbolProcessorEnvironment) :
  BaseProcessor(environment) {

  override fun process(resolver: Resolver): List<KSAnnotated> {

    val symbols = resolver.findAnnotations(Cohesive::class)

    if (!symbols.iterator().hasNext()) {
      return emptyList()
    } else if (symbols.toList().size > 1) {
      e(
        "More than one class annotated with @Cohesive found. Only one class can be annotated with @Cohesive"
      )
      return emptyList()
    }

    i("Init ${CohesiveProcessor::class.simpleName}")

    val file: OutputStream =
      getCodeGenerator()
        .createNewFile(
          dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
          packageName = "com.mcxross.cohesive.r",
          fileName = "DefaultCohesiveExtension",
        )
    file += "//Generated by Cohesive Plugin System Processor\n"
    file += "//Don't edit\n"
    file += "package com.mcxross.cohesive.r\n\n"
    file += "import com.mcxross.cohesive.cps.ExtensionIndex\n\n"
    file += "class DefaultCohesiveExtension : ExtensionIndex {\n"
    file += "//List of indexed Extensions\n"
    symbols.forEach { it.accept(CohesiveVisitor(ep = this), Unit) }
    file += "override var extensions = listOf(\n"
    extensions.forEach { file += "\"$it$\",\n" }
    file += ")\n\n"
    file += "}\n"
    i("Done ${CohesiveProcessor::class.simpleName}")
    file.close()

    return symbols.filterNot { it.validate() }.toList()
  }
}
