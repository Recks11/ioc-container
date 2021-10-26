package com.rexijie.ioc;

import com.rexijie.ioc.annotations.processor.ComponentAnnotationProcessor;
import com.rexijie.ioc.context.ApplicationContext;
import com.rexijie.ioc.context.ConfigurableApplicationContext;
import com.rexijie.ioc.environment.ApplicationEnvironment;
import com.rexijie.ioc.environment.Environment;
import com.rexijie.ioc.errors.NoSuchBeanException;
import com.rexijie.ioc.events.EventListener;
import com.rexijie.ioc.util.BeanUtils;
import com.rexijie.ioc.util.ClassUtils;
import com.rexijie.ioc.util.ReflectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class IOC {
    private static final Logger LOGGER = Logger.getLogger(IOC.class);
    private static final String DEFAULT_APPLICATION_CONTEXT_CLASS = "com.rexijie.ioc.context.DefaultApplicationContext";
    private String rootPackage;
    private final Class<?> mainClass;
    private final String[] appArgs;

    private IOC(Class<?> main, String... args) {
        this.mainClass = getMainClass(main);
        this.appArgs = args;
        setRootPackage();
    }

    private Class<?> getMainClass(Class<?> mainClass) {
        if (mainClass != null) return mainClass;
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
        // return passed class if no main method
        return mainClass;
    }

    public static void run(Class<?> application, String... args) {
        IOC iocApplication = new IOC(application, args);
        iocApplication.run();
    }

    public ApplicationContext run() {
        ConfigurableApplicationContext context;
        try {
            context = (ConfigurableApplicationContext) createContext();
            loadBeans(getRootPackage(), context);
        } catch (Throwable ex) {
            LOGGER.error("error starting application: " + ex.getMessage());
            throw new NoSuchBeanException(ex.getMessage(), ex);
        }

        return context;
    }

    private void prepareEnvironment(ApplicationContext context,
                                    Environment environment) {
        LOGGER.info("loading application environment");
        if (environment == null) context.addBean(Environment.ENV_BEAN_NAME, new ApplicationEnvironment());
        else {
            context.removeBean(Environment.ENV_BEAN_NAME);
            context.addBean(environment);
        }
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
        ConfigurableApplicationContext context;
        try {
            LOGGER.debug("Creating Application Context");
            Class<?> contextClass = ClassUtils.getRootClassLoader().loadClass(deduceApplicationContextClass());
            context = (ConfigurableApplicationContext) BeanUtils.instantiateBean(contextClass, ApplicationContext.class);
            prepareEnvironment(context, null);
            refreshContext(context);
//            prepareContext(context, context.getBean(Environment.ENV_BEAN_NAME), eventListeners, appArgs);
            refreshContext(context);
            afterRefresh(context, appArgs);
            return context;
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("unable to create default application context " +
                    "most likely because the context class is not found");
        }
    }

    private void prepareContext(ConfigurableApplicationContext applicationContext, Environment environment,
                                List<EventListener<?>> eventListeners, String[] appArgs) {

    }

    private void refreshContext(ApplicationContext context) {
        context.refresh();
    }

    private void afterRefresh(ConfigurableApplicationContext applicationContext, String[] appArgs) {

    }

    private String deduceApplicationContextClass() {
        return DEFAULT_APPLICATION_CONTEXT_CLASS;
    }

    private boolean isMainThread(Thread currentThread) {
        return currentThread.getName().equals("main") && currentThread.getThreadGroup().getName().equals("main");
    }
}
