package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.beans.store.BeanStore;
import com.rexijie.ioc.environment.Environment;

public interface ApplicationContext extends BeanFactory, BeanStore {
    Environment getEnvironment();
    String getName();
    void refresh();
}
