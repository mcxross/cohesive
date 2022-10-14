package com.mcxross.cohesive.common

import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.DefaultPluginManager

object Cohesive {

    init {
        //Must initialize logging before anything else
        Log.init()

        Log.i { "Cohesive starting" }

        val pluginManager = DefaultPluginManager()
    }

    fun bootstrap() {
        Log.i {
            "Starting up Cohesive"
        }
    }

    //private fun extensions() = pluginManager.getExtensions(IStore::class.java)

}
