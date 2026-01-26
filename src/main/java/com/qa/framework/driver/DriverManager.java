package com.qa.framework.driver;

import com.qa.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverManager {

    // ThreadLocal ensures that if we run tests in parallel,
    // each thread gets its own browser instance.
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        WebDriver dr = null;

        // Select browser based on config
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (Boolean.parseBoolean(ConfigReader.get("headless"))) {
                    options.addArguments("--headless=new");
                }
                dr = new ChromeDriver(options);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                dr = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                dr = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Common setup (Wait times, Maximize)
        dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.get("implicitWait"))));
        dr.manage().window().maximize();

        // Save the driver to the ThreadLocal
        driver.set(dr);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove(); // Clean up memory
        }
    }
}