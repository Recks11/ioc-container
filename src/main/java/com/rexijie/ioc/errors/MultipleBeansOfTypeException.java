package com.rexijie.ioc.errors;

public class MultipleBeansOfTypeException extends RuntimeException {

    public MultipleBeansOfTypeException() {
    }

    public MultipleBeansOfTypeException(Class<?> clazz) {
        super("more than one beans of type " + clazz.getName() + " please specify a bean name or annotate a bean implementing this interface with with @Primary");
    }

    public MultipleBeansOfTypeException(String message) {
        super(message);
    }
}
