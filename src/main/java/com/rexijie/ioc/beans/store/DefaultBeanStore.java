package com.rexijie.ioc.beans.store;

import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.errors.MultipleBeansOfTypeException;
import com.rexijie.ioc.errors.NoSuchBeanException;
import com.rexijie.ioc.util.ClassUtils;
import org.apache.log4j.Logger;

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

    public synchronized void addBean(BeanWrapper<?> beanWrapper) {
        addInterfacesToTypeMap(beanWrapper.getClazz(), beanWrapper);
        addBeanTypeMapping(beanWrapper.getClazz(), beanWrapper);
        beanCache.put(beanWrapper.getName(), beanWrapper);
    }

    public <T> void registerBean(String key, T bean) {
        LOG.debug("Registering Bean " + key);
        registerBeanInstance(key, bean);
    }

    @Override
    public <T> void addBean(T beanInstance) {
        if (beanInstance instanceof Class) {
            Class<?> bClass = (Class<?>) beanInstance;
            if (bClass.isInterface()) return; // TODO thorw error
            addBean(bClass.getName(), bClass);
            return;
        }
        registerBeanInstance(beanInstance.getClass().getName(), beanInstance);
        LOG.info("Added Bean: " + beanInstance.getClass().getName());
    }

    @Override
    public <T> void addBean(String key, Class<T> clazz) {
        registerBeanClass(key, clazz);
    }

    @Override
    public <T> void addBean(String key, T instance) {
        registerBeanInstance(key, instance);
    }

    public void removeBean(String key) {
        BeanWrapper<?> remove = beanCache.remove(key);
        if (remove != null && beanTypeMap.containsKey(remove.getClazz().getName())) {
            BeanDefinitionSet<String> beanNames;

            String className = remove.getClazz().getName();
            beanNames = beanTypeMap.get(className);
            beanNames.remove(key);
            if (beanNames.size() == 0) beanTypeMap.remove(className);

            for (Class<?> cls : ClassUtils.getAllInterfaces(remove.getClazz(), false)) {
                beanNames = beanTypeMap.get(cls.getName());
                beanNames.remove(key);
                if (beanNames.size() == 0) beanTypeMap.remove(cls.getName());
            }
        }
    }

    public <T> void removeBean(Class<T> beanClass) {
        String name = resolveNameForBeanClass(beanClass);
        if (beanTypeMap.containsKey(name)) {
            beanTypeMap.get(name)
                    .forEach(beanTypeMap::remove);
            beanTypeMap.remove(name);
        }
    }

    public Map<String, BeanDefinitionSet<String>> getBeanTypeMap() {
        return beanTypeMap;
    }

    public BeanDefinitionSet<String> getBeanTypeDetails(String key) {
        return getBeanTypeMap().get(key);
    }

    private <T> void addInterfacesToTypeMap(Class<?> clazz, BeanWrapper<?> beanWrapper) {
        Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(clazz, false);
        for (Class<?> ifc : allInterfaces) {
            addBeanTypeMapping(ifc, beanWrapper);
        }
    }

    @Override
    public BeanWrapper<?> getRawBean(String key) {
        LOG.debug("retrieving bean " + key);
        if (beanCache.containsKey(key)) return beanCache.get(key);
        if (!beanTypeMap.containsKey(key))
            throw new NoSuchBeanException("no bean named " + key + " exists in the beanstore");

        BeanDefinitionSet<String> strings = beanTypeMap.get(key);
        if (strings.size() == 1) return beanCache.get(strings.iterator().next());
        if (strings.isPrimaryBeanSet()) return beanCache.get(strings.getPrimaryBean().toString());
        throw new MultipleBeansOfTypeException(key);
    }

    private void addBeanTypeMapping(Class<?> type, BeanWrapper<?> beanWrapper) {
        String key = resolveNameForBeanClass(type);
        synchronized (beanTypeMap) {
            if (!containsBean(key)) beanTypeMap.put(key, new BeanDefinitionSet<>());
            getBeanTypeDetails(key).add(beanWrapper.getName(), beanWrapper);
        }
    }

    public Map<String, BeanWrapper<?>> getBeanCache() {
        return beanCache;
    }

    private <T> void registerBeanInstance(String key, T bean) {
        LOG.debug("Registering Bean " + key);
        synchronized (this.beanCache) {
            BeanWrapper<T> beanWrapper = BeanWrapper.forInstance(bean);
            if (key != null && !key.isEmpty()) beanWrapper.setName(key);
            addBean(beanWrapper);
        }
    }

    private <T> void registerBeanClass(String key, Class<T> beanClass) {
        LOG.debug("Registering Bean class" + key);
        synchronized (this.beanCache) {
            BeanWrapper<T> beanWrapper = BeanWrapper.forClass(beanClass);
            if (key != null && !key.isEmpty()) beanWrapper.setName(key);
            addBean(beanWrapper);
        }
    }

    private <T> void registerBeanWrapper(String key, BeanWrapper<T> bean) {
        LOG.debug("Registering Bean wrapper" + key);
        synchronized (this.beanCache) {
            addBean(bean);
        }
    }

    private String resolveNameForBeanClass(Class<?> clazz) {
        return clazz.getName();
    }
}
