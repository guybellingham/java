package com.gus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for 'mapping' an internal global data enum to the database table it should be loaded into.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableMapping {

    /** The name of the database table to map to. */
    String name() default "";

}
