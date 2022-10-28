package com.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.mcxross.cohesive.csp.annotation.ExtensionPoint

class CohesiveVisitor(private val ep: BaseProcessor) : KSVisitorVoid() {

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    if (classDeclaration.classKind != ClassKind.CLASS) {
      ep.e("Only class can be annotated with @Cohesive", classDeclaration)
      return
    }
    if (classDeclaration.isAbstract()) {
      ep.e("Abstract class can not be annotated with @Cohesive", classDeclaration)
      return
    }
    // Heuristic search for ExtensionPoint implementation
    if (classDeclaration.getAllSuperTypes().filter {
        it.declaration.qualifiedName?.asString() == ExtensionPoint::class.qualifiedName
      }.toList().isEmpty()) {
      ep.e("Class must implement ExtensionPoint", classDeclaration)
      return
    }

    ep.i("Found class ${classDeclaration.qualifiedName?.asString() ?: classDeclaration.simpleName.asString()}")
    ep.cache(classDeclaration.qualifiedName?.asString() ?: classDeclaration.simpleName.asString())
  }

  override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
  }

  override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {
  }

}
