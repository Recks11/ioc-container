package com.rexijie;

import com.rexijie.ioc.IOC;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IOCTest {

    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> IOC.run(IOCTest.class));
    }

}
