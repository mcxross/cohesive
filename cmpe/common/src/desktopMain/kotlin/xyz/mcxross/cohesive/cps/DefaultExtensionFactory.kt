package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.utils.Log

/** The default implementation for [ExtensionFactory]. It uses [Class.newInstance] method. */
open class DefaultExtensionFactory : ExtensionFactory {
  /** Creates an extension instance. */
  override fun <T> create(extensionClass: Class<T>): T {
    Log.d { "Creating Extension instance for $extensionClass" }
    return try {
      extensionClass.getDeclaredConstructor().newInstance()
    } catch (e: Exception) {
      throw PluginRuntimeException(e)
    }
  }
}
