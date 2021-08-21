package com.rexijie.ioc.annotations.processor;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.Bean;
import com.rexijie.ioc.beans.factory.AbstractBeanFactory;
import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.errors.BeanCreationException;
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
                processBeanMethodAnnotation(method, beanWrapper);
        }
    }

    // This method populates the bean wrapper class with required fields
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

    protected void processBeanMethodAnnotation(Method method, BeanWrapper<?> beanWrapper) {
        Class<?> returnType = method.getReturnType();
        String packageName = returnType.getPackage().getName();
        if (returnType.isPrimitive())
            throw new BeanCreationException("Cannot instantiate primitive type");

        if (packageName.startsWith("java") | packageName.startsWith("jdk"))
            throw new BeanCreationException("Cannot Automatically create bean from internal class");

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
            Object obj = method.invoke(beanWrapper.getBean(), args);
            BeanWrapper<?> bw = new BeanWrapper<>(obj);
            bw.setName(name);
            processBeanAnnotation(annotation, bw);

            factory.registerBean(name, bw);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanCreationException(e);
        }
    }
}
