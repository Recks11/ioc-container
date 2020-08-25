package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.BeanFactory;

public interface ApplicationContext extends BeanFactory {
    String getName();
}
