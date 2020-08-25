package com.rexijie.ioc.errors;

/**
 * Exception thrown when trying to create a new instance of a bean
 * ideally, this exception should never be reached
 */
public class BeanCreationError extends RuntimeException {
    public BeanCreationError() {
        super();
    }

    public BeanCreationError(String message) {
        super(message);
    }

    public BeanCreationError(Throwable cause) {
        super(cause);
    }
}
