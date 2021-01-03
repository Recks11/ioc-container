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

        if (clazz.isAnnotationPresent(getProcessedAnnotation())) {
            Component annotation = clazz.getAnnotation(getProcessedAnnotation());
            String name = annotation.value();
            context.addBean(name, clazz);
        }
    }

    public Class<Component> getProcessedAnnotation() {
        return Component.class;
    }
}
