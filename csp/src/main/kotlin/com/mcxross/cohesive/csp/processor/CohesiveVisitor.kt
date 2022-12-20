package com.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.mcxross.cohesive.csp.annotation.ExtensionPoint

/**
 * Visitor for processing classes annotated with `@Cohesive`.
 *
 * @param ep The processor to use when processing the class.
 */
class CohesiveVisitor(private val ep: BaseProcessor) : KSVisitorVoid() {

  /**
   * Visit a class declaration and process it if it is annotated with `@Cohesive`.
   *
   * @param classDeclaration The class declaration to visit.
   * @param data The data being passed through the visitor.
   */
  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    // If the class is not a class, log an error and return
    if (classDeclaration.classKind != ClassKind.CLASS) {
      ep.e("Only class can be annotated with @Cohesive", classDeclaration)
      return
    }

    // If the class is abstract, log an error and return
    if (classDeclaration.isAbstract()) {
      ep.e("Abstract class can not be annotated with @Cohesive", classDeclaration)
      return
    }

    // Check if the class implements ExtensionPoint
    val implementsExtensionPoint =
      classDeclaration
        .getAllSuperTypes()
        .filter { it.declaration.qualifiedName?.asString() == ExtensionPoint::class.qualifiedName }
        .toList()
        .isNotEmpty()

    // If the class does not implement ExtensionPoint, log an error and return
    if (!implementsExtensionPoint) {
      ep.e("Class must implement ExtensionPoint", classDeclaration)
      return
    }

    // Log that a valid class has been found
    ep.i(
      "Found class ${classDeclaration.qualifiedName?.asString() ?: classDeclaration.simpleName.asString()}"
    )

    // Cache the class
    ep.cache(classDeclaration.qualifiedName?.asString() ?: classDeclaration.simpleName.asString())
  }

  /**
   * Visit a property declaration.
   *
   * @param property The property declaration to visit.
   * @param data The data being passed through the visitor.
   */
  override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {}

  /**
   * Visit a type argument.
   *
   * @param typeArgument The type argument to visit.
   * @param data The data being passed through the visitor.
   */
  override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {}
}
