package com.rexijie.ioc.beans;

public interface BeanFactory extends BeanStore {

    String generateBeanName(Class<?> clazz);

    <T> void addBean(T beanInstance);

    <T> void addBean(Class<T> clazz);

    <T> void addBean(String key, Class<T> clazz);

    <T> void removeBean(Class<T> beanClass);
}
