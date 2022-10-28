package com.mcxross.cohesive.csp.annotation

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Inherited
@MustBeDocumented
annotation class Extension(
  val ordinal: Int = 0,
  /**
   * An array of extension points, that are implemented by this extension.
   * This explicit configuration overrides the automatic detection of extension points in the
   * [com.mcxross.cohesive.csp.processor.ExtensionProcessor].
   *
   *
   * In case your extension is directly derived from an extension point this attribute is NOT required.
   * But under certain [more complex scenarios](https://github.com/pf4j/pf4j/issues/264) it
   * might be useful to explicitly set the extension points for an extension.
   *
   * @return classes of extension points, that are implemented by this extension
   */
  val points: Array<KClass<out ExtensionPoint>> = [],
  /**
   * An array of plugin IDs, that have to be available in order to load this extension.
   * The [AbstractExtensionFinder] won't load this extension, if these plugins are not
   * available / started at runtime.
   *
   *
   * Notice: This feature requires the optional [ASM library](https://asm.ow2.io/)
   * to be available on the applications classpath and has to be explicitly enabled via
   * [AbstractExtensionFinder.setCheckForExtensionDependencies].
   *
   * @return plugin IDs, that have to be available in order to load this extension
   */
  val plugins: Array<String> = [],
)
