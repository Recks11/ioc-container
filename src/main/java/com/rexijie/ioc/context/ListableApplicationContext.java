package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.factory.BeanFactory;

public interface ListableApplicationContext extends ApplicationContext {
    BeanFactory getFactory();
}
