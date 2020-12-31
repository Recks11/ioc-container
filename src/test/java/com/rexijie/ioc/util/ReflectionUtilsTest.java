package com.rexijie.ioc.util;

import com.rexijie.ioc.beans.BeanCreator;
import com.rexijie.ioc.beans.BeanFactory;
import com.rexijie.ioc.beans.BeanStore;
import com.rexijie.ioc.beans.DefaultBeanFactory;
import com.rexijie.mock.Named;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilsTest {

    @Test
    void findClassesInPackage() {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        BeanCreator beanCreator = new BeanCreator(beanFactory);

        Class<?>[] classesInPackage = ReflectionUtils.findClassesInPackage("com.rexijie.mock");
        for (Class<?> clazz: classesInPackage) {
            if (clazz.isInterface()) continue;

            beanCreator.createBean(clazz.getName(), clazz);
        }

        int beanTypesFound = beanFactory.getBeanStore().getBeanNamesOfType(Named.class).size();

        Assertions.assertTrue(beanTypesFound > 0);
    }
}