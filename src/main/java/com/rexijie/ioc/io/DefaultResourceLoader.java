package com.rexijie.ioc.io;

import com.rexijie.ioc.util.ClassUtils;

import java.net.URL;
import java.util.List;

import static com.rexijie.ioc.util.ResourceUtils.CLASSPATH_URL_PREFIX;

public class DefaultResourceLoader implements ResourceLoader {
    private final ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = ClassUtils.getRootClassLoader();
    }

    @Override
    public List<Resource> loadResources(String path, String extension) {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Resource loadResource(String path) {
        if (path.startsWith(CLASSPATH_URL_PREFIX)) {
            path = path.substring(CLASSPATH_URL_PREFIX.length());
            return getLocalResource(path);
        }
        return null;
    }

    public ClassPathResource getLocalResource(String resource) {
        URL resourceUrl = getClassLoader().getResource(resource);
        return new ClassPathResource(resourceUrl);
    }

}
