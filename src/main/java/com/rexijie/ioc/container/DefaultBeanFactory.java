package com.rexijie.ioc.container;

import com.rexijie.ioc.beans.AbstractBeanFactory;
import com.rexijie.ioc.beans.BeanStore;

public class DefaultBeanFactory extends AbstractBeanFactory {

    public DefaultBeanFactory() {
        super();
    }

    /**
     * create an instance of a bean factory with a bean added to the beancache
     */
    public DefaultBeanFactory(BeanStore instance) {
        super(instance);
    }
}
