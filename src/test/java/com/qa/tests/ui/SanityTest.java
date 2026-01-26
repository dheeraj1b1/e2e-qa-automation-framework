// package com.qa.tests.ui;

// import com.qa.framework.driver.DriverManager;
// import com.qa.tests.base.BaseTest;
// import org.testng.Assert;
// import org.testng.annotations.Test;

// public class SanityTest extends BaseTest {

// @Test
// public void testApplicationLaunch() {
// // 1. Get the driver and navigate
// DriverManager.getDriver().get("https://opensource-demo.orangehrmlive.com/");

// // 2. Capture the title
// String title = DriverManager.getDriver().getTitle();
// System.out.println("Page Title is: " + title);

// // 3. Assert (Validation)
// Assert.assertTrue(title.contains("OrangeHRM"), "Title mismatch!");
// }
// }