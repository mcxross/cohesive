@file:Suppress("NewApi")

package com.mcxross.cohesive.common.common.platform

import com.mcxross.cohesive.common.editor.platform.toProjectFile

actual val HomeFolder: File get() = java.io.File(System.getProperty("user.home")).toProjectFile()