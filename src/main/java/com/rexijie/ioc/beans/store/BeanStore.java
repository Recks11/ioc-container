package com.rexijie.ioc.beans.store;

import com.rexijie.ioc.beans.BeanWrapper;

import java.util.Set;

public interface BeanStore {

    boolean containsBean(String name);

    <T> boolean containsBean(T beanInstance);

    <T> boolean containsBean(Class<T> beanClass);

    <T> void addBean(T beanInstance);

    <T> void addBean(String key, Class<T> clazz);

    <T> void addBean(String key, T instance);

    void addBean(BeanWrapper<?> beanWrapper);

    Set<String> getBeanNamesOfType(Class<?> clazz);

    void removeBean(String key);


    /**
     * Default method to get the raw value of a bean without parsing it. this method is optional
     * and ideally will not need to be used, but if the bean wrapper is needed, then this should be
     * overriden
     * @param key the key of the bean
     */
    default BeanWrapper<?> getRawBean(String key) {
        throw new RuntimeException("Method Not Implemented");
    }
}
