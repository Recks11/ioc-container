package com.rexijie.ioc.context;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.ComponentAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.CompositeAnnotationProcessor;
import com.rexijie.ioc.beans.AbstractBeanFactory;
import com.rexijie.ioc.beans.BeanCreator;

import java.util.ArrayList;
import java.util.List;

public class DefaultApplicationContext extends AbstractBeanFactory implements ApplicationContext {
    private String name;
    private final BeanCreator beanCreator = new BeanCreator(this);
    private final AnnotationProcessor annotationProcessor;

    public DefaultApplicationContext() {
        super();
        this.name = this.toString();
        List<AnnotationProcessor> annotationProcessors = new ArrayList<>();
        annotationProcessors.add(new ComponentAnnotationProcessor(this));
        annotationProcessors.add(new BeanAnnotationProcessor(this));
        this.annotationProcessor = new CompositeAnnotationProcessor(
                annotationProcessors
        );
        addBean(this);
    }

    @Override
    protected <T> void createBean(String name, Class<T> clazz) {
        beanCreator.createBean(name, clazz);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public AnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }

    @Override
    public void refresh() {

    }
}
