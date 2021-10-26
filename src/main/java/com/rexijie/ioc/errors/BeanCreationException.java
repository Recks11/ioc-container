package com.rexijie.ioc.errors;

/**
 * Exception thrown when trying to create a new instance of a bean
 * ideally, this exception should never be reached
 */
public class BeanCreationException extends RuntimeException {
    public BeanCreationException() {
        super();
    }

    public BeanCreationException(String message) {
        super(message);
    }

    public BeanCreationException(Throwable cause) {
        super(cause);
    }

    public BeanCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
