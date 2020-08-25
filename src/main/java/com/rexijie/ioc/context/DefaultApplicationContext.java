package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.BeanFactory;
import com.rexijie.ioc.container.DefaultBeanFactory;

public class DefaultApplicationContext implements ApplicationContext {
    private String name;
    private BeanFactory beanFactory = new DefaultBeanFactory();

    public DefaultApplicationContext() {
        this.name = this.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    @Override
    public <T> boolean containsBean(T beanInstance) {
        return beanFactory.containsBean(beanInstance);
    }

    @Override
    public <T> boolean containsBean(Class<T> beanClass) {
        return beanFactory.containsBean(beanClass);
    }

    @Override
    public String generateBeanName(Class<?> clazz) {
        return beanFactory.generateBeanName(clazz);
    }

    @Override
    public <T> void addBean(T beanInstance) {
        beanFactory.addBean(beanInstance);
    }

    @Override
    public <T> void addBean(Class<T> beanClass) {
        beanFactory.addBean(beanClass);
    }

    @Override
    public <T> void addBean(String key, Class<T> beanClass) {
        beanFactory.addBean(key, beanClass);
    }

    @Override
    public <T> void addBean(String key, T bean) {
        beanFactory.addBean(key, bean);
    }

    @Override
    public void removeBean(String key) {
        beanFactory.removeBean(key);
    }

    @Override
    public <T> void removeBean(Class<T> beanClass) {
        beanFactory.removeBean(beanClass);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return beanFactory.getBean(name, clazz);
    }

    @Override
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
