package com.qa.tests.ui;

import com.qa.framework.pages.LoginPage;
import com.qa.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NegativeLoginTest extends BaseTest {

    @Test(description = "Verify invalid login displays error message")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage();

        // 1. Try to login with wrong password
        loginPage.login("Admin", "WrongPass123");

        // 2. Verify the error message exists (Business Negative Test)
        // Note: OrangeHRM error locator usually is class="oxd-alert-content-text"
        boolean isErrorDisplayed = loginPage.isDisplayed(By.xpath("//p[contains(@class, 'oxd-alert-content-text')]"));

        Assert.assertTrue(isErrorDisplayed, "Error message should be displayed for invalid login.");
    }

    @Test(description = "Intentionally failing test to verify Screenshot generation")
    public void verifyScreenshotFeature() {
        LoginPage loginPage = new LoginPage();

        // 1. Try to login with wrong password
        loginPage.login("Admin", "WrongPass123");

        // 2. INTENTIONALLY FAIL: Claim that we should be on the Dashboard
        // This assertion will fail because we are still on the login page
        Assert.assertTrue(loginPage.getCurrentUrl().contains("dashboard"),
                "Intentional Failure: We expected dashboard but got login page.");
    }
}