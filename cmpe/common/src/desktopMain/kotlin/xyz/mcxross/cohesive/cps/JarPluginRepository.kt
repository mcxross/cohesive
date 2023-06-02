package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.cps.utils.JarFileFilter
import okio.Path

class JarPluginRepository(pluginsRoots: List<Path>) :
  BasePluginRepository(pluginsRoots, JarFileFilter()) {
  constructor(vararg pluginsRoots: Path) : this(listOf<Path>(*pluginsRoots))
}
