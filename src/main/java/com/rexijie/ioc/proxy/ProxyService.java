package com.rexijie.ioc.proxy;

import com.rexijie.ioc.proxy.executors.LoggingInvocationHandler;
import com.rexijie.ioc.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyService {

    public static <I, T> I createProxyFor(Class<I> clazz, T target) {
        return clazz.cast(createProxyFor(clazz, new LoggingInvocationHandler<>(target)));
    }

    public static <I> Object createProxyFor(Class<I> clazz, InvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(ClassUtils.getRootClassLoader(),
                getInterfacesForClass(clazz),
                invocationHandler);
    }

    private static Class<?>[] getInterfacesForClass(Class<?> clazz) {
        return ClassUtils.getAllInterfaces(clazz, false);
    }
}
