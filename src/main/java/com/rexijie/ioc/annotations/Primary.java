package com.rexijie.ioc.annotations;

import java.lang.annotation.*;

/**
 * Signifies if a bean is the default bean to be used of a particular type
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Primary {
}
