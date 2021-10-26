package com.rexijie.ioc.environment;

import com.rexijie.ioc.io.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rexijie.ioc.util.ResourceUtils.classPathRelative;

public class ApplicationEnvironment implements Environment {
    private static final Logger LOG = Logger.getLogger(ApplicationEnvironment.class);
    private final EnvironmentVariableStore envStore;
    private final Object sync = new Object();
    private final List<ResourceProcessor<?>> resourceProcessors = new ArrayList<>();

    public ApplicationEnvironment() {
        LOG.debug("creating application environment");
        this.envStore = new InMemoryEnvironmentStore();
        initDefaultProcessors();
        loadSystemEnvironment();
        loadPropertiesFiles();
    }

    public ApplicationEnvironment(EnvironmentVariableStore envStore) {
        this.envStore = envStore;
        initDefaultProcessors();
        loadSystemEnvironment();
        loadPropertiesFiles();
    }

    private void loadSystemEnvironment() {
        System.getenv()
                .keySet()
                .forEach(key -> envStore.setValue(key, System.getenv().get(key)));
    }

    private void loadPropertiesFiles() {
        LOG.debug("loading properties files");
        ResourceLoader rl = new EnvironmentConfigResourceLoader();

        List<Resource> resources = rl.loadResources(classPathRelative("/"), ".properties");

        resources.forEach(resource ->
                resourceProcessors.forEach(resourceProcessor -> resourceProcessor.process(resource)));
    }

    private void initDefaultProcessors() {
        addResourceProcessor(new PropertiesFileProcessor(line -> {
            String[] split = line.trim().split("=");
            if (split.length == 2) {
                String key = split[0].trim();
                String value = split[1].trim();
                envStore.setValue(key, value);
                if (value.contains(",")) {
                    List<String> values = Arrays.asList(value.split(","));
                    // TODO (Handle list)
                }
            }
        }));
    }

    public void addResourceProcessor(ResourceProcessor<?> resourceProcessor) {
        synchronized (resourceProcessors) {
            resourceProcessors.add(resourceProcessor);
        }
    }

    @Override
    public String get(String key) {
        return envStore.getValue(key);
    }

    @Override
    public void set(String key, String value) {
        synchronized (sync) {
            envStore.setValue(key, value);
        }
    }

    @Override
    public void clear() {
        synchronized (sync) {
            envStore.clear();
        }
    }
}
