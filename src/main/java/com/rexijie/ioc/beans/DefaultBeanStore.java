package com.rexijie.ioc.beans;

import com.rexijie.ioc.annotations.processor.BeanAnnotationProcessor;
import com.rexijie.ioc.errors.MultipleBeansOfTypeException;
import com.rexijie.ioc.errors.NoSuchBeanException;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanStore implements BeanStore {

    private final Map<String, BeanWrapper<?>> beanCache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> beanTypeMap = new ConcurrentHashMap<>();
    private final BeanAnnotationProcessor beanAnnotationProcessor;

    public DefaultBeanStore() {
        this.beanAnnotationProcessor = new BeanAnnotationProcessor(this);
    }

    @Override
    public Map<String, BeanWrapper<?>> getBeanCache() {
        return beanCache;
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

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (clazz.isInterface()) {
            Set<String> beans = getBeanNamesOfType(clazz);
            if (beans == null || beans.size() < 1) throw new NoSuchBeanException(clazz);
            // if there is exactly one bean of this interface type
            if (beans.size() == 1) {
                String beanName = beans.iterator().next();
                return getBean(beanName, clazz);
            }

            Optional<String> primaryBeanOptional = beans
                    .stream()
                    .filter(key -> beanCache.get(key).isPrimary())
                    .findFirst();

            if (primaryBeanOptional.isPresent()) {
                return getBean(primaryBeanOptional.get(), clazz);
            } else {
                throw new MultipleBeansOfTypeException(clazz);
            }
        }

        return getBean(clazz.getName(), clazz);
    }

    @Override
    public Object getBean(String name) {
        if (!containsBean(name)) throw new NoSuchBeanException("No bean named " + name);

        return beanCache.get(name).getBean();
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(getBean(name));
    }

    Set<String> getBeanNamesOfType(Class<?> clazz) {
        return beanTypeMap.get(clazz.getName());
    }

    @Override
    public <T> void addBean(String key, T bean) {
        if (containsBean(key)) return;

        BeanWrapper<T> beanWrapper = new BeanWrapper<>(bean);
        if (key != null && !key.isEmpty()) beanWrapper.setName(key);

        beanCache.put(beanWrapper.getName(), beanWrapper);
        populateTypeMap(bean.getClass(), beanWrapper.getName());

        beanAnnotationProcessor.processAnnotation(beanWrapper);

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

    public BeanAnnotationProcessor getBeanAnnotationProcessor() {
        return beanAnnotationProcessor;
    }

    private <T> void populateTypeMap(Class<?> clazz, String beanName) {
        for (Class<?> interfaceClass : clazz.getInterfaces()) {
            addBeanTypeMapping(interfaceClass, beanName);
            populateTypeMap(interfaceClass, beanName);
        }

        if (clazz.getSuperclass() == null) return;
        if (!clazz.getSuperclass().equals(Object.class)) {
            populateTypeMap(clazz.getSuperclass(), beanName);
        }
    }

    private void addBeanTypeMapping(Class<?> typeName, String name) {
        String key = typeName.getName();
        if (beanTypeMap.containsKey(typeName.getName())) {
            Set<String> beans = beanTypeMap.get(key);
            beans.add(name);
        } else {
            Set<String> beanNames = new HashSet<>();
            beanTypeMap.put(key, beanNames);
            addBeanTypeMapping(typeName, name);
        }
    }
}
