package com.rexijie.ioc.io;

import java.io.File;
import java.net.URI;
import java.net.URL;

public interface Resource {
    URL getUrl();
    URI getUri();
    File getFile();
    String getExtension();
    boolean isFile();
    boolean isDirectory();
}
