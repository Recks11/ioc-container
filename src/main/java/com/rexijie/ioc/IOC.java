package com.rexijie.ioc;

import com.rexijie.ioc.annotations.processor.ComponentAnnotationProcessor;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.EditableApplicationContext;
import com.rexijie.ioc.util.BeanUtils;
import com.rexijie.ioc.util.ClassUtils;
import com.rexijie.ioc.util.ReflectionUtils;
import org.apache.log4j.Logger;

public class IOC {
    private static final Logger LOGGER = Logger.getLogger(IOC.class);
    private static final String DEFAULT_APPLICATION_CONTEXT_CLASS = "com.rexijie.ioc.context.DefaultApplicationContext";
    private String rootPackage;
    private final Class<?> mainClass;

    private IOC(Class<?> main) {
        this.mainClass = getMainClass();
        setRootPackage();
    }

    private Class<?> getMainClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement element : stackTrace) {
                if (element.getMethodName().equals("main")) {
                    String className = element.getClassName();
                    return ClassUtils.getRootClassLoader().loadClass(className);
                }
            }
        } catch (Exception ex) {
            // do nothing
        }

        return mainClass;
    }

    public static void run(Class<?> application) {
        IOC iocApplication = new IOC(application);
        iocApplication.run();
    }

    public ApplicationContext run() {
        EditableApplicationContext context = null;
        try {
            context = (EditableApplicationContext) createContext();
            loadBeans(getRootPackage(), context);
        } catch (Throwable ex) {
            LOGGER.error("error starting application: " + ex.getMessage());
        }

        return context;
    }

    /**
     * Load beans annotated with {@code @Component} to the application context.
     * by scanning all classes in the package tree from the root package and processing annotations in all classes
     *
     * @param rootPackage the root package
     */
    private void loadBeans(String rootPackage, ApplicationContext context) {
        Class<?>[] classesInPackage = ReflectionUtils.findClassesInPackage(rootPackage);
        ComponentAnnotationProcessor cap = new ComponentAnnotationProcessor(context);

        for (Class<?> clazz : classesInPackage) {
            cap.processAnnotation(clazz);
        }
    }

    private String getRootPackage() {
        return this.rootPackage;
    }

    private void setRootPackage() {
        this.rootPackage = this.mainClass.getPackage().getName();
    }

    private ApplicationContext createContext() {
        ApplicationContext context;
        try {
            Class<?> contextClass = ClassUtils.getRootClassLoader().loadClass(deduceApplicationContextClass());
            context = BeanUtils.instantiateBean(contextClass, ApplicationContext.class);
            refreshContext(context);
//            prepareContext(context, environment, listeners, applicationArguments, printedBanner);
//            refreshContext(context);
//            afterRefresh(context, applicationArguments);
            return context;
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("unable to create default application context " +
                    "most likely because the context class is not found");
        }
    }

    private void refreshContext(ApplicationContext context) {
        context.refresh();
    }

    private String deduceApplicationContextClass() {
        return DEFAULT_APPLICATION_CONTEXT_CLASS;
    }

    private boolean isMainThread(Thread currentThread) {
        return currentThread.getName().equals("main") && currentThread.getThreadGroup().getName().equals("main");
    }
}
