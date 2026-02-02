package com.qa.framework.utils;

import com.qa.framework.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ScreenshotUtil {

    // Captures screenshot and returns it as a Base64 string (embeds directly in
    // HTML)
    public static String getScreenshot() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }
}