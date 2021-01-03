package com.rexijie.ioc.beans;

import com.rexijie.ioc.annotations.Named;
import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.errors.BeanCreationException;
import com.rexijie.ioc.errors.NoSuchBeanException;
import com.rexijie.ioc.util.ClassUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

import static com.rexijie.ioc.util.ClassUtils.isInternalType;

/**
 * The BeanCreator is responsible for creating beans
 * Beans created by this class will be added to the provided {@link BeanStore}
 * <br/>
 * This does not support Java provided classes like String, Long.. e.t.c..
 * but why would you make native bean types?
 * It is meant to instantiate Custom beans and add them to the BeanStore.
 * <br/>
 * When a class has multiple constructors, it will recursively call itself
 * for all constructors and instantiate beans, then reuse those created beans to create
 * an instance of the class.
 */
public class BeanCreator {
    private final Logger logger = Logger.getLogger(BeanCreator.class);
    private final BeanFactory beanFactory;

    public BeanCreator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public synchronized <T> String createBean(String key, Class<T> clazz) {
        if (key == null) key = clazz.getName();
        if (clazz.isPrimitive()) {
            throw new RuntimeException("cannot instantiate primitive classes");
        }
        if (clazz.isInterface()) {
            if (!beanFactory.containsBean(key)) {
                throw new NoSuchBeanException(clazz);
            }
            return null;
        }

        Constructor<?>[] ctorArr = clazz.getConstructors();
        int len = ctorArr.length;

        if (len == 0) throw new BeanCreationException("No declared constructor for class " + clazz);

        Constructor<?> c = ctorArr[len - 1];
        int pCount = c.getParameterCount();

        if (pCount == 0) {
            return createAndStoreBean(key, c);
        } else {
            Object[] objects = new Object[pCount];
            int idx = 0;

            for (Parameter par : c.getParameters()) {
                String name;

                if (par.isAnnotationPresent(Named.class)) {
                    name = par.getDeclaredAnnotation(Named.class).value();
                    objects[idx] = beanFactory.getBean(name);

                } else {
                    if (ClassUtils.isInternalType(par.getType())) {
                        objects[idx] = ClassUtils.getDefaultForPrimitiveType(par.getType());
                        continue;
                    }

                    if (par.getType().isInterface()) {
                        Class<?> interfaceClass = par.getType();
                        objects[idx] = beanFactory.getBean(interfaceClass);
                        continue;
                    }

                    name = par.getType().getName();
                    if (!beanFactory.containsBean(name)) { // instantiate bean if it doesn't exist
                        createBean(name, par.getType());
                    }
                    objects[idx] = beanFactory.getBean(name); // use stored bean
                    idx++;
                }
            }
            return createAndStoreBean(key, c, objects);
        }
    }

    // @TODO find classes this method does not work for
    private <T> String createAndStoreBean(String key, Constructor<T> c, Object... initArgs) {
        String name = key != null ? key : c.getDeclaringClass().getName();
        try {
            if (beanFactory.containsBean(name)) return name;
            if (isInternalType(c.getDeclaringClass()))
                throw new BeanCreationException("Cannot Automatically create bean from internal class");

            executeBeforeCreate();
            T instance = c.newInstance(initArgs);
            executePostCreate();
            beanFactory.registerBean(name, instance);
            return name;
        } catch (IllegalAccessException e) {
            logger.error("No public constructor for class ".concat(c.getName()), e);
        } catch (InstantiationException e) {
            logger.error("Cannot instantiate abstract class ".concat(c.getName()), e);
        } catch (InvocationTargetException e) {
            logger.error("There was an error in the constructor of class ".concat(c.getName()), e);
        }
        return name;
    }

    private void executePostCreate() {

    }

    private void executeBeforeCreate() {

    }
}