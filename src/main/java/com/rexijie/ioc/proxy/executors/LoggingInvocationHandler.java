package com.rexijie.ioc.proxy.executors;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LoggingInvocationHandler<T> implements ContextAwareInvocationHandler {
    private static final Logger LOG = Logger.getLogger(LoggingInvocationHandler.class.getName());
    private final Map<String, Method> methodMap = new ConcurrentHashMap<>();
    private final T target;

    public LoggingInvocationHandler(T target) {
        this.target = target;
        for (Method method: target.getClass().getDeclaredMethods()) {
            methodMap.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforeInvoke(proxy, method, args);
        Object invoke = methodMap.get(method.getName()).invoke(target, args);
        afterInvoke(proxy, method, args);
        return invoke;
    }

    @Override
    public void beforeInvoke(Object proxy, Method method, Object[] args) {
        // do fun stuff here
        LOG.info("Executing method: "+method.getName());
    }

    @Override
    public void afterInvoke(Object proxy, Method method, Object[] args) {
        // more fun stuff here
        LOG.info("DONE Executing method");
    }
}
