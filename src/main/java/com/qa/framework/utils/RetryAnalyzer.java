package com.qa.framework.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int maxTry = 1; // Retry 1 time if it fails

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < maxTry) {
                count++;
                return true; // Tells TestNG to re-run the test
            }
        }
        return false;
    }
}