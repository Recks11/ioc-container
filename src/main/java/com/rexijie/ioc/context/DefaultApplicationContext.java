package com.rexijie.ioc.context;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.ComponentAnnotationProcessor;
import com.rexijie.ioc.annotations.processor.CompositeAnnotationProcessor;
import com.rexijie.ioc.beans.BeanCreator;
import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.beans.factory.AbstractBeanFactory;
import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.beans.store.DefaultBeanStore;
import com.rexijie.ioc.environment.ApplicationEnvironment;
import com.rexijie.ioc.environment.Environment;
import org.apache.log4j.Logger;

import java.util.*;

public class DefaultApplicationContext extends AbstractBeanFactory implements ConfigurableApplicationContext {
    private static Logger LOG = Logger.getLogger(DefaultApplicationContext.class);
    private String name;
    private final BeanCreator beanCreator = BeanCreator.withContext(this);
    private final AnnotationProcessor annotationProcessor;
    private Environment environment;

    public DefaultApplicationContext() {
        super();
        LOG.debug("initializing Application context");
        this.name = this.toString();
        this.environment = new ApplicationEnvironment();
        List<AnnotationProcessor> annotationProcessors = new ArrayList<>();
        annotationProcessors.add(new ComponentAnnotationProcessor(this));
        annotationProcessors.add(new BeanAnnotationProcessor(this));
        this.annotationProcessor = new CompositeAnnotationProcessor(
                annotationProcessors
        );
        getBeanStore().addBean(this);
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
        LOG.debug("refreshing application context");
        DefaultBeanStore store = (DefaultBeanStore) getBeanStore();
        Map<String, BeanWrapper<?>> storeCopy = new HashMap<>(store.getBeanCache());
        store.getBeanCache().clear();
        store.getBeanTypeMap().clear();
        storeCopy.values().forEach(beanWrapper -> {
            getAnnotationProcessor().processAnnotation(beanWrapper);
            store.addBean(beanWrapper);
        });
    }

    public void close() {

    }

    @Override
    public boolean containsBean(String name) {
        return getBeanStore().containsBean(name);
    }

    @Override
    public <T> boolean containsBean(T beanInstance) {
        return getBeanStore().containsBean(beanInstance);
    }

    @Override
    public <T> boolean containsBean(Class<T> beanClass) {
        return getBeanStore().containsBean(beanClass);
    }

    @Override
    public <T> void addBean(T beanInstance) {
        getBeanStore().addBean(beanInstance);
    }

    /**
     * Create a bean and add it to the context while providing stored beans
     * as constructor parameters.
     */
    @Override
    public <T> void addBean(String key, Class<T> clazz) {
        BeanWrapper<?> instance = beanCreator.createBean(key, clazz);
        getBeanStore().addBean(instance);
    }

    @Override
    public <T> void addBean(String key, T instance) {
        getBeanStore().addBean(key, instance);
    }

    @Override
    public void addBean(BeanWrapper<?> beanWrapper) {
        getBeanStore().addBean(beanWrapper);
    }

    @Override
    public Set<String> getBeanNamesOfType(Class<?> clazz) {
        return getBeanStore().getBeanNamesOfType(clazz);
    }

    @Override
    public void removeBean(String key) {
        getBeanStore().removeBean(key);
    }
}
