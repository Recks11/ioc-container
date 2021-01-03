package com.rexijie.ioc.annotations;

import java.lang.annotation.*;

/**
 * Classes decorated with this annotation will be picked up while scanning for annotated classes.
 * they will then be instantiated and added to the context.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Component {

    String value();
}
