@file:Suppress("NewApi")

package com.mcxross.cohesive.common.ui.view.editor.platform

import com.mcxross.cohesive.common.common.platform.toProjectFile
import com.mcxross.cohesive.common.ui.view.editor.platform.File

actual val HomeFolder: File get() = java.io.File(System.getProperty("user.home")).toProjectFile()