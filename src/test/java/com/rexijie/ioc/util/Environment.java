package com.rexijie.ioc.util;

import com.rexijie.ioc.environment.ApplicationEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Environment {

    @Test
    void canSuccessfullyLoadEnvFiles() {
        ApplicationEnvironment env = new ApplicationEnvironment();

        Assertions.assertEquals(env.get("test.string"), "random value as content");
    }
}
