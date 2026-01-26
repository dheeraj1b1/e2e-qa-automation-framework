package com.qa.tests.base;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setupSuite() {
        // Load configuration once before any test runs
        ConfigReader.initConfig();
    }

    @BeforeMethod
    public void setup() {
        // Initialize the browser before every @Test method
        DriverManager.initDriver();
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser after every @Test method
        DriverManager.quitDriver();
    }
}