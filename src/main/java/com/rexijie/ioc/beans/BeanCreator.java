package com.rexijie.ioc.beans;

import com.rexijie.ioc.annotations.Named;
import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.errors.BeanCreationException;
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
    private final Logger LOG = Logger.getLogger(BeanCreator.class);
    private final ApplicationContext context;

    private BeanCreator(ApplicationContext context) {
        this.context = context;
    }

    public static BeanCreator withContext(ApplicationContext context) {
        return new BeanCreator(context);
    }

    public synchronized BeanWrapper<?> createBean(BeanWrapper<Object> beanWrapper) {
        Object instance = createBean(beanWrapper.getName(), beanWrapper.getClazz());
        beanWrapper.setBean(instance);
        return beanWrapper;
    }

    public synchronized <T> BeanWrapper<?> createBean(String key, Class<T> clazz) {
        LOG.debug("Creating Bean of Type " + clazz.getName());
        if (key == null) key = clazz.getName();
        if (clazz.isPrimitive()) {
            throw new RuntimeException("cannot instantiate primitive classes");
        }
        if (clazz.isInterface()) {
            return context.getRawBean(clazz.getName());
        }

        Constructor<?>[] ctorArr = clazz.getConstructors();
        int len = ctorArr.length;

        if (len == 0) throw new BeanCreationException("No declared constructor for class " + clazz);

        Constructor<?> c = ctorArr[len - 1];
        int pCount = c.getParameterCount();
        T instance;
        if (pCount == 0) {
            Object o = instantiateBean(key, c);
            instance = clazz.cast(o);
        } else {
            Object[] objects = new Object[pCount];
            int idx = 0;

            for (Parameter par : c.getParameters()) {
                String name;

                if (par.isAnnotationPresent(Named.class)) {
                    name = par.getDeclaredAnnotation(Named.class).value();
                    objects[idx] = context.getBean(name);

                } else {
                    if (ClassUtils.isInternalType(par.getType())) {
                        objects[idx] = ClassUtils.getDefaultForPrimitiveType(par.getType());
                        continue;
                    }

                    if (par.getType().isInterface()) {
                        Class<?> interfaceClass = par.getType();
                        objects[idx] = context.getBean(interfaceClass);
                        continue;
                    }

                    name = par.getType().getName();
                    objects[idx] = context.getBean(name); // use stored bean
                    idx++;
                }
            }
            Object o = instantiateBean(key, c, objects);
            instance = clazz.cast(o);
        }
        LOG.debug("Created Bean of Type " + clazz.getName());
        return BeanWrapper.forInstance(instance);
    }

    // @TODO find classes this method does not work for
    private <T> T instantiateBean(String key, Constructor<T> c, Object... initArgs) {
        String name = key != null ? key : c.getDeclaringClass().getName();
        try {
            if (context.containsBean(name)) return context.getBean(key, c.getDeclaringClass());
            if (isInternalType(c.getDeclaringClass()))
                throw new BeanCreationException("Cannot Automatically create bean from internal class");

            executeBeforeCreate();
            T instance = c.newInstance(initArgs);
            executePostCreate();
            return instance;
        } catch (IllegalAccessException e) {
            LOG.error("No public constructor for class ".concat(c.getName()), e);
        } catch (InstantiationException e) {
            LOG.error("Cannot instantiate abstract class ".concat(c.getName()), e);
        } catch (InvocationTargetException e) {
            LOG.error("There was an error in the constructor of class ".concat(c.getName()), e);
        }
        throw new BeanCreationException("Error Creating Bean");
    }

    private void executePostCreate() {

    }

    private void executeBeforeCreate() {

    }
}