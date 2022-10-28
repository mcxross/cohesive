package com.mcxross.cohesive.cps

/**
 * An [ExtensionFactory] that always returns a specific instance.
 * Optional, you can specify the extension classes for which you want singletons.
 */
class SingletonExtensionFactory(pluginManager: PluginManager, vararg extensionClassNames: String) :
  DefaultExtensionFactory() {
  private val extensionClassNames: List<String>
  private val cache: MutableMap<ClassLoader, MutableMap<String, Any>>

  init {
    this.extensionClassNames = listOf(*extensionClassNames)
    cache = HashMap()
    pluginManager.addPluginStateListener(
      object : PluginStateListener {
        override fun pluginStateChanged(event: PluginStateEvent) {
          if (event.pluginState != PluginState.STARTED) {
            cache.remove(event.plugin.pluginClassLoader)
          }
        }
      },
    )
  }

  override fun <T> create(extensionClass: Class<T>): T {
    val extensionClassName = extensionClass.name
    val extensionClassLoader = extensionClass.classLoader
    if (!cache.containsKey(extensionClassLoader)) {
      cache[extensionClassLoader] = HashMap()
    }
    val classLoaderBucket = cache[extensionClassLoader]!!
    if (classLoaderBucket.containsKey(extensionClassName)) {
      return classLoaderBucket[extensionClassName] as T
    }
    val extension: T = super.create(extensionClass)
    if (extensionClassNames.isEmpty() || extensionClassNames.contains(extensionClassName)) {
      classLoaderBucket[extensionClassName] = extension!!
    }
    return extension
  }
}
