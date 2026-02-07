package com.qa.framework.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.framework.driver.DriverManager;
import com.qa.framework.utils.ExtentReportManager;
import com.qa.framework.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
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

        // FIX: Only take screenshot if Driver is active (UI Tests)
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