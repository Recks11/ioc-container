package com.rexijie;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.DefaultApplicationContext;
import com.rexijie.ioc.errors.NoSuchBeanException;
import com.rexijie.mock.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

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
        context.addBean(new NoParam());
        NoParam obj = context.getBean(NoParam.class);
        assertNotNull(obj);
    }

    @Test
    void canCreateBeanWithOneConstructorParam() {
        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(new OneParam(new NoParam()));
        OneParam obj = context.getBean(OneParam.class);
        assertNotNull(obj);
        assertNotNull(obj.getNoParam());
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
    void canCreateBeanWithProvidedConstructorBean() {
        ApplicationContext context = new DefaultApplicationContext();
        context.addBean(new NoParam());
        context.addBean(OneParam.class.getName(), OneParam.class);
        OneParam bean = context.getBean(OneParam.class);
        assertNotNull(bean);

        assertThrows(NoSuchBeanException.class, () -> context.addBean("TP", OneInterfaceParam.class));
    }

    @Test
    void canCreateClassFromClassLoader() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> annotationProcessorClass = classLoader.loadClass("com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor");

        ApplicationContext context = new DefaultApplicationContext();
        context.addBean("com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor", annotationProcessorClass);
        AnnotationProcessor annotationProcessor = context.getBean(AnnotationProcessor.class);

        assertTrue(annotationProcessor instanceof BeanAnnotationProcessor);
    }

    @Test
    void canCreateBeanFromAnnotatedMethod() {
        DefaultApplicationContext context = new DefaultApplicationContext();

        context.addBean(new InnerBeanClass());
        context.refresh();

        assertTrue(context.containsBean("namedBean"));
        assertTrue(context.containsBean("customBean"));
        assertTrue(context.containsBean(Named.class));
    }

    @Test
    void whenRemoveBeanRemoveTypes() {
        DefaultApplicationContext context = new DefaultApplicationContext();
        context.addBean(new InnerBeanClass());
        context.refresh();

        assertTrue(context.containsBean("namedBean"));
        assertTrue(context.containsBean("customBean"));
        assertTrue(context.containsBean(Named.class));

        context.removeBean("namedBean");
        Set<String> beanNamesOfType = context.getBeanNamesOfType(Named.class);
        Set<String> beanNamesOfTypeNamedClass = context.getBeanNamesOfType(NamedClass.class);

        assertEquals(1, beanNamesOfType.size());
        assertEquals(1, beanNamesOfTypeNamedClass.size());
    }
}
