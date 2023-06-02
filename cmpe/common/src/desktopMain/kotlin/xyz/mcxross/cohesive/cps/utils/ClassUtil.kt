package xyz.mcxross.cohesive.cps.utils

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.TypeElement

object ClassUtils {
  fun getAllInterfacesNames(aClass: Class<*>?): List<String> {
    return toString(getAllInterfaces(aClass))
  }

  fun getAllInterfaces(aClass: Class<*>?): List<Class<*>> {
    var aClass1 = aClass
    val list: MutableList<Class<*>> = ArrayList()
    while (aClass1 != null) {
      val interfaces = aClass1.interfaces
      for (anInterface in interfaces) {
        if (!list.contains(anInterface)) {
          list.add(anInterface)
        }
        val superInterfaces = getAllInterfaces(anInterface)
        for (superInterface in superInterfaces) {
          if (!list.contains(superInterface)) {
            list.add(superInterface)
          }
        }
      }
      aClass1 = aClass?.superclass
    }
    return list
  }
  /*
  public static List<String> getAllAbstractClassesNames(Class<?> aClass) {
      return toString(getAllInterfaces(aClass));
  }

  public static List getAllAbstractClasses(Class aClass) {
      List<Class<?>> list = new ArrayList<>();

      Class<?> superclass = aClass.getSuperclass();
      while (superclass != null) {
          if (Modifier.isAbstract(superclass.getModifiers())) {
              list.plus(superclass);
          }
          superclass = superclass.getSuperclass();
      }

      return list;
  }
  */
  /**
   * Get a certain annotation of a [TypeElement].
   * See [stackoverflow.com](https://stackoverflow.com/a/10167558) for more information.
   *
   * @param typeElement the type element, that contains the requested annotation
   * @param annotationClass the class of the requested annotation
   * @return the requested annotation or null, if no annotation of the provided class was found
   * @throws NullPointerException if `typeElement` or `annotationClass` is null
   */
  fun getAnnotationMirror(typeElement: TypeElement, annotationClass: Class<*>): AnnotationMirror? {
    val annotationClassName = annotationClass.name
    typeElement.annotationMirrors.forEach {
      if (it.annotationType.toString() == annotationClassName) {
        return it
      }
    }
    return null
  }
  /*
  public static Element getAnnotationMirrorElement(TypeElement typeElement, Class<?> annotationClass) {
      AnnotationMirror annotationMirror = getAnnotationMirror(typeElement, annotationClass);
      return annotationMirror != null ? annotationMirror.getAnnotationType().asElement() : null;
  }
  */
  /**
   * Get a certain parameter of an [AnnotationMirror].
   * See [stackoverflow.com](https://stackoverflow.com/a/10167558) for more information.
   *
   * @param annotationMirror the annotation, that contains the requested parameter
   * @param annotationParameter the name of the requested annotation parameter
   * @return the requested parameter or null, if no parameter of the provided name was found
   * @throws NullPointerException if `annotationMirror` is null
   */
  fun getAnnotationValue(
    annotationMirror: AnnotationMirror,
    annotationParameter: String
  ): AnnotationValue? {
    /* for ((key, value): Map.Entry<ExecutableElement?, AnnotationValue?> in annotationMirror.elementValues) {
         if (key!!.simpleName.toString() == annotationParameter) {
             return value
         }
     }*/
    return null
  }

  /**
   * Get a certain annotation parameter of a [TypeElement].
   * See [stackoverflow.com](https://stackoverflow.com/a/10167558) for more information.
   *
   * @param typeElement the type element, that contains the requested annotation
   * @param annotationClass the class of the requested annotation
   * @param annotationParameter the name of the requested annotation parameter
   * @return the requested parameter or null, if no annotation for the provided class was found or no annotation parameter was found
   * @throws NullPointerException if `typeElement` or `annotationClass` is null
   */
  fun getAnnotationValue(
    typeElement: TypeElement,
    annotationClass: Class<*>,
    annotationParameter: String,
  ): AnnotationValue? {
    val annotationMirror = getAnnotationMirror(typeElement, annotationClass)
    return if (annotationMirror != null) getAnnotationValue(
      annotationMirror,
      annotationParameter,
    ) else null
  }

  /**
   * Uses [Class.getSimpleName] to convert from [Class] to [String].
   *
   * @param classes
   * @return
   */
  private fun toString(classes: List<Class<*>>): List<String> {
    val list: MutableList<String> = ArrayList()
    classes.forEach {
      list.add(it.simpleName)
    }
    return list
  }
}
