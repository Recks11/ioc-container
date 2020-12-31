package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.BeanFactory;

public interface ListableApplicationContext extends ApplicationContext {

    BeanFactory getFactory();
}
