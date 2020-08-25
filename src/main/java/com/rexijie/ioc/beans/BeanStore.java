package com.rexijie.ioc.beans;

import java.util.Map;

public interface BeanStore {

    default Map<String, BeanWrapper<?>> getBeanCache() {
        return null;
    }

    boolean containsBean(String name);

    <T> boolean containsBean(T beanInstance);

    <T> boolean containsBean(Class<T> beanClass);

    <T> T getBean(Class<T> clazz);

    Object getBean(String name);

    <T> T getBean(String name, Class<T> clazz);

    <T> void addBean(String key, T bean);

    void removeBean(String key);
}
