package com.rexijie.ioc.environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEnvironmentStore implements EnvironmentVariableStore {
    private final Map<String, String> environmentVars = new ConcurrentHashMap<>();

    public InMemoryEnvironmentStore() {
    }

    public String getValue(String key) {
        return environmentVars.get(key);
    }

    public void setValue(String key, String value) {
        synchronized (environmentVars) {
            environmentVars.put(key, value);
        }
    }

    @Override
    public void remove(String key) {
        environmentVars.remove(key);
    }

    @Override
    public void clear() {
        environmentVars.clear();
    }
}
