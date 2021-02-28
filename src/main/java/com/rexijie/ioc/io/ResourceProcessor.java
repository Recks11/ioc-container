package com.rexijie.ioc.io;

import java.util.function.Consumer;

public interface ResourceProcessor<T> {
    boolean canProcessResource(Resource resource);
    void process(Resource resource);
    void process(Resource resource, Consumer<T> callback);
}
