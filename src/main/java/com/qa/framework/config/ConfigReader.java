package com.qa.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    public static void initConfig() {
        if (properties == null) {
            try {
                properties = new Properties();
                // Load the config.properties file
                FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Could not load config.properties file: " +
                        e.getMessage());
            }
        }
    }

    public static String get(String key) {
        if (properties == null) {
            initConfig();
        }
        return properties.getProperty(key);
    }
}