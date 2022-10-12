package com.mcxross.cohesive.cps

import com.mcxross.cohesive.cps.utils.*
import java.io.FileFilter
import java.nio.file.Path


class DevelopmentPluginRepository(pluginsRoots: List<Path>) : BasePluginRepository(pluginsRoots) {
    constructor(vararg pluginsRoots: Path) : this(mutableListOf<Path>(*pluginsRoots)) {}

    init {
        val pluginsFilter =
            AndFileFilter(DirectoryFileFilter())
        pluginsFilter.addFileFilter(NotFileFilter(createHiddenPluginFilter()))
        super.filter = pluginsFilter
    }

    protected fun createHiddenPluginFilter(): FileFilter {
        val hiddenPluginFilter: OrFileFilter = OrFileFilter(HiddenFilter())

        // skip default build output folders since these will cause errors in the logs
        hiddenPluginFilter
            .addFileFilter(NameFileFilter(MAVEN_BUILD_DIR))
            .addFileFilter(NameFileFilter(GRADLE_BUILD_DIR))
        return hiddenPluginFilter
    }

    companion object {
        const val MAVEN_BUILD_DIR = "target"
        const val GRADLE_BUILD_DIR = "build"
    }
}