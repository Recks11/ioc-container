package com.rexijie.mock;

import com.rexijie.ioc.annotations.Bean;

public class InnerBeanClass {
    String name;

    @Bean
    public Named namedBean() {
        return new NamedClass();
    }

    @Bean(value = "customBean", primary = true)
    public Named namedBean2() {
        return new NamedClass();
    }
}
