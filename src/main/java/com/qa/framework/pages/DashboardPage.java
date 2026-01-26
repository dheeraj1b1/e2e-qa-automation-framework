package com.qa.framework.pages;

import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    // Existing Locators
    private By userDropdown = By.className("oxd-userdropdown-name");
    private By pimOption = By.xpath("//span[text()='PIM']");

    // NEW: Locators for Admin and Logout
    private By adminMenu = By.xpath("//span[text()='Admin']");
    private By userDropdownIcon = By.className("oxd-userdropdown-tab"); // The clickable dropdown
    private By logoutLink = By.xpath("//a[contains(text(), 'Logout')]");

    public String getLoggedInUser() {
        return getText(userDropdown);
    }

    public boolean isPIMOptionDisplayed() {
        return isDisplayed(pimOption);
    }

    // --- New Transaction Methods ---

    public AdminPage clickAdminModule() {
        click(adminMenu);
        return new AdminPage();
    }

    public LoginPage logout() {
        click(userDropdownIcon); // 1. Click Profile Icon
        click(logoutLink); // 2. Click Logout
        return new LoginPage(); // Returns us to the Login Screen
    }
}