package com.qa.framework.pages;

import com.qa.framework.driver.DriverManager;
import com.qa.framework.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * BasePage encapsulates common web page interactions and utilities used by
 * concrete page objects.
 *
 * OOP concepts applied:
 * - Encapsulation: wrapping element lookups and actions (click/type/getText) to
 * keep interaction details inside this class.
 * - Inheritance: concrete page classes (e.g., `LoginPage`, `DashboardPage`)
 * extend
 * `BasePage` to reuse common behavior and keep page-specific code focused.
 * - Single Responsibility: this class only models UI interactions; logging and
 * driver lifecycle are delegated to other utilities.
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor captures the thread-local WebDriver and initializes an explicit
     * wait.
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /**
     * Checks whether an element located by `locator` is present and visible.
     */
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the current browser URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Clicks an element after waiting for it to be clickable. Logging is
     * delegated to `LoggerUtil` (cross-cutting concern separated from page logic).
     */
    protected void click(By locator) {
        LoggerUtil.logInfo("Clicking element: " + locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Types text into an input after waiting for visibility and clearing existing
     * content first.
     */
    protected void type(By locator, String text) {
        LoggerUtil.logInfo("Typing '" + text + "' into: " + locator);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Returns visible text of the element located by `locator`.
     */
    protected String getText(By locator) {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        LoggerUtil.logInfo("Retrieved text: '" + text + "' from: " + locator);
        return text;
    }
}