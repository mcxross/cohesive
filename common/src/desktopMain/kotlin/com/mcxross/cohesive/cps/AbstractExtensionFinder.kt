package com.mcxross.cohesive.cps

import com.mcxross.cohesive.common.frontend.openapi.ui.view.CohesiveView
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.asm.ExtensionInfo
import com.mcxross.cohesive.cps.utils.ClassUtils
import com.mcxross.cohesive.csp.annotation.Extension

abstract class AbstractExtensionFinder(val pluginManager: PluginManager) : ExtensionFinder,
  PluginStateListener {

  @Volatile
  protected var extensions: MutableMap<String, Set<String>> = readStorages()

  // cache extension infos by class name
  @Volatile
  protected lateinit var extensionInfos: MutableMap<String, ExtensionInfo?>
  protected var checkForExtensionDependencies: Boolean? = null

  /**
   * Attempts to read the class path for the extension index file.
   *
   * The key is always 'cohesive', and the value is the set of extension classes as strings (e.g. 'com.mcxross.cohesive.cps.CohesivePlugin')
   * It's responsible for System Plugins
   * @return a [Map] of extensions or an empty [Map] if the file is not found
   */
  abstract fun readPluginExtensionIndex(): Map<String, Set<String>>

  /**
   * Attempts to read the [Plugin]s for the extension index file.
   *
   * The key is the plugin id, and the value is the set of extension classes as strings (e.g. 'com.mcxross.cohesive.cps.CohesivePlugin')
   * @return a [Map] of extensions or an empty [Map] if the file is not found
   */
  abstract fun readSystemExtensionIndex(): MutableMap<String, Set<String>>
  override fun find(): CohesiveView? {
    extensions.values.forEach { set ->
      set.toList().forEach {
        if (it.endsWith("$")) {
          Log.d { "Found Cohesive extension: $it" }
          return javaClass.classLoader.loadClass(it.replace("$", "")).getDeclaredConstructor()
            .newInstance() as CohesiveView
        } else {
          Log.d { "No found Cohesive extension" }
        }
      }

    }
    return null
  }

  override fun <T> find(type: Class<T>): List<ExtensionWrapper<T>> {
    Log.d { "Finding extensions of extension point ${type.name}" }

    val result: MutableList<ExtensionWrapper<T>> = ArrayList()

    // plus extensions found in classpath and plugins
    extensions.keys.forEach {
      // classpath's extensions <=> pluginId = cohesive
      val pluginExtensions: List<ExtensionWrapper<T>> = find(type, it)
      result.addAll(pluginExtensions)
    }
    if (result.isEmpty()) {
      Log.d { "No extensions found for extension point ${type.name}" }
    } else {
      Log.d { "Found ${result.size} extensions for extension point ${type.name}" }
    }

    // sort by "ordinal" property
    result.sort()
    return result
  }

  override fun <T> find(type: Class<T>, pluginId: String): List<ExtensionWrapper<T>> {
    Log.d { "Finding extensions of extension point ${type.name} for plugin $pluginId" }
    val result = ArrayList<ExtensionWrapper<T>>()

    // classpath's extensions <=> pluginId = null
    val classNames = findClassNames(pluginId)
    if (classNames.isEmpty()) {
      return result
    }

    if (pluginId != "cohesive") {

      val pluginWrapper: PluginWrapper = pluginManager.getPlugin(pluginId)
      if (com.mcxross.cohesive.cps.PluginState.STARTED != pluginWrapper.pluginState) {
        return result
      }
      Log.i { "Checking Plugin extensions from plugin $pluginId" }

    } else {
      Log.d { "Checking System extensions from classpath" }
    }

    val classLoader =
      if (pluginId != "cohesive") pluginManager.getPluginClassLoader(pluginId) else javaClass.classLoader

    for (className in classNames) {
      try {
        if (isCheckForExtensionDependencies()) {
          // Load extension annotation without initializing the class itself.
          //
          // If optional dependencies are used, the class pluginLoader might not be able
          // to load the extension class because of missing optional dependencies.
          //
          // Therefore, we're extracting the extension annotation via asm, in order
          // to extract the required plugins for an extension. Only if all required
          // plugins are currently available and started, the corresponding
          // extension is loaded through the class pluginLoader.

          // Make sure, that all plugins required by this extension are available.
          val missingPluginIds: MutableList<String> = ArrayList()
          getExtensionInfo(className, classLoader).plugins.forEach {
            val requiredPlugin: PluginWrapper = pluginManager.getPlugin(it)
            if (com.mcxross.cohesive.cps.PluginState.STARTED != requiredPlugin.pluginState) {
              missingPluginIds.add(it)
            }
          }
          if (missingPluginIds.isNotEmpty()) {
            val missing = StringBuilder()
            missingPluginIds.forEach {
              if (missing.isNotEmpty()) missing.append(", ")
              missing.append(it)
            }
            Log.d { "Extension $className is ignored due to missing plugins: $missing" }
            continue
          }
        }
        Log.d { "Loading class $className using class pluginLoader $classLoader" }
        val extensionClass = classLoader.loadClass(className)
        Log.d { "Checking extension type $className" }

        if (type.isAssignableFrom(extensionClass)) {
          val extensionWrapper: ExtensionWrapper<T> = createExtensionWrapper(extensionClass)
          result.add(extensionWrapper)
          Log.d { "Added extension $className with ordinal ${extensionWrapper.ordinal}" }
        } else {
          Log.d { "$className is not an extension for extension point ${type.name}" }
          if (com.mcxross.cohesive.cps.RuntimeMode.DEVELOPMENT == pluginManager.runtimeMode) {
            checkDifferentClassLoaders(type, extensionClass)
          }
        }
      } catch (e: ClassNotFoundException) {
        Log.e { e.message.toString() }
      } catch (e: NoClassDefFoundError) {
        Log.e { e.message.toString() }
      }
    }
    if (result.isEmpty()) {
      Log.d { "No extensions found for extension point ${type.name}" }
    } else {
      Log.d { "Found ${result.size} extension(s) for extension point ${type.name}" }
    }

    // sort by "ordinal" property
    result.sort()
    return result
  }

  override fun <T> find(pluginId: String): List<ExtensionWrapper<T>> {
    Log.d { "Finding extensions from plugin $pluginId" }
    val result: MutableList<ExtensionWrapper<T>> = ArrayList()
    val classNames = findClassNames(pluginId)
    if (classNames.isEmpty()) {
      return result
    }
    val pluginWrapper: PluginWrapper = pluginManager.getPlugin(pluginId)
    if (com.mcxross.cohesive.cps.PluginState.STARTED != pluginWrapper.pluginState) {
      return result
    }
    Log.d { "Checking extensions from plugin $pluginId" }
    val classLoader = pluginManager.getPluginClassLoader(pluginId)
    classNames.forEach {
      try {
        Log.d { "Loading class $it using class pluginLoader $classLoader" }
        val extensionClass = classLoader.loadClass(it)
        val extensionWrapper: ExtensionWrapper<T> = createExtensionWrapper(extensionClass)
        result.add(extensionWrapper)
        Log.d { "Added extension $it with ordinal ${extensionWrapper.ordinal}" }
      } catch (e: ClassNotFoundException) {
        Log.e { e.message.toString() }
      } catch (e: NoClassDefFoundError) {
        Log.e { e.message.toString() }
      }
    }

    if (result.isEmpty()) {
      Log.d { "No extensions found for plugin $pluginId" }
    } else {
      Log.d { "Found ${result.size} extensions for plugin $pluginId" }
    }

    // sort by "ordinal" property
    result.sort()
    return result
  }

  override fun findClassNames(pluginId: String): Set<String> {
    return extensions[pluginId] ?: return emptySet()
  }

  override fun pluginStateChanged(event: PluginStateEvent) {
    // TODO optimize (do only for some transitions)
    // clear cache
    extensions = mutableMapOf()

    // By default, we're assuming, that no checks for extension dependencies are necessary.
    //
    // A plugin, that has an optional dependency to other plugins, might lead to un-loadable
    // Java classes (NoClassDefFoundError) at application runtime due to possibly missing
    // dependencies. Therefore, we're enabling the check for optional extensions, if the
    // started plugin contains at least one optional plugin dependency.
    if (checkForExtensionDependencies == null && com.mcxross.cohesive.cps.PluginState.STARTED == event.pluginState) {
      for (dependency in event.plugin.descriptor.dependencies!!) {
        if (dependency.isOptional) {
          Log.d { "Enable check for extension dependencies via ASM." }
          checkForExtensionDependencies = true
          break
        }
      }
    }
  }

  /**
   * Returns true, if the extension finder checks extensions for its required plugins.
   * This feature has to be enabled, in order check the availability of
   * [Extension.plugins] configured by an extension.
   *
   *
   * This feature is enabled by default, if at least one available plugin makes use of
   * optional plugin dependencies. Those optional plugins might not be available at runtime.
   * Therefore, any extension is checked by default against available plugins before its
   * instantiation.
   *
   *
   * Notice: This feature requires the optional [ASM library](https://asm.ow2.io/)
   * to be available on the applications classpath.
   *
   * @return true, if the extension finder checks extensions for its required plugins
   */
  fun isCheckForExtensionDependencies(): Boolean {
    return java.lang.Boolean.TRUE == checkForExtensionDependencies
  }

  /**
   * Plugin developers may enable / disable checks for required plugins of an extension.
   * This feature has to be enabled, in order check the availability of
   * [Extension.plugins] configured by an extension.
   *
   *
   * This feature is enabled by default, if at least one available plugin makes use of
   * optional plugin dependencies. Those optional plugins might not be available at runtime.
   * Therefore, any extension is checked by default against available plugins before its
   * instantiation.
   *
   *
   * Notice: This feature requires the optional [ASM library](https://asm.ow2.io/)
   * to be available on the applications classpath.
   *
   * @param checkForExtensionDependencies true to enable checks for optional extensions, otherwise false
   */
  fun setCheckForExtensionDependencies(checkForExtensionDependencies: Boolean) {
    this.checkForExtensionDependencies = checkForExtensionDependencies
  }

  protected fun debugExtensions(extensions: Set<String>) {

    if (extensions.isEmpty()) {
      Log.d { "No extensions found" }
    } else {
      Log.d { "Found possible ${extensions.size} extensions:" }
      extensions.forEach {
        Log.d { it }
      }

    }

  }

  private fun readStorages(): MutableMap<String, Set<String>> {
    val result: MutableMap<String, Set<String>> = LinkedHashMap()
    result.putAll(readSystemExtensionIndex())
    result.putAll(readPluginExtensionIndex())
    return result
  }


  /**
   * Returns the parameters of an [Extension] annotation without loading
   * the corresponding class into the class pluginLoader.
   *
   * @param className name of the class, that holds the requested [Extension] annotation
   * @param classLoader class pluginLoader to access the class
   * @return the contents of the [Extension] annotation or null, if the class does not
   * have an [Extension] annotation
   */
  private fun getExtensionInfo(className: String, classLoader: ClassLoader): ExtensionInfo {
    if (!extensionInfos.containsKey(className)) {
      Log.d { "Load annotation for $className using asm" }
      val info: ExtensionInfo? = ExtensionInfo.load(className, classLoader)
      if (info == null) {
        Log.w { "No extension annotation was found for $className" }
        extensionInfos[className] = null
      } else {
        extensionInfos[className] = info
      }
    }
    return extensionInfos[className]!!
  }

  private fun <T> createExtensionWrapper(extensionClass: Class<*>): ExtensionWrapper<T> {
    val extensionAnnotation: Extension? = findExtensionAnnotation(extensionClass)
    val ordinal = extensionAnnotation?.ordinal ?: 0
    val descriptor = ExtensionDescriptor(ordinal, extensionClass)
    return ExtensionWrapper(descriptor, pluginManager.extensionFactory)
  }

  private fun checkDifferentClassLoaders(type: Class<*>, extensionClass: Class<*>) {
    val typeClassLoader = type.classLoader // class pluginLoader of extension point
    val extensionClassLoader = extensionClass.classLoader
    val match: Boolean = ClassUtils.getAllInterfacesNames(extensionClass).contains(type.simpleName)
    if (match && extensionClassLoader != typeClassLoader) {
      // in this scenario the method 'isAssignableFrom' returns only FALSE
      // see http://www.coderanch.com/t/557846/java/java/FWIW-FYI-isAssignableFrom-isInstance-differing
      Log.e { "Different class loaders: $extensionClassLoader (E) and $typeClassLoader (EP)" }
    }
  }

  companion object {

    fun findExtensionAnnotation(clazz: Class<*>): Extension? {
      if (clazz.isAnnotationPresent(Extension::class.java)) {
        return clazz.getAnnotation(Extension::class.java)
      }

      // search recursively through all annotations
      clazz.annotations.forEach {
        val annotationClass: Class<out Annotation> = it.annotationClass.java
        if (!annotationClass.name.startsWith("java.lang.annotation")) {
          val extensionAnnotation: Extension? =
            findExtensionAnnotation(
              annotationClass,
            )
          if (extensionAnnotation != null) {
            return extensionAnnotation
          }
        }
      }
      return null
    }
  }
}
