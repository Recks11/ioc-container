package com.rexijie.ioc.beans;

public class BeanKeyGenerator {

    public BeanKeyGenerator() {
    }

    public String generateKey(Class<?> clazz) {
        if (clazz.isInterface()) {
            return clazz.getName();
        }

        if (clazz.getSuperclass().equals(Object.class)) {
            if (clazz.getInterfaces().length > 0) {
                return generateKeyFromInterfacedBean(clazz, clazz);
            }
        } else {
            Class<?> interfaceClass = clazz;

            while (!interfaceClass.getSuperclass().equals(Object.class)) {
                interfaceClass = interfaceClass.getSuperclass();
            }

            if (interfaceClass.getInterfaces().length > 0) {
                return generateKeyFromInterfacedBean(interfaceClass, clazz);
            }
        }
        return clazz.getName();
    }

    public String generateKeyFromInterfacedBean(Class<?> interfaceClass, Class<?> beanClass) {
        Class<?>[] interfaces = interfaceClass.getInterfaces();
        String mainInterfaceName = interfaces[0].getName();
        String className = beanClass.getName();

        return className.concat(" " + mainInterfaceName);
    }
}
