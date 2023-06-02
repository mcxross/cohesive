package xyz.mcxross.cohesive.mellow

lateinit var _HomeFolder: java.io.File

//TODO: fix property
actual val HomeFolder: File get() = _HomeFolder.parentFile as File
