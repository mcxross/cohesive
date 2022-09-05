package com.mcxross.cohesive.common.ui.mellow.component.model

import com.mcxross.cohesive.mellow.File

lateinit var _HomeFolder: java.io.File
actual val HomeFolder: File get() = _HomeFolder.toProjectFile()