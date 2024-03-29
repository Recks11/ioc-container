package com.rexijie.ioc.beans.factory;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.CompositeAnnotationProcessor;
import com.rexijie.ioc.beans.BeanCreator;
import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.DefaultApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DefaultBeanFactory extends AbstractBeanFactory {
    private final BeanCreator bC = BeanCreator.withContext(new DefaultApplicationContext());
    private AnnotationProcessor annotationProcessor;

    public DefaultBeanFactory() {
        super();
        List<AnnotationProcessor> annotationProcessors = new ArrayList<>();
        this.annotationProcessor = new CompositeAnnotationProcessor(
                annotationProcessors
        );
    }

    /**
     * create an instance of a bean factory with a bean added to the beancache
     */
    public DefaultBeanFactory(BeanStore instance) {
        super(instance);
    }

    @Override
    protected <T> void createBean(String name, Class<T> clazz) {
        bC.createBean(name, clazz);
    }

    @Override
    public AnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }
}
