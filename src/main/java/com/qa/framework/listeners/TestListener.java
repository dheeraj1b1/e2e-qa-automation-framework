package com.qa.framework.listeners;

import com.aventstack.extentreports.Status;
import com.qa.framework.utils.ExtentReportManager;
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
        // We will add Screenshots here in the next step!
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped");
    }
}