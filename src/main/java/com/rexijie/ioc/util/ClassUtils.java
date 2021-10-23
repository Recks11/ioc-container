package com.rexijie.ioc.util;

import java.util.*;

import static com.rexijie.ioc.util.ObjectUtil.returnIfNull;

interface Executable<T> {
    T apply();
}

public class ClassUtils {
    private static final String JAVA_PACKAGE_NAME = "java.";
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[]{};
    private static final Map<Class<?>, Class<?>> jdkWrapperClassMap = new HashMap<>(32);
    private static final Map<Class<?>, Class<?>> jdkPrimitiveTypeMap = new HashMap<>(32);
    private static final Map<Class<?>, Executable<?>> primitiveDefaults = new HashMap<>(32);

    static {
        jdkWrapperClassMap.put(Long.class, long.class);
        jdkWrapperClassMap.put(Double.class, double.class);
        jdkWrapperClassMap.put(Float.class, float.class);
        jdkWrapperClassMap.put(Integer.class, int.class);
        jdkWrapperClassMap.put(Byte.class, byte.class);
        jdkWrapperClassMap.put(Short.class, short.class);
        jdkWrapperClassMap.put(Character.class, char.class);
        jdkWrapperClassMap.put(Void.class, void.class);
        jdkWrapperClassMap.put(Boolean.class, boolean.class);
        jdkWrapperClassMap.put(String.class, String.class);

        // spring does this too and it is useful
        for (Map.Entry<Class<?>, Class<?>> entry : jdkWrapperClassMap.entrySet()) {
            jdkPrimitiveTypeMap.put(entry.getValue(), entry.getKey());
        }

        primitiveDefaults.put(String.class, () -> "[DEFAULT]");
        primitiveDefaults.put(long.class, () -> 0L);
        primitiveDefaults.put(double.class, () -> 0D);
        primitiveDefaults.put(float.class, () -> 0F);
        primitiveDefaults.put(int.class, () -> 0);
        primitiveDefaults.put(byte.class, () -> 0);
        primitiveDefaults.put(short.class, () -> 0);
        primitiveDefaults.put(char.class, () -> 'c');
        primitiveDefaults.put(boolean.class, () -> false);
    }

    public static Object getDefaultForPrimitiveType(Class<?> clazz) {
        Class<?> key = clazz;
        if (jdkWrapperClassMap.containsKey(clazz)) {
            key = jdkWrapperClassMap.get(clazz);
        }
        return primitiveDefaults.get(key).apply();
    }

    public static boolean isInternalType(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        return jdkWrapperClassMap.containsKey(clazz) || jdkPrimitiveTypeMap.containsKey(clazz) || (packageName.startsWith("java") | packageName.startsWith("jdk"));
    }

    public static Class<?>[] getAllInterfaces(Class<?> clazz, boolean includeJavaInterfaces) {
        Set<Class<?>> interfaces = new HashSet<>();

        for (Class<?> ifc : getParentInterfaceForClass(clazz, includeJavaInterfaces)) {
            if (ifc.getName().startsWith(JAVA_PACKAGE_NAME) && !includeJavaInterfaces) continue;
            interfaces.add(ifc);
            interfaces.addAll(Arrays.asList(getParentInterfaceForInterface(ifc, includeJavaInterfaces)));
        }

        return toClassArray(interfaces);
    }

    private static Class<?>[] getParentInterfaceForInterface(Class<?> clazz, boolean includeJavaInterfaces) {
        Set<Class<?>> interfaces = new HashSet<>();

        for (Class<?> cla : clazz.getInterfaces()) {
            if (cla.getName().startsWith(JAVA_PACKAGE_NAME) && !includeJavaInterfaces) continue;
            interfaces.add(cla);
            if (cla.getInterfaces().length > 0) {
                interfaces.addAll(
                        Arrays.asList(getParentInterfaceForInterface(cla, includeJavaInterfaces)));
            }
        }

        return toClassArray(interfaces);
    }

    private static Class<?>[] getParentInterfaceForClass(Class<?> clazz, boolean includeJavaInterfaces) {
        if (clazz.getName().startsWith(JAVA_PACKAGE_NAME) && !includeJavaInterfaces) return EMPTY_CLASS_ARRAY;
        Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(clazz.getInterfaces()));

        Class<?> localClass = clazz;
        while (localClass.getSuperclass() != null && localClass.getSuperclass() != Object.class) {

            localClass = localClass.getSuperclass();

            interfaces.addAll(Arrays.asList(localClass.getInterfaces()));
        }

        return toClassArray(interfaces);
    }


    public static ClassLoader getRootClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
            // ignore cannot access
        }

        // if classLoader is null then couldn't access
        classLoader = returnIfNull(classLoader,
                ClassUtils.class::getClassLoader);

        // if classloader is still null then use system
        classLoader = returnIfNull(classLoader,
                ClassLoader::getSystemClassLoader);

        return classLoader;
    }

    public static Class<?>[] toClassArray(Collection<? extends Class<?>> classes) {
        return classes.toArray(EMPTY_CLASS_ARRAY);
    }
}
