package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.Log
import java.io.File
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.util.*

/**
 * One instance of this class should be created by holder manager for every available plug-in.
 * By default, this class pluginLoader is a Parent Last ClassLoader - it loads the classes from the holder's jars
 * before delegating to the parent class pluginLoader.
 * Use [.classLoadingStrategy] to change the loading strategy.
 */
class PluginClassLoader @JvmOverloads constructor(
    pluginManager: PluginManager,
    pluginDescriptor: PluginDescriptor,
    parent: ClassLoader?,
    classLoadingStrategy: ClassLoadingStrategy = ClassLoadingStrategy.PDA,
) :
    URLClassLoader(arrayOfNulls(0), parent) {
    private val pluginManager: PluginManager
    private val pluginDescriptor: PluginDescriptor
    private val classLoadingStrategy: ClassLoadingStrategy

    @Deprecated(
        """Replaced by {@link #PluginClassLoader(PluginManager, PluginDescriptor, ClassLoader, ClassLoadingStrategy)}.
      If {@code parentFirst} is {@code true}, indicates that the parent {@link ClassLoader} should be consulted
      before trying to load the a class through this pluginLoader."""
    )
    constructor(
        pluginManager: PluginManager,
        pluginDescriptor: PluginDescriptor,
        parent: ClassLoader?,
        parentFirst: Boolean,
    ) : this(
        pluginManager,
        pluginDescriptor,
        parent,
        if (parentFirst) ClassLoadingStrategy.APD else ClassLoadingStrategy.PDA
    ) {
    }

    /**
     * classloading according to `classLoadingStrategy`
     */
    init {
        this.pluginManager = pluginManager
        this.pluginDescriptor = pluginDescriptor
        this.classLoadingStrategy = classLoadingStrategy
    }

    public override fun addURL(url: URL) {
        Log.d { "Add $url" }
        super.addURL(url)
    }

    fun addFile(file: File) {
        try {
            addURL(file.canonicalFile.toURI().toURL())
        } catch (e: IOException) {
//            throw new RuntimeException(e);
            Log.e { e.message.toString() }
        }
    }

    /**
     * By default, it uses a child first delegation model rather than the standard parent first.
     * If the requested class cannot be found in this class pluginLoader, the parent class pluginLoader will be consulted
     * via the standard [ClassLoader.loadClass] mechanism.
     * Use [.classLoadingStrategy] to change the loading strategy.
     */
    @Throws(ClassNotFoundException::class)
    override fun loadClass(className: String): Class<*> {
        synchronized(getClassLoadingLock(className)) {

            // first check whether it's a system class, delegate to the system pluginLoader
            if (className.startsWith(JAVA_PACKAGE_PREFIX)) {
                return findSystemClass(className)
            }

            // if the class is part of the holder engine use parent class pluginLoader
            if (className.startsWith(PLUGIN_PACKAGE_PREFIX) && !className.startsWith("demo") && !className.startsWith(
                    "test"
                )
            ) {
//                log.trace("Delegate the loading of PF4J class '{}' to parent", className);
                return parent.loadClass(className)
            }
            Log.v { "Received request to load class $className" }

            // second check whether it's already been loaded
            val loadedClass = findLoadedClass(className)
            if (loadedClass != null) {
                Log.v { "Found loaded class $className" }
                return loadedClass
            }
            for (classLoadingSource in classLoadingStrategy.sources) {
                var c: Class<*>? = null
                try {
                    when (classLoadingSource) {
                        ClassLoadingStrategy.Source.APPLICATION -> c = super.loadClass(className)
                        ClassLoadingStrategy.Source.PLUGIN -> c = findClass(className)
                        ClassLoadingStrategy.Source.DEPENDENCIES -> c = loadClassFromDependencies(className)
                    }
                } catch (ignored: ClassNotFoundException) {
                }
                if (c != null) {
                    Log.v {  "Found class $className in $classLoadingSource classpath" }
                    return c
                } else {
                    Log.v { "Couldn't find class $className in $classLoadingSource classpath" }
                }
            }
            throw ClassNotFoundException(className)
        }
    }

    /**
     * Load the named resource from this holder.
     * By default, this implementation checks the holder's classpath first then delegates to the parent.
     * Use [.classLoadingStrategy] to change the loading strategy.
     *
     * @param name the name of the resource.
     * @return the URL to the resource, `null` if the resource was not found.
     */
    override fun getResource(name: String): URL? {
        Log.v { "Received request to load resource $name" }
        for (classLoadingSource in classLoadingStrategy.sources) {
            val url: URL? = when (classLoadingSource) {
                ClassLoadingStrategy.Source.APPLICATION -> super.getResource(name)
                ClassLoadingStrategy.Source.PLUGIN -> findResource(name)
                ClassLoadingStrategy.Source.DEPENDENCIES -> findResourceFromDependencies(name)
            }
            if (url != null) {
                Log.v { "Found resource $name in $classLoadingSource classpath" }
                return url
            } else {
                Log.v { "Couldn't find resource $name in $classLoadingSource" }
            }
        }
        return null
    }

    @Throws(IOException::class)
    override fun getResources(name: String): Enumeration<URL> {
        val resources: MutableList<URL> = ArrayList()
        Log.v { "Received request to load resources $name" }
        for (classLoadingSource in classLoadingStrategy.sources) {
            when (classLoadingSource) {
                ClassLoadingStrategy.Source.APPLICATION -> if (parent != null) {
                    resources.addAll(Collections.list(parent.getResources(name)))
                }

                ClassLoadingStrategy.Source.PLUGIN -> resources.addAll(Collections.list(findResources(name)))
                ClassLoadingStrategy.Source.DEPENDENCIES -> resources.addAll(findResourcesFromDependencies(name))
            }
        }
        return Collections.enumeration(resources)
    }

    protected fun loadClassFromDependencies(className: String?): Class<*>? {
        Log.v { "Search in dependencies for class $className" }
        val dependencies: List<PluginDependency> = pluginDescriptor.dependencies!!
        for (dependency in dependencies) {
            val classLoader: ClassLoader = pluginManager.getPluginClassLoader(dependency.pluginId!!)

            // If the dependency is marked as optional, its class pluginLoader might not be available.
            if (classLoader == null && dependency.isOptional) {
                continue
            }
            try {
                return classLoader.loadClass(className)
            } catch (e: ClassNotFoundException) {
                // try next dependency
            }
        }
        return null
    }

    protected fun findResourceFromDependencies(name: String?): URL? {
        Log.v { "Search in dependencies for resource $name" }
        val dependencies: List<PluginDependency> = pluginDescriptor.dependencies!!
        for (dependency in dependencies) {
            val classLoader = dependency.pluginId?.let { pluginManager.getPluginClassLoader(it) } as PluginClassLoader

            // If the dependency is marked as optional, its class pluginLoader might not be available.
            if (classLoader == null && dependency.isOptional) {
                continue
            }
            val url = classLoader.findResource(name)
            if (Objects.nonNull(url)) {
                return url
            }
        }
        return null
    }

    @Throws(IOException::class)
    protected fun findResourcesFromDependencies(name: String?): Collection<URL> {
        Log.v { "Search in dependencies for resources $name" }
        val results: MutableList<URL> = ArrayList()
        val dependencies: List<PluginDependency> = pluginDescriptor.dependencies!!
        for (dependency in dependencies) {
            val classLoader = dependency.pluginId?.let { pluginManager.getPluginClassLoader(it) } as PluginClassLoader

            // If the dependency is marked as optional, its class pluginLoader might not be available.
            if (classLoader == null && dependency.isOptional) {
                continue
            }
            results.addAll(Collections.list(classLoader.findResources(name)))
        }
        return results
    }

    companion object {
        private const val JAVA_PACKAGE_PREFIX = "java."
        private const val PLUGIN_PACKAGE_PREFIX = ""
    }
}