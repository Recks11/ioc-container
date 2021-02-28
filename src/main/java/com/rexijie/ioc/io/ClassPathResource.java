package com.rexijie.ioc.io;

import com.rexijie.ioc.util.ClassUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ClassPathResource implements Resource {
    private URI uri;
    private URL url;
    private String name;
    private String extension;
    private ClassLoader classLoader;
    private Class<?> clazz;

    public ClassPathResource(URI uri) {
        this.uri = uri;
        classLoader = ClassUtils.getRootClassLoader();
        parseName();
    }

    public ClassPathResource(URI uri, ClassLoader classLoader) {
        this.uri = uri;
        parseName();
    }

    public ClassPathResource(URI uri, Class<?> theClass) {
        this.uri = uri;
        this.clazz = theClass;
        parseName();
    }

    public ClassPathResource(URL file) {
        this.url = file;
        classLoader = ClassUtils.getRootClassLoader();
        parseName();
    }

    public ClassPathResource(URL file, ClassLoader classLoader) {
        this.url = file;
        parseName();
    }

    private void parseName() {
        name = getFile().getName();
        String[] split = getFile().getName().split("\\.");
        String ext = split[split.length - 1];
        if (!ext.equals(this.name))
            extension = "."+ext;
    }

    @Override
    public URI getUri() {
        return this.uri;
    }

    @Override
    public URL getUrl() {
        try {
            if (this.uri != null)
                return uri.toURL();
        } catch (MalformedURLException exception) {
            System.out.println("MALFORMED URL");
        }
        return this.url;
    }

    public String getPath() {
        if (this.uri != null)
            return uri.getPath();
        return this.url.getPath();
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public File getFile() {
        return new File(getPath());
    }

    @Override
    public boolean isFile() {
        return getFile().isFile();
    }

    @Override
    public boolean isDirectory() {
        return getFile().isDirectory();
    }

    public ClassLoader getClassLoader() {
        if (clazz != null) return clazz.getClassLoader();
        return classLoader;
    }
}
