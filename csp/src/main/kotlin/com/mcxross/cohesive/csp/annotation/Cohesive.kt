package com.mcxross.cohesive.csp.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Cohesive(

    /**
     * The name of the Secondary Plugin.
     *
     * It is used to identify the plugin in the UI and logs.
     * This is required.
     */
    val platform: String,

    /**
     * The version of the Secondary Plugin.
     *
     * It is used to identify the plugin in the UI and logs.
     * This is required.
     */
    val version: String,

    /**
     * A list of Net annotations of the various Platform networks.
     *
     * These will appear in the UI as a list of networks to choose from.
     * This is required.
     * */
    val nets: Array<Net>,
)
