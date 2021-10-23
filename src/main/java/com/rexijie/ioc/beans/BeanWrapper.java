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

    private BeanWrapper() {
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
        if (!tClass.isInstance(bean)) throw new RuntimeException("supplied bean not instance of definition class");
        this.bean = bean;
        this.isInstantiated = true;
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
        Constructor<?>[] constructors = getClazz().getConstructors();
        int paramCount = 0;
        int lastParamCount;

        for (Constructor<?> constructor : constructors) {
            lastParamCount = constructor.getParameterCount();

            paramCount = Math.max(paramCount, lastParamCount);
        }

        numberOfDependencies = paramCount;
    }

    public static <T> BeanWrapper<T> create() {
        return new BeanWrapper<>();
    }

    public static <T> BeanWrapper<T> forInstance(T bean) {
        BeanWrapper<T> wrapper = new BeanWrapper<>();
        wrapper.bean = bean;
        wrapper.name = bean.getClass().getName();
        wrapper.typename = bean.getClass().getName();
        wrapper.tClass = bean.getClass();
        wrapper.methods = bean.getClass().getDeclaredMethods();
        wrapper.isInstantiated = true;
        wrapper.calculateNumberOfDependencies();
        return wrapper;
    }

    public static <T> BeanWrapper<T> fromWrapper(BeanWrapper<T> other) {
        BeanWrapper<T> wrapper = BeanWrapper.create();
        wrapper.name = other.name;
        wrapper.bean = other.getBean();
        wrapper.typename = other.typename;
        wrapper.tClass = other.tClass;
        wrapper.methods = other.methods;
        wrapper.isInstantiated = other.isInstantiated;
        wrapper.isPrimary = other.isPrimary;
        wrapper.numberOfDependencies = other.numberOfDependencies;
        return wrapper;
    }

    public static <T> BeanWrapper<T> forClass(Class<?> tClass) {
        BeanWrapper<T> wrapper = BeanWrapper.create();
        wrapper.name = tClass.getName();
        wrapper.typename = tClass.getName();
        wrapper.tClass = tClass;
        wrapper.methods = tClass.getDeclaredMethods();
        wrapper.isInstantiated = false;
        wrapper.calculateNumberOfDependencies();
        return wrapper;
    }
}
