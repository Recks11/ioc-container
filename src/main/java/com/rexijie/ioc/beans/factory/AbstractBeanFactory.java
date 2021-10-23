package com.rexijie.ioc.beans.factory;

import com.rexijie.ioc.annotations.AnnotationProcessor;
import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.beans.store.DefaultBeanStore;
import com.rexijie.ioc.errors.MultipleBeansOfTypeException;
import com.rexijie.ioc.errors.NoSuchBeanException;
import com.rexijie.ioc.util.BeanUtils;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.Set;

/**
 * This class is the skeleton for a bean store
 * it contains the bean cache which stores instances of beans
 * and default methods to add and remove beans
 */
public abstract class AbstractBeanFactory implements BeanFactory {
    private static final Logger LOG = Logger.getLogger(AbstractBeanFactory.class);
    private BeanStore beanStore = new DefaultBeanStore();

    /**
     * Creates a bean store with itself as a bean
     */

    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(BeanStore instance) {
        this.beanStore = instance;
//       TODO this.addBean(instance);
    }

    public BeanStore getBeanStore() {
        return beanStore;
    }

    public void setBeanStore(BeanStore beanStore) {
        this.beanStore = beanStore;
    }

    @Override
    public Object getBean(String name) {
        BeanWrapper<?> rawBean = beanStore.getRawBean(name);
        if (rawBean.isInstantiated())
            return rawBean.getBean();
        return BeanUtils.instantiateBean(rawBean.getClazz());
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(getBean(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (clazz.isInterface()) {
            Object o = beanStore.getRawBean(clazz.getName()).getBean();
            return clazz.cast(o);
        }

        return getBean(clazz.getName(), clazz);
    }

    public abstract AnnotationProcessor getAnnotationProcessor();

    abstract protected <T> void createBean(String name, Class<T> clazz);
}
