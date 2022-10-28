package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.AndFileFilter
import com.mcxross.cohesive.cps.utils.DirectoryFileFilter
import com.mcxross.cohesive.cps.utils.HiddenFilter
import com.mcxross.cohesive.cps.utils.NameFileFilter
import com.mcxross.cohesive.cps.utils.NotFileFilter
import com.mcxross.cohesive.cps.utils.OrFileFilter
import okio.Path
import java.io.FileFilter

class DevelopmentPluginRepository(pluginsRoots: List<Path>) : BasePluginRepository(pluginsRoots) {

  override var filter: FileFilter? = filter()

  constructor(vararg pluginsRoots: Path) : this(mutableListOf<Path>(*pluginsRoots)) {}

  fun filter(): AndFileFilter {
    val pluginsFilter = AndFileFilter(DirectoryFileFilter())
    pluginsFilter.fileFilters.add(NotFileFilter(createHiddenPluginFilter()))
    return pluginsFilter
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
