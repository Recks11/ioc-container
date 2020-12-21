package com.rexijie.ioc.annotations.processor;

import com.rexijie.ioc.annotations.AnnotationProcessor;

import java.util.List;

public class CompositeAnnotationProcessor implements AnnotationProcessor {
    List<AnnotationProcessor> annotationProcessors;

    public CompositeAnnotationProcessor(List<AnnotationProcessor> annotationProcessors) {
        this.annotationProcessors = annotationProcessors;
    }

    @Override
    public void processAnnotation(Object annotatedClass) {
        for (AnnotationProcessor processor :
                annotationProcessors) {
            processor.processAnnotation(annotatedClass);
        }
    }
}
