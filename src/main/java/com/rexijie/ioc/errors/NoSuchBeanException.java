package com.rexijie.ioc.errors;

public class NoSuchBeanException extends RuntimeException {
    public NoSuchBeanException(String message) {
        super(message);
    }

    public NoSuchBeanException(Class<?> clazz) {
        super("No Beans of type " + clazz.getName() + " exists in the application context");
    }

    public NoSuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
