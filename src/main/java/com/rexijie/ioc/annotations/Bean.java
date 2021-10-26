package com.rexijie.ioc.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Bean {

    /**
     *  The name of the bean
     */
    String name() default "";

    boolean primary() default false;
}
