package com.qa.framework.pages;

import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // Locators
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.xpath("//button[@type='submit']");

    // Actions
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this; // Fluent return
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public DashboardPage clickLogin() {
        click(loginButton);
        return new DashboardPage(); // Navigates to the next page
    }

    // Wrapper to do it all at once
    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }
}