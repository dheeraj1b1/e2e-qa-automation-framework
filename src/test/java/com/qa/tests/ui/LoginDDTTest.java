package com.qa.tests.ui;

import com.qa.framework.pages.DashboardPage;
import com.qa.framework.pages.LoginPage;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginDDTTest extends BaseTest {

    // Data Provider: Supplies data to the test method
    @DataProvider(name = "loginData")
    public Object[][] getData() {
        return new Object[][] {
                // { username, password, shouldLoginSucceed }
                { "Admin", "admin123", true }, // Valid
                { "Admin", "WrongPass", false }, // Invalid Password
                { "WrongUser", "admin123", false } // Invalid Username
        };
    }

    @Test(dataProvider = "loginData", description = "Login test with multiple data sets")
    public void testLoginDDT(String username, String password, boolean shouldLoginSucceed) {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);

        if (shouldLoginSucceed) {
            // If we expect success, check for Dashboard
            Assert.assertTrue(getDriver().getCurrentUrl().contains("dashboard"),
                    "Login failed for valid creds: " + username);

            // Logout to reset state for next iteration (if using same browser)
            new DashboardPage().logout();
        } else {
            // If we expect failure, check we are still on login page (or see error)
            Assert.assertTrue(getDriver().getCurrentUrl().contains("login"),
                    "Login succeeded for invalid creds: " + username);
        }
    }
}