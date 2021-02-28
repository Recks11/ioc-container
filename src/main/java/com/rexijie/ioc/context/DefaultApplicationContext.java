package com.rexijie.ioc.context;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.ComponentAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.CompositeAnnotationProcessor;
import com.rexijie.ioc.beans.factory.AbstractBeanFactory;
import com.rexijie.ioc.beans.BeanCreator;
import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.environment.ApplicationEnvironment;
import com.rexijie.ioc.environment.Environment;
import com.rexijie.ioc.environment.EnvironmentVariableStore;
import com.rexijie.ioc.environment.InMemoryEnvironmentStore;

import java.util.ArrayList;
import java.util.List;

public class DefaultApplicationContext extends AbstractBeanFactory implements EditableApplicationContext {
    private String name;
    private final BeanCreator beanCreator = new BeanCreator(this);
    private final AnnotationProcessor annotationProcessor;
    private EnvironmentVariableStore envVars;
    private Environment environment;

    public DefaultApplicationContext() {
        super();
        this.name = this.toString();
        this.environment = new ApplicationEnvironment();
        List<AnnotationProcessor> annotationProcessors = new ArrayList<>();
        annotationProcessors.add(new ComponentAnnotationProcessor(this));
        annotationProcessors.add(new BeanAnnotationProcessor(this));
        this.annotationProcessor = new CompositeAnnotationProcessor(
                annotationProcessors
        );
        addBean(this);
        this.envVars = new InMemoryEnvironmentStore();
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

    public BeanFactory getFactory() {
        return this;
    }
    @Override
    public AnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        refresh();
    }

    /**
     * DO some bean post processing and then reload the context
     */
    @Override
    public void refresh() {

    }

    public void close() {

    }
}
