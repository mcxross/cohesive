package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.JarFileFilter
import java.nio.file.Path
import java.util.*

class JarPluginRepository(pluginsRoots: List<Path>) : BasePluginRepository(pluginsRoots, JarFileFilter()) {
    constructor(vararg pluginsRoots: Path) : this(listOf<Path>(*pluginsRoots)) {}
}