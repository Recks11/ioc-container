package com.rexijie.ioc.util;


import org.apache.log4j.Logger;

public class ObjectUtil {
    static final Logger logger = Logger.getLogger(ObjectUtil.class);

    private ObjectUtil() {}

    public static boolean checkNonNull(Object object) {
        return object != null;
    }
}
