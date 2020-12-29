package com.rexijie.ioc.annotations.processor;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.Component;
import com.rexijie.ioc.context.ApplicationContext;

public class ComponentAnnotationProcessor implements AnnotationProcessor {

    private final ApplicationContext context;

    public ComponentAnnotationProcessor(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void processAnnotation(Object annotatedClass) {
        if (!(annotatedClass instanceof Class))
            return;
        Class<?> clazz = (Class<?>) annotatedClass;

        if (clazz.isAnnotationPresent(Component.class)) {
            context.addBean(clazz);
        }
    }
}
