package com.rexijie.ioc.beans;

import java.util.Set;

public interface BeanStore {

    boolean containsBean(String name);

    <T> boolean containsBean(T beanInstance);

    <T> boolean containsBean(Class<T> beanClass);

    <T> void registerBean(String key, T bean);

    Set<String> getBeanNamesOfType(Class<?> clazz);

    void removeBean(String key);


    /**
     * Default method to get the raw value of a bean without parsing it. this method is optional
     * and ideally will not need to be used, but if the bean wrapper is needed, then this should be
     * overriden
     * @param key the key of the bean
     */
    default BeanWrapper<?> getRawBean(String key) {
        return null;
    }
}
