package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.StringUtils

/**
 * An exception used to indicate that a plugin problem occurred.
 * It's a generic plugin exception class to be thrown when no more specific class is applicable.
 */
open class PluginRuntimeException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
    constructor(
        cause: Throwable,
        message: String,
        vararg args: Any?,
    ) : super(StringUtils.format(message, *args), cause)

    constructor(message: String, vararg args: Any?) : super(StringUtils.format(message, *args))
}