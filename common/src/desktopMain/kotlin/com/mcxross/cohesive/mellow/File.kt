@file:Suppress("NewApi")

package com.mcxross.cohesive.mellow

import com.mcxross.cohesive.common.common.platform.toProjectFile

actual val HomeFolder: File
    get() = java.io.File(System.getProperty("user.home")).toProjectFile()