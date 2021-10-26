package com.rexijie.ioc.environment;

public interface EnvironmentVariableStore {
    String getValue(String key);
    void setValue(String key, String value);
    void remove(String key);
    void clear();
}
