package com.rexijie.ioc.util;


import org.apache.log4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectUtil {
    private static final Logger logger = Logger.getLogger(ObjectUtil.class);

    private ObjectUtil() {
    }

    public static boolean checkNonNull(Object object) {
        return object != null;
    }



    // null consumers not really needed but why not?
    /**
     * Executes a callback if the specified object is not null,
     * with the object as a parameter
     *
     * @param object object to check null
     */
    public static <T> void doIfNotNull(T object, Consumer<T> objectConsumer) {
        if (object != null)
            objectConsumer.accept(object);
    }

    // not really needed, Just for fun
    public static <T, R> R returnIfNotNull(T object, Function<T, R> objectConsumer) {
        if (object == null) return null;
        return objectConsumer.apply(object);
    }

    public static <T> void doIfNull(T object, Consumer<T> consumer) {
        if (object == null) consumer.accept(null);
    }

    public static <T> T returnIfNull(T object, Supplier<T> objectConsumer) {
        if (object == null) {
            try {
                return objectConsumer.get();
            } catch (Exception ex) {
                logger.error("error calling supplier method");
                throw new RuntimeException(ex);
            }
        }
        return object;
    }
}
