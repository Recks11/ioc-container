package com.rexijie.ioc.beans.factory;

import java.util.Set;

public class NoOpBeanFactory implements BeanFactory {
    @Override
    public <T> void addBean(T beanInstance) {

    }

    @Override
    public <T> void addBean(Class<T> clazz) {

    }

    @Override
    public <T> void addBean(String key, Class<T> clazz) {

    }

    @Override
    public <T> void addBean(String key, T instance) {

    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return null;
    }

    @Override
    public Object getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public <T> boolean containsBean(T beanInstance) {
        return false;
    }

    @Override
    public <T> boolean containsBean(Class<T> beanClass) {
        return false;
    }

    @Override
    public <T> void registerBean(String key, T bean) {

    }

    @Override
    public Set<String> getBeanNamesOfType(Class<?> clazz) {
        return null;
    }

    @Override
    public void removeBean(String key) {

    }
}
