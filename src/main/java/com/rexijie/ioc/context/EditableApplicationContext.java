package com.rexijie.ioc.context;

import com.rexijie.ioc.environment.Environment;

public interface EditableApplicationContext extends ApplicationContext {
    void setEnvironment(Environment environment);
    void close();
}
