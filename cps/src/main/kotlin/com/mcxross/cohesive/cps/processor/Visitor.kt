package com.mcxross.cohesive.cps.processor

import com.google.devtools.ksp.symbol.*
import java.io.OutputStream

class Visitor(private val file: OutputStream, private val ep: ExtensionProcessor) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if (classDeclaration.classKind != ClassKind.CLASS) {
            ep.error("Only class can be annotated with @Extension", classDeclaration)
            return
        }

    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
    }

    override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {
    }
}