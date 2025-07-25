package com.jetview.util;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Roman Kochergin
 */
public class ClassUtils {

    public static <A extends Annotation> Optional<A> getAnnotation(Class<?> cls, Class<A> annotationClass) {
        Objects.requireNonNull(cls);
        Objects.requireNonNull(annotationClass);

        if (cls.isAnnotationPresent(annotationClass)) {
            return Optional.of(cls.getAnnotation(annotationClass));
        }

        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            if (superclass.isAnnotationPresent(annotationClass)) {
                return Optional.of(superclass.getAnnotation(annotationClass));
            }
            superclass = superclass.getSuperclass();
        }

        return Optional.empty();
    }

    private ClassUtils() {
    }
}
