package com.qa.tests.base;

import com.qa.framework.config.ConfigReader; // Import ConfigReader
import com.qa.framework.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
        // FIX: Navigate to the URL globally for ALL tests
        String url = ConfigReader.getProperty("baseUrl");
        if (url != null) {
            getDriver().get(url);
        } else {
            // Fallback if config is missing (safety check)
            getDriver().get("https://opensource-demo.orangehrmlive.com/");
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}