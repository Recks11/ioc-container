package com.rexijie.ioc.environment;

public interface Environment {
    String ENV_BEAN_NAME = "app-environment";
    String get(String key);
    void set(String key, String value);
    void clear();
}
