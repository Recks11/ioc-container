package com.rexijie.ioc.beans.store;

import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.util.ClassUtils;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanStore implements BeanStore {
    private static final Logger LOG = Logger.getLogger(DefaultBeanStore.class);
    private final Map<String, BeanWrapper<?>> beanCache = new ConcurrentHashMap<>();
    private final Map<String, BeanDefinitionSet<String>> beanTypeMap = new ConcurrentHashMap<>();

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

    public Set<String> getBeanNamesOfType(Class<?> clazz) {
        return beanTypeMap.get(clazz.getName());
    }

    public synchronized <T> void addBean(BeanWrapper<?> beanWrapper) {
        addInterfacesToTypeMap(beanWrapper.getClazz(), beanWrapper);
        beanCache.put(beanWrapper.getName(), beanWrapper);
    }

    public <T> void registerBean(String key, T bean) {
        LOG.debug("Registering Bean " + key);
        synchronized (this.beanCache) {
            if (containsBean(key)) return;

            if (bean instanceof BeanWrapper) {
                addBean((BeanWrapper<?>) bean);
                return;
            }
            BeanWrapper<T> beanWrapper = new BeanWrapper<>(bean);
            if (key != null && !key.isEmpty()) beanWrapper.setName(key);
            addBean(beanWrapper);
        }
    }

    public void removeBean(String key) {
        beanCache.remove(key);
    }

    public <T> void removeBean(Class<T> beanClass) {
        String name = beanClass.getName();
        removeBean(name);
    }

    public Map<String, BeanDefinitionSet<String>> getBeanTypeMap() {
        return beanTypeMap;
    }

    public BeanDefinitionSet<String> getBeanTypeDetails(String key) {
        return getBeanTypeMap().get(key);
    }

    private <T> void addInterfacesToTypeMap(Class<?> clazz, BeanWrapper<?> beanWrapper) {
        Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(clazz);
        for (Class<?> ifc : allInterfaces) {
            addBeanTypeMapping(ifc, beanWrapper);
        }
    }

    @Override
    public BeanWrapper<?> getRawBean(String key) {
        return beanCache.get(key);
    }

    private void addBeanTypeMapping(Class<?> type, BeanWrapper<?> beanWrapper) {
        String key = type.getName();
        synchronized (beanTypeMap) {
            if (!containsBean(key)) beanTypeMap.put(key, new BeanDefinitionSet<>());
            getBeanTypeDetails(key).add(beanWrapper.getName(), beanWrapper);
        }
    }

    public Map<String, BeanWrapper<?>> getBeanCache() {
        return beanCache;
    }
}
