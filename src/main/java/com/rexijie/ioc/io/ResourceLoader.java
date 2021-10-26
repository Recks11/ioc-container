package com.rexijie.ioc.io;

import com.rexijie.ioc.util.ClassUtils;

import java.net.URL;
import java.util.List;

public interface ResourceLoader {
    default URL getBaseURL() {
        return ClassUtils.getRootClassLoader().getResource("./");
    }

    Resource loadResource(String path);

    List<Resource> loadResources(String path, String extension);

    ClassLoader getClassLoader();
}
