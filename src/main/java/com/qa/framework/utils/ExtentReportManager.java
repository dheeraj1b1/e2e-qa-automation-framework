package com.qa.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File; // Import this!

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports initReports() {
        if (extent == null) {
            // 1. Get the Suite Type from Gradle (passed via systemProperty)
            String suiteType = System.getProperty("suiteType", "UI").toUpperCase();

            String path = System.getProperty("user.dir") + "/reports";
            File outputDir = new File(path);

            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // 2. Dynamic File Name (e.g., API_ExtentReport.html or UI_ExtentReport.html)
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(path + "/" + suiteType + "_ExtentReport.html");

            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setReportName(suiteType + " Test Results"); // Dynamic Name
            sparkReporter.config().setDocumentTitle(suiteType + " Automation Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Suite Type", suiteType); // Add to report info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void createTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}