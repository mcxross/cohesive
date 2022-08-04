package com.mcxross.cohesive.common.editor.platform

import com.mcxross.cohesive.common.common.platform.File

lateinit var _HomeFolder: java.io.File
actual val HomeFolder: File get() = _HomeFolder.toProjectFile()