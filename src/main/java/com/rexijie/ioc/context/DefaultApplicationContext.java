package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.AbstractBeanFactory;
import com.rexijie.ioc.beans.BeanFactory;
import com.rexijie.ioc.container.DefaultBeanFactory;

public class DefaultApplicationContext extends AbstractBeanFactory implements ApplicationContext {
    private String name;
    private BeanFactory beanFactory = new DefaultBeanFactory();

    public DefaultApplicationContext() {
        this.name = this.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
