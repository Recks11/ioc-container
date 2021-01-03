package com.rexijie.ioc.beans.factory;

import com.rexijie.ioc.beans.store.BeanStore;

public interface BeanFactory extends BeanStore {

    <T> void addBean(T beanInstance);

    <T> void addBean(Class<T> clazz);

    <T> void addBean(String key, Class<T> clazz);

    <T> void addBean(String key, T instance);

    <T> T getBean(Class<T> clazz);

    Object getBean(String name);

    <T> T getBean(String name, Class<T> clazz);
}
