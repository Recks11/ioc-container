package com.rexijie.ioc.proxy.executors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface ContextAwareInvocationHandler extends InvocationHandler {
    void beforeInvoke(Object proxy, Method method, Object[] args);
    void afterInvoke(Object proxy, Method method, Object[] args);
}
