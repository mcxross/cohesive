package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.asm.ExtensionInfo
import com.mcxross.cohesive.cps.utils.ClassUtils
import com.mcxross.cohesive.cps.utils.Log

abstract class AbstractExtensionFinder(pluginManager: PluginManager) : ExtensionFinder,
    PluginStateListener {
    protected var pluginManager: PluginManager

    @Volatile
    protected var entries: Map<String?, Set<String>>? = null
        get() {
            if (field == null) {
                entries = readStorages()
            }
            return field
        }

    @Volatile
    protected lateinit var extensionInfos // cache extension infos by class name
            : MutableMap<String, ExtensionInfo?>
    protected var checkForExtensionDependencies: Boolean? = null

    init {
        this.pluginManager = pluginManager
    }

    abstract fun readPluginsStorages(): Map<String, Set<String>>?
    abstract fun readClasspathStorages(): MutableMap<String?, Set<String>>
    override fun <T> find(type: Class<T>): List<ExtensionWrapper<T>> {
        Log.d { "Finding extensions of extension point ${type.name}" }
        val entries = entries
        val result: MutableList<ExtensionWrapper<T>> = ArrayList()

        // add extensions found in classpath and plugins
        for (pluginId in entries!!.keys) {
            // classpath's extensions <=> pluginId = null
            val pluginExtensions: List<ExtensionWrapper<T>> = find(type, pluginId!!)
            result.plus(pluginExtensions)
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
        val pluginWrapper: PluginWrapper = pluginManager.getPlugin(pluginId)
        if (PluginState.STARTED != pluginWrapper.getPluginState()) {
            return result
        }
        Log.i { "Checking extensions from plugin $pluginId" }
        val classLoader = pluginManager.getPluginClassLoader(pluginId)
        for (className in classNames) {
            try {
                if (isCheckForExtensionDependencies()) {
                    // Load extension annotation without initializing the class itself.
                    //
                    // If optional dependencies are used, the class loader might not be able
                    // to load the extension class because of missing optional dependencies.
                    //
                    // Therefore we're extracting the extension annotation via asm, in order
                    // to extract the required plugins for an extension. Only if all required
                    // plugins are currently available and started, the corresponding
                    // extension is loaded through the class loader.
                    val extensionInfo: ExtensionInfo = getExtensionInfo(className!!, classLoader)

                    // Make sure, that all plugins required by this extension are available.
                    val missingPluginIds: MutableList<String> = ArrayList()
                    for (requiredPluginId in extensionInfo.plugins) {
                        val requiredPlugin: PluginWrapper = pluginManager.getPlugin(requiredPluginId)
                        if (PluginState.STARTED != requiredPlugin.getPluginState()) {
                            missingPluginIds.add(requiredPluginId)
                        }
                    }
                    if (missingPluginIds.isNotEmpty()) {
                        val missing = StringBuilder()
                        for (missingPluginId in missingPluginIds) {
                            if (missing.isNotEmpty()) missing.append(", ")
                            missing.append(missingPluginId)
                        }
                        Log.wtf { "Extension $className is ignored due to missing plugins: $missing" }
                        continue
                    }
                }
                Log.d { "Loading class $className using class loader $classLoader" }
                val extensionClass = classLoader.loadClass(className)
                Log.d { "Checking extension type $className" }

                if (type.isAssignableFrom(extensionClass)) {
                    val extensionWrapper: ExtensionWrapper<T> = createExtensionWrapper(extensionClass)
                    result.add(extensionWrapper)
                    Log.d { "Added extension $className with ordinal ${extensionWrapper.ordinal}" }
                } else {
                    Log.wtf { "$className is not an extension for extension point ${type.name}" }
                    if (RuntimeMode.DEVELOPMENT == pluginManager.runtimeMode) {
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
            Log.d { "Found ${result.size} extensions for extension point ${type.name}" }
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
        if (PluginState.STARTED != pluginWrapper.getPluginState()) {
            return result
        }
        Log.wtf { "Checking extensions from plugin $pluginId" }
        val classLoader = pluginManager.getPluginClassLoader(pluginId)
        for (className in classNames) {
            try {
                Log.d { "Loading class $className using class loader $classLoader" }
                val extensionClass = classLoader.loadClass(className)
                val extensionWrapper: ExtensionWrapper<T> = createExtensionWrapper(extensionClass)
                result.add(extensionWrapper)
                Log.d { "Added extension $className with ordinal ${extensionWrapper.ordinal}" }
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
        return entries!![pluginId] ?: return emptySet()
    }

    override fun pluginStateChanged(event: PluginStateEvent) {
        // TODO optimize (do only for some transitions)
        // clear cache
        entries = null

        // By default we're assuming, that no checks for extension dependencies are necessary.
        //
        // A plugin, that has an optional dependency to other plugins, might lead to unloadable
        // Java classes (NoClassDefFoundError) at application runtime due to possibly missing
        // dependencies. Therefore we're enabling the check for optional extensions, if the
        // started plugin contains at least one optional plugin dependency.
        if (checkForExtensionDependencies == null && PluginState.STARTED == event.pluginState) {
            for (dependency in event.plugin.getDescriptor().dependencies!!) {
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
     * Therefore any extension is checked by default against available plugins before its
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
     * Therefore any extension is checked by default against available plugins before its
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
            for (extension in extensions) {
                Log.d { extension }

            }
        }

    }

    private fun readStorages(): MutableMap<String?, Set<String>> {
        val result: MutableMap<String?, Set<String>> = LinkedHashMap()
        result.putAll(readClasspathStorages())
        result.putAll(readPluginsStorages()!!)
        return result
    }


    /**
     * Returns the parameters of an [Extension] annotation without loading
     * the corresponding class into the class loader.
     *
     * @param className name of the class, that holds the requested [Extension] annotation
     * @param classLoader class loader to access the class
     * @return the contents of the [Extension] annotation or null, if the class does not
     * have an [Extension] annotation
     */
    private fun getExtensionInfo(className: String, classLoader: ClassLoader): ExtensionInfo {
        if (!extensionInfos.containsKey(className)) {
            Log.wtf { "Load annotation for $className using asm" }
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
        return pluginManager.extensionFactory?.let { ExtensionWrapper(descriptor, it) }!!
    }

    private fun checkDifferentClassLoaders(type: Class<*>, extensionClass: Class<*>) {
        val typeClassLoader = type.classLoader // class loader of extension point
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
            for (annotation in clazz.annotations) {
                val annotationClass: Class<out Annotation> = annotation.annotationClass.java
                if (!annotationClass.name.startsWith("java.lang.annotation")) {
                    val extensionAnnotation: Extension? = findExtensionAnnotation(annotationClass)
                    if (extensionAnnotation != null) {
                        return extensionAnnotation
                    }
                }
            }
            return null
        }
    }
}