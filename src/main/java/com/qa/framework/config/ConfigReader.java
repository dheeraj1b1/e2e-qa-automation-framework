package com.qa.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Simple configuration reader.
 *
 * Behavior & precedence:
 * 1. System properties (e.g. `-Dbrowser=firefox`) take highest precedence.
 * 2. If not provided, values are read from
 * `src/main/resources/config.properties`.
 *
 * Design notes:
 * - The class uses a static initializer to load configuration once per JVM.
 * - This keeps runtime access fast via `getProperty()` while still allowing
 * overrides via system properties and environment variables as needed.
 */
public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();
        String path = "src/main/resources/config.properties";
        try (FileInputStream input = new FileInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties from: " + path, e);
        }
    }

    /**
     * Returns a configuration value for `key`.
     *
     * First checks JVM system properties, then falls back to the loaded properties
     * file.
     */
    public static String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }
}