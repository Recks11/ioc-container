package com.rexijie.ioc.context;

import com.rexijie.ioc.beans.factory.BeanFactory;
import com.rexijie.ioc.environment.Environment;
import com.rexijie.ioc.environment.EnvironmentVariableStore;

public interface ApplicationContext extends BeanFactory {
    Environment getEnvironment();
    String getName();
    void refresh();
}
