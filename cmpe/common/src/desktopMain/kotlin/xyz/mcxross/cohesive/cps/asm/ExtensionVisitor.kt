package xyz.mcxross.cohesive.cps.asm

import xyz.mcxross.cohesive.common.utils.Log
import xyz.mcxross.cohesive.csp.annotation.Extension
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * This visitor extracts an [ExtensionInfo] from any class,
 * that holds an [Extension] annotation.
 *
 *
 * The annotation parameters are extracted from byte code by using the
 * [ASM library](https://asm.ow2.io/). This makes it possible to
 * access the [Extension] parameters without loading the class into
 * the class pluginLoader. This avoids possible [NoClassDefFoundError]'s
 * for extensions, that can't be loaded due to missing dependencies.
 */
internal class ExtensionVisitor(val extensionInfo: ExtensionInfo) : ClassVisitor(ASM_VERSION) {
  override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
    return if (Type.getType(descriptor).className != Extension::class.java.name) {
      super.visitAnnotation(descriptor, visible)
    } else object : AnnotationVisitor(ASM_VERSION) {
      override fun visitArray(name: String): AnnotationVisitor {
        return if ("ordinal" == name || "plugins" == name || "points" == name) {
          object : AnnotationVisitor(ASM_VERSION, super.visitArray(name)) {
            override fun visit(key: String, value: Any) {
              Log.d { "Load annotation attribute $name = $value ($value.javaClass.name)" }
              when (name) {
                "ordinal" -> {
                  extensionInfo.ordinal = value.toString().toInt()
                }

                "plugins" -> {
                  when (value) {
                    is String -> {
                      Log.d { "Found corePlugin $value" }
                      extensionInfo.plugins.plus(value)
                    }

                    is Array<*> -> {
                      Log.d { "Found plugins ${value.contentToString()}" }

                      extensionInfo.plugins.plus(listOf(*value))
                    }

                    else -> {
                      Log.d { "Found corePlugin $value" }
                      extensionInfo.plugins.plus(value.toString())
                    }
                  }
                }

                else -> {
                  val pointClassName = (value as Type).className
                  Log.d { "Found point $pointClassName" }
                  extensionInfo.points.plus(pointClassName)
                }
              }
              super.visit(key, value)
            }
          }
        } else super.visitArray(name)
      }
    }
  }

  companion object {
    private const val ASM_VERSION = Opcodes.ASM7
  }
}
