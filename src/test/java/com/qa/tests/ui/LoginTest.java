package com.qa.tests.ui;

import com.qa.framework.driver.DriverManager;
import com.qa.framework.pages.DashboardPage;
import com.qa.framework.pages.LoginPage;
import com.qa.tests.base.BaseTest;
import com.qa.framework.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testValidLogin() {
        // 1. Navigate
        DriverManager.getDriver().get("https://opensource-demo.orangehrmlive.com/");

        // 2. Login
        // Notice: No need to pass 'driver' anymore. BasePage handles it.
        LoginPage loginPage = new LoginPage();

        // Fluent style chaining (Optional, but looks nice)
        DashboardPage dashboardPage = loginPage.enterUsername("Admin")
                .enterPassword("admin123")
                .clickLogin();

        // 3. Validation
        String userName = dashboardPage.getLoggedInUser();
        LoggerUtil.logInfo("Logged in as: " + userName);

        Assert.assertNotNull(userName, "User name should be visible!");
        Assert.assertTrue(dashboardPage.isPIMOptionDisplayed(), "PIM Menu should be visible!");
    }
}