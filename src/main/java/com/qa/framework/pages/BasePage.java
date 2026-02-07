package com.qa.framework.pages;

import com.qa.framework.driver.DriverManager;
import com.qa.framework.utils.LoggerUtil; // Import Logger
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void click(By locator) {
        LoggerUtil.logInfo("Clicking element: " + locator); // Log action
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        LoggerUtil.logInfo("Typing '" + text + "' into: " + locator); // Log action
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        LoggerUtil.logInfo("Retrieved text: '" + text + "' from: " + locator); // Log action
        return text;
    }
}