package com.mcxross.cohesive.csp.processor

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.mcxross.cohesive.csp.annotation.ExtensionPoint
import java.io.OutputStream

class ExtensionVisitor(private val file: OutputStream, private val ep: ExtensionProcessor) :
  KSVisitorVoid() {
  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    if (classDeclaration.classKind != ClassKind.CLASS) {
      ep.e("Only class can be annotated with @Extension", classDeclaration)
      return
    }
    if (classDeclaration.isAbstract()) {
      ep.e("Abstract class can not be annotated with @Extension", classDeclaration)
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
