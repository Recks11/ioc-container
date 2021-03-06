package com.rexijie.ioc.beans.factory;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.beans.store.DefaultBeanStore;
import com.rexijie.ioc.errors.MultipleBeansOfTypeException;
import com.rexijie.ioc.errors.NoSuchBeanException;

import java.util.Optional;
import java.util.Set;

/**
 * This class is the skeleton for a bean store
 * it contains the bean cache which stores instances of beans
 * and default methods to add and remove beans
 */
public abstract class AbstractBeanFactory implements BeanFactory {
    private BeanStore beanStore = new DefaultBeanStore();

    /**
     * Creates a bean store with itself as a bean
     */

    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(BeanStore instance) {
        this.addBean(instance);
    }

    public BeanStore getBeanStore() {
        return beanStore;
    }

    public void setBeanStore(DefaultBeanStore beanStore) {
        this.beanStore = beanStore;
    }

    public <T> void addBean(T beanInstance) {
        registerBean(beanInstance.getClass().getName(), beanInstance);
    }

    public <T> void addBean(Class<T> clazz) {
        addBean(null, clazz);
    }

    public <T> void addBean(String key, Class<T> clazz) {
        createBean(key, clazz);
    }

    @Override
    public <T> void addBean(String key, T instance) {
        registerBean(key, instance);
    }

    public BeanWrapper<?> getBeanWrapper(String name) {
        return beanStore.getRawBean(name);
    }

    @Override
    public Object getBean(String name) {
        if (!containsBean(name)) throw new NoSuchBeanException("No bean named " + name);

        return getBeanWrapper(name).getBean();
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(getBean(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (clazz.isInterface()) {
            Set<String> beans = beanStore.getBeanNamesOfType(clazz);
            if (beans == null || beans.size() < 1) throw new NoSuchBeanException(clazz);
            // if there is exactly one bean of this interface type
            if (beans.size() == 1) {
                String beanName = beans.iterator().next();
                return getBean(beanName, clazz);
            }

            Optional<String> primaryBeanOptional = beans
                    .stream()
                    .filter(key -> getBeanWrapper(key).isPrimary())
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
    public Set<String> getBeanNamesOfType(Class<?> clazz) {
        return beanStore.getBeanNamesOfType(clazz);
    }


    @Override
    public boolean containsBean(String name) {
        return beanStore.containsBean(name);
    }

    @Override
    public <T> boolean containsBean(T beanInstance) {
        return beanStore.containsBean(beanInstance);
    }

    @Override
    public <T> boolean containsBean(Class<T> beanClass) {
        return beanStore.containsBean(beanClass);
    }

    @Override
    public <T> void registerBean(String key, T bean) {
        beanStore.registerBean(key, bean);
        getAnnotationProcessor()
                .processAnnotation(getBeanWrapper(key));
    }

    @Override
    public void removeBean(String key) {
        beanStore.removeBean(key);
    }

    public abstract AnnotationProcessor getAnnotationProcessor();

    abstract protected <T> void createBean(String name, Class<T> clazz);
}
