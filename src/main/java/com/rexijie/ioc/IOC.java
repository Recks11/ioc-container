package com.rexijie.ioc;

import com.rexijie.ioc.annotations.processor.ComponentAnnotationProcessor;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.DefaultApplicationContext;
import com.rexijie.ioc.util.ClassUtils;
import com.rexijie.ioc.util.ReflectionUtils;

public class IOC {
    private static final ApplicationContext applicationContext = new DefaultApplicationContext();
    private static final ClassLoader classLoader = ClassUtils.getRootClassLoader();

    public static void run(Class<?> application) {
        String _package = application.getPackage().getName();
        Class<?>[] classesInPackage = ReflectionUtils.findClassesInPackage(_package);
        ComponentAnnotationProcessor cap = new ComponentAnnotationProcessor(applicationContext);

        for (Class<?> clazz: classesInPackage) {
            cap.processAnnotation(clazz);
        }
    }
}
