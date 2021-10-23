package com.rexijie.ioc.util;

import com.rexijie.ioc.beans.BeanCreator;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.DefaultApplicationContext;
import com.rexijie.mock.Named;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {

    @Test
    void findClassesInPackage() {
        ApplicationContext context = new DefaultApplicationContext();

        Class<?>[] classesInPackage = ReflectionUtils.findClassesInPackage("com.rexijie.mock");

        Assertions.assertTrue(classesInPackage.length > 0);
    }
}