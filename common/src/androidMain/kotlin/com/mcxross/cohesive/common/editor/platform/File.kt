package com.mcxross.cohesive.common.editor.platform

lateinit var _HomeFolder: java.io.File
actual val HomeFolder: File get() = _HomeFolder.toProjectFile()