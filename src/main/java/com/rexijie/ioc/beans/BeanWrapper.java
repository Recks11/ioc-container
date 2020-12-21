package com.rexijie.ioc.beans;

import java.lang.reflect.Constructor;

/**
 * wrapper class to hold bean objects in the bean store
 * it holds values used to sort the bean or do processing tasks
 * @param <T> the type of the class
 */

public class BeanWrapper<T> {

    private String name;
    private int numberOfDependencies;
    private String typename;
    private Class<?> tClass;
    private T bean;
    private boolean isPrimary;

    public BeanWrapper() {
    }

    public BeanWrapper(T bean) {
        this.bean = bean;
        this.name = bean.getClass().getName();
        this.typename = bean.getClass().getName();
        this.tClass = bean.getClass();
        calculateNumberOfDependencies();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
        calculateNumberOfDependencies();
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public int getDependencyCount() {
        return this.numberOfDependencies;
    }

    private void calculateNumberOfDependencies() {
        Constructor<?>[] constructors = bean.getClass().getConstructors();
        int paramCount = 0;
        int lastParamCount;

        for (Constructor<?> constructor : constructors) {
            lastParamCount = constructor.getParameterCount();

            paramCount = Math.max(paramCount, lastParamCount);
        }

        numberOfDependencies = paramCount;
    }
}
