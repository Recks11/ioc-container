package com.rexijie.ioc.beans;

import org.apache.log4j.Logger;

/**
 * This class is the skeleton for a bean store
 * it contains the bean cache which stores instances of beans
 * and default methods to add and remove beans
 */
public abstract class AbstractBeanFactory implements BeanFactory {
    private final Logger logger = Logger.getLogger(AbstractBeanFactory.class);
    private final BeanCreator bC = new BeanCreator(this);
    private BeanKeyGenerator beanKeyGenerator = new BeanKeyGenerator();
    private BeanStore beanStore = new DefaultBeanStore();

    /**
     * Creates a bean store with itself as a bean
     */
    public AbstractBeanFactory() {
        addBean(this);
    }

    public AbstractBeanFactory(BeanStore instance) {
        this.addBean(instance);
    }

    @Override
    public String generateBeanName(Class<?> clazz) {
        return beanKeyGenerator.generateKey(clazz);
    }

    public <T> void addBean(T beanInstance) {
        addBean(beanInstance.getClass().getName(), beanInstance);
    }

    public <T> void addBean(Class<T> clazz) {
        addBean(null, clazz);
        logInstanceCreation(clazz);
    }

    public <T> void addBean(String key, Class<T> clazz) {
        bC.createBean(key, clazz);
    }

    public <T> void addBean(String key, T bean) {
        beanStore.addBean(key, bean);
    }

    public void removeBean(String key) {
        beanStore.removeBean(key);
    }

    public <T> void removeBean(Class<T> beanClass) {
        String name = beanClass.getName();
        removeBean(name);
    }

    @Override
    public boolean containsBean(String name) {
        return beanStore.containsBean(name);
    }

    @Override
    public <T> boolean containsBean(T beanInstance) {
        return containsBean(beanInstance.getClass().getName());
    }

    @Override
    public <T> boolean containsBean(Class<T> beanClass) {
        return containsBean(beanClass.getName());
    }


    @Override
    public Object getBean(String name) {
        return beanStore.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanStore.getBean(clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return beanStore.getBean(name, clazz);
    }

    public void setBeanStore(BeanStore beanStore) {
        this.beanStore = beanStore;
    }

    public BeanKeyGenerator getBeanKeyGenerator() {
        return beanKeyGenerator;
    }

    public void setBeanKeyGenerator(BeanKeyGenerator beanKeyGenerator) {
        this.beanKeyGenerator = beanKeyGenerator;
    }

    public BeanStore getBeanStore() {
        return beanStore;
    }

    protected <T> void logInstanceCreation(Class<T> clazz) {
        logger.debug("Created instance for class: ".concat(clazz.getName()));
    }

    protected void logBeanRetrieval(String name) {
        logger.debug("Retrieving bean: ".concat(name));
    }
}
