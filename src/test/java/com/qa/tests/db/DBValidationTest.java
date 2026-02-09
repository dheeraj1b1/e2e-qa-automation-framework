package com.qa.tests.db;

import com.qa.framework.utils.DBUtil;
import com.qa.framework.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class DBValidationTest {

    @Test(priority = 1, description = "Verify Database Connection is successful")
    public void testDBConnection() {
        LoggerUtil.logInfo("DB: Testing Connection...");
        boolean isConnected = false;
        try {
            if (DBUtil.getConnection() != null) {
                isConnected = true;
            }
        } catch (Exception e) {
            LoggerUtil.logError("Connection failed: " + e.getMessage());
        }
        Assert.assertTrue(isConnected, "Database connection should be established.");
        LoggerUtil.logInfo("DB: Connection Successful.");
    }

    @Test(priority = 2, description = "Verify 'admin_user' exists (Simulating successful Admin creation)")
    public void testAdminUserExists() {
        LoggerUtil.logInfo("DB: Verifying 'admin_user' existence...");
        boolean exists = DBUtil.isUserPresent("admin_user");
        Assert.assertTrue(exists, "Admin user should exist in DB.");
        LoggerUtil.logInfo("DB: Admin user found.");
    }

    @Test(priority = 3, description = "Verify Data Integrity: Email matches for 'standard_user'")
    public void testUserDataIntegrity() {
        LoggerUtil.logInfo("DB: Validating email for 'standard_user'...");
        Map<String, String> user = DBUtil.getUserData("standard_user");

        Assert.assertEquals(user.get("email"), "user@test.com", "Email should match mock data.");
        LoggerUtil.logInfo("DB: Email validation passed.");
    }

    @Test(priority = 4, description = "Verify Role Constraint: 'api_bot' has 'system' role")
    public void testUserRoleValidation() {
        LoggerUtil.logInfo("DB: Checking role for 'api_bot'...");
        Map<String, String> user = DBUtil.getUserData("api_bot");

        Assert.assertEquals(user.get("role"), "system", "Role should be 'system'.");
        LoggerUtil.logInfo("DB: Role validation passed.");
    }

    @Test(priority = 5, description = "Verify Account Status: 'locked_user' is inactive")
    public void testUserStatus() {
        LoggerUtil.logInfo("DB: Checking active status for 'locked_user'...");
        Map<String, String> user = DBUtil.getUserData("locked_user");

        // MySQL stores BOOLEAN as TINYINT (1=True, 0=False)
        Assert.assertEquals(user.get("is_active"), "0", "User should be inactive (0).");
        LoggerUtil.logInfo("DB: Status validation passed.");
    }

    @Test(priority = 6, description = "Negative Test: Verify non-existent user returns empty")
    public void testNonExistentUser() {
        LoggerUtil.logInfo("DB: Verifying 'ghost_user' does NOT exist...");
        boolean exists = DBUtil.isUserPresent("ghost_user");
        Assert.assertFalse(exists, "Non-existent user should not be found.");
        LoggerUtil.logInfo("DB: Negative test passed.");
    }
}