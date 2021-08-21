package com.rexijie.ioc.context;

import com.rexijie.ioc.environment.Environment;

public interface ConfigurableApplicationContext extends ApplicationContext {
    void setEnvironment(Environment environment);
    void close();
}
