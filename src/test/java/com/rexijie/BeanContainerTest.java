package com.rexijie;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.beans.factory.DefaultBeanFactory;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.DefaultApplicationContext;
import com.rexijie.ioc.errors.BeanCreationError;
import com.rexijie.ioc.errors.NoSuchBeanException;
import com.rexijie.mock.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


class BeanContainerTest {

    @Test
    void containerStoresAndRetrievesObjects() {
        ApplicationContext context = new DefaultApplicationContext();
        Project project = new Project();
        project.setName("Bean Factory");
        project.setDuration(Duration.ofDays(1L).toMinutes());
        context.addBean("projectClass", project);

        Project pr = context.getBean("projectClass", Project.class);

        assertEquals(pr, project);
    }

    @Test
    void canCreateBeanWithNoConstructorParam() {
        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(NoParam.class);
        NoParam obj = context.getBean(NoParam.class);
        assertNotNull(obj);
    }

    @Test
    void canCreateBeanWithOneConstructorParam() {
        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(OneParam.class);
        OneParam obj = context.getBean(OneParam.class);
        assertNotNull(obj);
        assertNotNull(obj.getNoParam());
    }

    @Test
    void canCreateBeanWithManyConstructorParam() {
        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(TwoParam.class);
        TwoParam obj = context.getBean(TwoParam.class);
        assertNotNull(obj);
        assertNotNull(obj.getNoParam());
        assertNotNull(obj.getOneParam());
        assertNotNull(obj.getOneParam().getNoParam());
    }

    @Test
    void throwsErrorIfNoBeanOfTypeInterfaceExists() {
        ApplicationContext context = new DefaultApplicationContext();
        Assertions.assertThrows(NoSuchBeanException.class, () ->
                        context.getBean(OneInterfaceParam.class),
                "Bean was Created from constructor interface but was meant to throw error");

        Assertions.assertThrows(NoSuchBeanException.class, () ->
                        context.getBean(Named.class),
                "Bean was created from interface but was meant to throw error");
    }

    @Test
    void throwsErrorWhenBeanCreatedWithJdkProvidedTypeInConstructor() {
        ApplicationContext context = new DefaultApplicationContext();
        Assertions.assertThrows(BeanCreationError.class,
                () -> context.addBean(StringParamClass.class),
                "Bean was created with class from Jdk provided class but was meant to throw error");
    }

    @Test
    void canCreateBeanWithInterfaceInConstructor() {
        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(NamedClass.class);
        context.addBean(OneInterfaceParam.class);

        OneInterfaceParam bean = context.getBean(OneInterfaceParam.class);

        assertNotNull(bean);
    }

    @Test
    void canCreateClassFromClassLoader() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> annotationProcessorClass = classLoader.loadClass("com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor");

        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(annotationProcessorClass);
        AnnotationProcessor annotationProcessor = context.getBean(AnnotationProcessor.class);

        assertTrue(annotationProcessor instanceof BeanAnnotationProcessor);
    }

    @Test
    void canCreateBeanFromAnnotatedMethod() {
        DefaultApplicationContext context = new DefaultApplicationContext();
        DefaultBeanFactory beanFactory = (DefaultBeanFactory) context.getBean(BeanFactory.class);

        context.addBean(InnerBeanClass.class);

        assertTrue(context.containsBean("namedBean"));
        assertTrue(context.containsBean("customBean"));
        assertTrue(context.containsBean(Named.class));
    }
}
