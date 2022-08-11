package com.mcxross.cohesive.desktop.utils

import com.mcxross.cohesive.desktop.AsyncPluginManager
import com.mcxross.cohesive.desktop.DefaultAsyncPluginManager
import org.pf4j.DefaultExtensionFinder
import org.pf4j.ExtensionFinder
import org.pf4j.PluginManager
import java.util.concurrent.CompletableFuture


fun loadPlugins(onLoaded: () -> Unit, onStarted: (plugin: PluginManager) -> Unit) {
    // create the plugin manager
    // create the plugin manager
    val pluginManager: AsyncPluginManager = object : DefaultAsyncPluginManager() {
        //        final PluginManager pluginManager = new DefaultPluginManager() {
        override fun createExtensionFinder(): ExtensionFinder {
            val extensionFinder = super.createExtensionFinder() as DefaultExtensionFinder
            extensionFinder.addServiceProviderExtensionFinder() // to activate "HowdyGreeting" extension
            return extensionFinder
        }
    }

    // load the plugins
//        pluginManager.loadPlugins();
    // load the plugins
//        pluginManager.loadPlugins();
    val feature: CompletableFuture<Void> = pluginManager.loadPluginsAsync()
    feature.thenRun {
        println("Plugins loaded")
        onLoaded()
    }

    // start (active/resolved) the plugins
//        pluginManager.startPlugins();
    // start (active/resolved) the plugins
//        pluginManager.startPlugins();
    feature.thenCompose { v: Void? -> pluginManager.startPluginsAsync() }
    feature.thenRun {
        println("Plugins started")
        onStarted(pluginManager)
    }

    feature.get()

}