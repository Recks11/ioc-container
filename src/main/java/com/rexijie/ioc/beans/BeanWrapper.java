package com.rexijie.ioc.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
    private Method[] methods;
    private T bean;
    private boolean isPrimary;
    private boolean isInstantiated;

    public BeanWrapper() {
    }

    public BeanWrapper(T bean) {
        this.bean = bean;
        this.name = bean.getClass().getName();
        this.typename = bean.getClass().getName();
        this.tClass = bean.getClass();
        this.methods = bean.getClass().getDeclaredMethods();
        this.isInstantiated = true;
        calculateNumberOfDependencies();
    }

    public BeanWrapper(Class<?> tClass) {
        this.name = tClass.getName();
        this.typename = tClass.getName();
        this.tClass = tClass;
        this.methods = tClass.getDeclaredMethods();
        this.isInstantiated = false;
        calculateNumberOfDependencies();
    }

    public BeanWrapper(BeanWrapper<T> other) {
        this.name = other.name;
        this.bean = other.getBean();
        this.typename = other.typename;
        this.tClass = other.tClass;
        this.methods = other.methods;
        this.isInstantiated = other.isInstantiated;
        this.isPrimary = other.isPrimary;
        this.numberOfDependencies = other.numberOfDependencies;
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
        if (bean != null) this.isInstantiated = true;
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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Class<?> getClazz() {
        return tClass;
    }

    public Method[] getMethods() {
        return methods;
    }

    public boolean isInstantiated() {
        return isInstantiated;
    }

    public void setInstantiated(boolean instantiated) {
        isInstantiated = instantiated;
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
