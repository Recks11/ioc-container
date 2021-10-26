package com.rexijie.ioc.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class PropertiesFileProcessor implements ResourceProcessor<String> {
    private final Consumer<String> resourceLineConsumer;

    public PropertiesFileProcessor(Consumer<String> callback) {
        this.resourceLineConsumer = callback;
    }

    @Override
    public boolean canProcessResource(Resource resource) {
        return resource.getExtension() != null &&
                resource.getExtension().equals(".properties");
    }

    @Override
    public void process(Resource resource) {
        process(resource, resourceLineConsumer);
    }

    @Override
    public void process(Resource resource, Consumer<String> callback) {
        try {
            BufferedReader BR = new BufferedReader(new InputStreamReader(resource.getUrl().openStream()));
            String line;
            while ((line = BR.readLine()) != null) {
                if (canProcessResource(resource))
                callback.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
