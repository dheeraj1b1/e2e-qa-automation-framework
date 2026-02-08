package com.qa.framework.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.framework.driver.DriverManager;
import com.qa.framework.utils.ExtentReportManager;
import com.qa.framework.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener integrating ExtentReports and screenshots on failures.
 *
 * Design notes:
 * - Uses the TestNG listener (observer) pattern to react to test lifecycle
 * events
 * without polluting test logic with reporting concerns (Separation of
 * Concerns).
 * - Responsible for initializing and flushing reports and attaching artifacts
 * (screenshots) when UI-driven tests fail.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Initialize reporting infrastructure before any tests run
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        // Ensure all report buffers are written
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a logical test entry in the report for each TestNG test method
        ExtentReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, "Test Failed");
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        // Attach a screenshot only if a WebDriver was created for this thread
        if (DriverManager.getDriver() != null) {
            try {
                String screenshot = ScreenshotUtil.getScreenshot();
                ExtentReportManager.getTest().fail("Screenshot of Failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
            } catch (Exception e) {
                ExtentReportManager.getTest().log(Status.WARNING, "Failed to attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped");
    }
}