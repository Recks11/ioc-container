package com.rexijie.ioc.beans;

import com.rexijie.ioc.util.ClassUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanStore implements BeanStore {

    private final Map<String, BeanWrapper<?>> beanCache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> beanTypeMap = new ConcurrentHashMap<>();

    public DefaultBeanStore() {
    }

    @Override
    public boolean containsBean(String name) {
        return beanCache.containsKey(name) || beanTypeMap.containsKey(name);
    }

    @Override
    public <T> boolean containsBean(T beanInstance) {
        return containsBean(beanInstance.getClass());
    }

    @Override
    public <T> boolean containsBean(Class<T> beanClass) {
        return containsBean(beanClass.getName());
    }

    Set<String> getBeanNamesOfType(Class<?> clazz) {
        return beanTypeMap.get(clazz.getName());
    }

    public <T> void registerBean(String key, T bean) {
        synchronized (this.beanCache) {
            if (containsBean(key)) return;

            BeanWrapper<T> beanWrapper = new BeanWrapper<>(bean);
            if (key != null && !key.isEmpty()) beanWrapper.setName(key);

            beanCache.put(beanWrapper.getName(), beanWrapper);
            addInterfacesToTypeMap(bean.getClass(), beanWrapper.getName());
        }
    }

    public void removeBean(String key) {
        beanCache.remove(key);
    }

    public <T> void removeBean(Class<T> beanClass) {
        String name = beanClass.getName();
        removeBean(name);
    }

    public Map<String, Set<String>> getBeanTypeMap() {
        return beanTypeMap;
    }

    private <T> void addInterfacesToTypeMap(Class<?> clazz, String beanName) {
        Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(clazz);
        for (Class<?> ifc : allInterfaces) {
            addBeanTypeMapping(ifc, beanName);
        }
    }

    private void addBeanTypeMapping(Class<?> typeName, String beanName) {
        String key = typeName.getName();
        synchronized (this.beanTypeMap) {
            if (!containsBean(key)) beanTypeMap.put(key, new HashSet<>());
            beanTypeMap.get(key).add(beanName);
        }
    }

    protected Map<String, BeanWrapper<?>> getBeanCache() {
        return beanCache;
    }
}
