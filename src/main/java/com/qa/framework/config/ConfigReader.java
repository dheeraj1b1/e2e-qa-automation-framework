package com.qa.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            String path = "src/main/resources/config.properties";
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    public static String getProperty(String key) {
        // 1. Check if the value is passed via Command Line (-Dbrowser=firefox)
        String value = System.getProperty(key);

        // 2. If not, take it from the file
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }
}