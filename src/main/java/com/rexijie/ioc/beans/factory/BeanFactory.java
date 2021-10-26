package com.rexijie.ioc.beans.factory;

import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.errors.NoSuchBeanException;

public interface BeanFactory {

    BeanStore getBeanStore();

    <T> T getBean(Class<T> clazz) throws NoSuchBeanException;

    Object getBean(String name) throws NoSuchBeanException;

    <T> T getBean(String name, Class<T> clazz) throws NoSuchBeanException;
}
