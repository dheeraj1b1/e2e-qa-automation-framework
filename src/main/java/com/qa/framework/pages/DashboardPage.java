package com.qa.framework.pages;

import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    // Locators
    private By userDropdown = By.className("oxd-userdropdown-name");
    private By pimOption = By.xpath("//span[text()='PIM']");

    // Actions
    public String getLoggedInUser() {
        return getText(userDropdown);
    }

    public boolean isPIMOptionDisplayed() {
        return isDisplayed(pimOption);
    }
}