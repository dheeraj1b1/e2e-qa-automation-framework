package com.qa.framework.utils;

import com.aventstack.extentreports.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logInfo(String message) {
        // 1. Log to Console
        logger.info(message);

        // 2. Log to Extent Report (if test is active)
        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.INFO, message);
            }
        } catch (Exception e) {
            // Ignore if report is not initialized (e.g., during BeforeSuite)
        }
    }

    public static void logError(String message) {
        logger.error(message);
        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.FAIL, message);
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}