package com.mcxross.cohesive.common.ui.view.editor.platform

import com.mcxross.cohesive.common.ui.view.editor.platform.File

lateinit var _HomeFolder: java.io.File
actual val HomeFolder: File get() = _HomeFolder.toProjectFile()