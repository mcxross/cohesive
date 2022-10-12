package com.mcxross.cohesive.cps.processor

import com.mcxross.cohesive.cps.utils.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.Reader
import java.util.regex.Pattern
import javax.annotation.processing.Filer
import javax.lang.model.element.Element

/**
 * It's a storage (database) that persists [com.mcxross.cohesive.cps.Extension]s.
 * The standard operations supported by storage are [.read] and [.write].
 * The storage is populated by [ExtensionAnnotationProcessor].
 */
abstract class ExtensionStorage(processor: ExtensionProcessor) {
    protected val processor: ExtensionProcessor

    init {
        this.processor = processor
    }

    abstract fun read(): Map<String?, Set<String?>?>?
    abstract fun write(extensions: Map<String?, Set<String?>?>?)

   /* *//**
     * Helper method.
     *//*
    protected val filer: Filer
        protected get() = processor.environment.getFiler()*/

    /**
     * Helper method.
     */
    protected fun error(message: String, vararg args: Any) {
        Log.e { String.format(message, *args) }
    }

    /**
     * Helper method.
     */
    protected fun error(element: Element, message: String, vararg args: Any) {
        Log.e { String.format(message, *args) }
    }

    /**
     * Helper method.
     */
    protected fun info(message: String, vararg args: Any) {
        Log.i { String.format(message, *args) }
    }

    /**
     * Helper method.
     */
    protected fun info(element: Element, message: String, vararg args: Any) {
        Log.i { String.format(message, *args) }
    }

    companion object {
        private val COMMENT = Pattern.compile("#.*")
        private val WHITESPACE = Pattern.compile("\\s+")
        @Throws(IOException::class)
        fun read(reader: Reader?, entries: MutableSet<String?>) {
            if (reader != null) {
                BufferedReader(reader).use { bufferedReader ->
                    var line: String
                    while (bufferedReader.readLine().also { line = it } != null) {
                        line = COMMENT.matcher(line).replaceFirst("")
                        line = WHITESPACE.matcher(line).replaceAll("")
                        if (line.isNotEmpty()) {
                            entries.add(line)
                        }
                    }
                }
            }
        }
    }
}
