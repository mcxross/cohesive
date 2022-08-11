@file:Suppress("NewApi")

package com.mcxross.cohesive.common.common.platform

actual val HomeFolder: File get() = java.io.File(System.getProperty("user.home")).toProjectFile()