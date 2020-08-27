package com.rexijie.ioc.beans;

import java.util.Map;

public interface BeanStore {

    boolean containsBean(String name);

    <T> boolean containsBean(T beanInstance);

    <T> boolean containsBean(Class<T> beanClass);

    <T> void registerBean(String key, T bean);

    void removeBean(String key);
}
