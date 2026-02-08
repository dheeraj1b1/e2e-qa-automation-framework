package com.qa.tests.ui;

import com.qa.framework.pages.AdminPage;
import com.qa.framework.pages.DashboardPage;
import com.qa.framework.pages.LoginPage;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdminUserTransaction extends BaseTest {

        @Test(description = "Transaction: Login -> Verify Admin Table -> Logout")
        public void testAdminTableVisibilityAndLogout() {
                // 2. Login
                LoginPage loginPage = new LoginPage();
                DashboardPage dashboardPage = loginPage.login("Admin", "admin123");

                // 3. Dashboard Validation
                Assert.assertTrue(dashboardPage.isPIMOptionDisplayed(),
                                "Pre-Condition Failed: Dashboard did not load.");

                // 4. Navigate to Admin
                AdminPage adminPage = dashboardPage.clickAdminModule();

                // 5. Verify Table
                Assert.assertTrue(adminPage.isUsersTableDisplayed(),
                                "Transaction Failed: Admin Users table is not visible.");

                // 6. Logout
                LoginPage finalLoginPage = dashboardPage.logout();

                // 7. Verify Logout
                Assert.assertTrue(finalLoginPage.isLoginButtonDisplayed(),
                                "Logout Failed: Login button not visible.");
        }
}