package com.rexijie.ioc.util;

import com.rexijie.ioc.errors.BeanCreationException;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanUtils {
    private static final Logger logger = Logger.getLogger(BeanUtils.class);

    public static <T, A> A instantiateBean(Class<T> clazz, Class<A> type) {
        if (type.isAssignableFrom(clazz)) {
            T tInstance = instantiateBean(clazz);
            return type.cast(tInstance);
        }
        throw new BeanCreationException("Expected bean type "+ type.getName() +
                "is not assignable to provided type "+ clazz.getName());
    }
    public static <T> T instantiateBean(Class<T> clazz) {
        if (clazz.isInterface()) {
            throw new BeanCreationException("cannot instantiate interface");
        }
        Constructor<?> primaryConstructor = findPrimaryConstructor(clazz);
        Object instance = instantiateClass(primaryConstructor);
        return clazz.cast(instance);
    }

    private static <T> Constructor<?> findPrimaryConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            Constructor<?>[] ctorArr = clazz.getConstructors();
            int constructorArrayLength = ctorArr.length;
            if (constructorArrayLength == 0) throw new BeanCreationException("No declared constructor for class " + clazz);

            Constructor<?> c = ctorArr[constructorArrayLength - 1];
            return c;
        }
    }

    private static  <T> T instantiateClass(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (IllegalAccessException e) {
            logger.error("No public constructor for class ".concat(constructor.getName()), e);
        } catch (InstantiationException e) {
            logger.error("Cannot instantiate abstract class ".concat(constructor.getName()), e);
        } catch (InvocationTargetException e) {
            logger.error("There was an error in the constructor of class ".concat(constructor.getName()), e);
        }
        return null;
    }
}
