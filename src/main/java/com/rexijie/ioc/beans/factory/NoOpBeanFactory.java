package com.rexijie.ioc.beans.factory;

import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.errors.NoSuchBeanException;

public class NoOpBeanFactory implements BeanFactory {

    @Override
    public BeanStore getBeanStore() {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws NoSuchBeanException {
        return null;
    }

    @Override
    public Object getBean(String name) throws NoSuchBeanException {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws NoSuchBeanException {
        return null;
    }
}
