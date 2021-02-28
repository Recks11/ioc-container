package com.rexijie.ioc.io;

import com.rexijie.ioc.util.ClassUtils;
import com.rexijie.ioc.util.ResourceUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rexijie.ioc.util.ResourceUtils.CLASSPATH_URL_PREFIX;

public class EnvironmentConfigResourceLoader implements ResourceLoader {
    private final ClassLoader classLoader;

    public EnvironmentConfigResourceLoader() {
        this.classLoader = ClassUtils.getRootClassLoader();
    }

    @Override
    public Resource loadResource(String path) {
        if (path.startsWith(CLASSPATH_URL_PREFIX)) {
            try {
                URI uri = getBaseURL().toURI();
                File base = new File(uri);
                List<File> files = ResourceUtils.loadFilesWithExtension(base, ".properties");

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Load resources from the classpath with the provided extension
     *
     * @param path      the path to load from
     * @param extension the extension
     * @return a list of resources
     */
    @Override
    public List<Resource> loadResources(String path, String extension) {
        if (path.startsWith(CLASSPATH_URL_PREFIX)) {
            try {
                URI uri = getBaseURL().toURI();
                File base = new File(uri);
                List<File> files = ResourceUtils.loadFilesWithExtension(base, ".properties");
                return files.stream()
                        .map(file -> new ClassPathResource(file.toURI()))
                        .collect(Collectors.toList());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
