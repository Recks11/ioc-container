package com.rexijie.ioc.annotations.processor;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.Bean;
import com.rexijie.ioc.beans.AbstractBeanFactory;
import com.rexijie.ioc.beans.BeanFactory;
import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.errors.BeanCreationError;
import com.rexijie.ioc.errors.NoSuchBeanException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * annotation processor for Classes and methods annotated with {@link Bean}
 */
public class BeanAnnotationProcessor implements AnnotationProcessor {

    private final BeanFactory factory;

    public BeanAnnotationProcessor(BeanFactory beanFactory) {
        this.factory = beanFactory;
    }

    @Override
    public void processAnnotation(Object annotatedMethod) {
        BeanWrapper<?> beanWrapper = (BeanWrapper<?>) annotatedMethod;
        Class<?> annotatedClass = beanWrapper.getBean().getClass();
        if (annotatedClass.isAnnotationPresent(Bean.class)) {
            Bean annotation = annotatedClass.getAnnotation(Bean.class);
            processBeanAnnotation(annotation, (BeanWrapper<?>) annotatedMethod);
        }

        for (Method method : beanWrapper.getBean().getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Bean.class))
                processBeanMethodAnnotation(method, beanWrapper.getBean());
        }
    }

    protected void processBeanAnnotation(Bean annotation, BeanWrapper<?> beanWrapper) {
        if (annotation.primary()) {
            beanWrapper.setPrimary(true);
        }

        if (!annotation.value().isEmpty()) {
            String defaultName = beanWrapper.getBean().getClass().getName();
            if (defaultName.equals(beanWrapper.getName())) {
                beanWrapper.setName(annotation.value());
            }
        }
    }

    protected void processBeanMethodAnnotation(Method method, Object thisObj) {
        Class<?> returnType = method.getReturnType();
        String packageName = returnType.getPackage().getName();
        if (returnType.isPrimitive())
            throw new BeanCreationError("Cannot instantiate primitive type");

        if (packageName.startsWith("java") | packageName.startsWith("jdk"))
            throw new BeanCreationError("Cannot Automatically create bean from internal class");

        Bean annotation = method.getAnnotation(Bean.class);
        String name = annotation.value().isEmpty() ? method.getName() : annotation.value();

        int paramCount = method.getParameterCount();
        int index = 0;
        Object[] args = new Object[paramCount];
        for (Parameter parameter : method.getParameters()) {
            if (!factory.containsBean(parameter.getType())) throw new NoSuchBeanException(parameter.getType());
            args[index] = factory.getBean(parameter.getType());
            index++;
        }

        try {
            Object obj = method.invoke(thisObj, args);
            factory.registerBean(name, obj);

            BeanWrapper<?> beanWrapper = ((AbstractBeanFactory)factory).getBeanCache().get(name);
            processBeanAnnotation(annotation, beanWrapper);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanCreationError(e);
        }
    }
}
