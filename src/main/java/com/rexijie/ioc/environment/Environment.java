package com.rexijie.ioc.environment;

public interface Environment {
    String get(String key);
    void set(String key, String value);
    void clear();
}
