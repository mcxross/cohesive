package com.mcxross.cohesive.csp.annotation

/**
 * Annotation for marking a class as a Cohesive extension.
 *
 * It must be used only once per Secondary Plugin Module.
 *
 * @param platform The platform for which this extension is intended.
 * @param version The version of the platform for which this extension is intended.
 * @param nets The networks for which this extension is intended.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Cohesive(

  /**
   * The name of the Secondary Plugin.
   *
   * It is used to identify the plugin in the UI and logs. This is required.
   */
  val platform: String,

  /**
   * The version of the Secondary Plugin.
   *
   * It is used to identify the plugin in the UI and logs. This is required.
   */
  val version: String,

  /**
   * A list of Net annotations of the various Platform networks.
   *
   * These will appear in the UI as a list of networks to choose from. This is required.
   */
  val nets: Array<Net>,
)
