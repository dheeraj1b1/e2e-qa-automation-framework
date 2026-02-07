package com.qa.tests.base;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.driver.DriverManager;
import com.qa.framework.utils.LoggerUtil; // Import Logger
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected String username;
    protected String password;

    @BeforeMethod
    public void setUp() {
        LoggerUtil.logInfo("Initializing Driver...");
        DriverManager.initDriver();

        String url = ConfigReader.getProperty("baseUrl");
        if (url != null) {
            LoggerUtil.logInfo("Navigating to URL: " + url);
            getDriver().get(url);
        } else {
            getDriver().get("https://opensource-demo.orangehrmlive.com/");
        }

        username = ConfigReader.getProperty("username");
        password = ConfigReader.getProperty("password");
    }

    @AfterMethod
    public void tearDown() {
        LoggerUtil.logInfo("Closing Driver...");
        DriverManager.quitDriver();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}