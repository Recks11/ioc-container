package com.rexijie.ioc.environment;

public interface EnvironmentVariableStore {
    public String getValue(String key);
    void setValue(String key, String value);
}
