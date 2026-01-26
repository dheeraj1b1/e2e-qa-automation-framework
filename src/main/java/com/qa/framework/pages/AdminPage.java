package com.qa.framework.pages;

import org.openqa.selenium.By;

public class AdminPage extends BasePage {

    // Locator for the main records table
    private By systemUsersTable = By.xpath("//div[contains(@class, 'oxd-table')]");

    public boolean isUsersTableDisplayed() {
        return isDisplayed(systemUsersTable);
    }
}