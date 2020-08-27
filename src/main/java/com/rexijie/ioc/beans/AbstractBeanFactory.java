package com.rexijie.ioc.beans;

import com.rexijie.ioc.errors.MultipleBeansOfTypeException;
import com.rexijie.ioc.errors.NoSuchBeanException;

import java.util.Optional;
import java.util.Set;

/**
 * This class is the skeleton for a bean store
 * it contains the bean cache which stores instances of beans
 * and default methods to add and remove beans
 */
public abstract class AbstractBeanFactory extends DefaultBeanStore implements BeanFactory {
    private final BeanCreator bC = new BeanCreator(this);

    /**
     * Creates a bean store with itself as a bean
     */
    public AbstractBeanFactory() {
        addBean(this);
    }

    public AbstractBeanFactory(BeanStore instance) {
        this.addBean(instance);
    }

    public <T> void addBean(T beanInstance) {
        registerBean(beanInstance.getClass().getName(), beanInstance);
    }

    public <T> void addBean(Class<T> clazz) {
        addBean(null, clazz);
    }

    public <T> void addBean(String key, Class<T> clazz) {
//        bC.createBean(key, clazz);
        Object bean = createBean(key, clazz);
        registerBean(key, bean);
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
                    .filter(key -> getBeanCache().get(key).isPrimary())
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

        return getBeanCache().get(name).getBean();
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(getBean(name));
    }

    abstract protected <T> Object createBean(String name, Class<T> clazz);
}
