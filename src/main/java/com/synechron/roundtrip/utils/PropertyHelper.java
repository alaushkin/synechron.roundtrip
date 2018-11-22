package com.synechron.roundtrip.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.synechron.roundtrip.exception.SynechronPropertyException;
import org.apache.commons.lang3.StringUtils;

public class PropertyHelper {

    public static void loadProperties(String fileName) throws SynechronPropertyException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(fileName)) {
            props.load(resourceStream);
            System.getProperties().putAll(props);
        } catch (IOException e) {
            throw new SynechronPropertyException("Cannot load property file " + fileName);
        }
    }

    public static Integer getIntProperty(String name) throws SynechronPropertyException {
        String prop = System.getProperty(name);
        if (prop == null || !StringUtils.isNumeric(prop))
            throw new SynechronPropertyException("Property " + name + "is invalid");
        return Integer.valueOf(prop);
    }

}
