package xyz.mcxross.cohesive.cps

import xyz.mcxross.cohesive.cps.utils.AndFileFilter
import xyz.mcxross.cohesive.cps.utils.DirectoryFileFilter
import xyz.mcxross.cohesive.cps.utils.HiddenFilter
import xyz.mcxross.cohesive.cps.utils.NameFileFilter
import xyz.mcxross.cohesive.cps.utils.OrFileFilter
import okio.Path
import java.io.FileFilter

class DevelopmentPluginRepository(pluginsRoots: List<Path>) : BasePluginRepository(pluginsRoots) {

  override var filter: FileFilter? = filter()
  constructor(vararg pluginsRoots: Path) : this(mutableListOf<Path>(*pluginsRoots))

  fun filter(): AndFileFilter {
    return AndFileFilter(DirectoryFileFilter(), createHiddenPluginFilter())
  }

  protected fun createHiddenPluginFilter(): FileFilter {
    val hiddenPluginFilter = OrFileFilter(HiddenFilter())

    // skip default build output folders since these will cause errors in the logs
    hiddenPluginFilter.fileFilters.add(NameFileFilter(MAVEN_BUILD_DIR))
    hiddenPluginFilter.fileFilters.add(NameFileFilter(GRADLE_BUILD_DIR))

    return hiddenPluginFilter
  }

  companion object {
    const val MAVEN_BUILD_DIR = "target"
    const val GRADLE_BUILD_DIR = "build"
  }
}
