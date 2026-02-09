package com.qa.framework.driver;

import com.qa.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Driver manager responsible for creating and providing thread-safe WebDriver
 * instances.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {

        String browser = ConfigReader.getProperty("browser").toLowerCase();
        WebDriver dr = null;

        // Allow HEADLESS to be controlled by either config property or environment
        // variable
        String headlessEnv = System.getenv("HEADLESS");
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"))
                || (headlessEnv != null && headlessEnv.equalsIgnoreCase("true"));

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                // CRITICAL FIX: Fixes "DevToolsActivePort file doesn't exist" in CI/CD
                chromeOptions.addArguments("--remote-allow-origins=*");

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }

                dr = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (isHeadless) {
                    ffOptions.addArguments("--headless");
                    ffOptions.addArguments("--no-sandbox");
                    ffOptions.addArguments("--disable-dev-shm-usage");
                    ffOptions.addArguments("--width=1920");
                    ffOptions.addArguments("--height=1080");
                }
                dr = new FirefoxDriver(ffOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                // CRITICAL FIX: Fixes connection issues in Edge CI/CD
                edgeOptions.addArguments("--remote-allow-origins=*");

                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--disable-gpu");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                dr = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        if (dr != null) {
            dr.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicitWait"))));
            dr.manage().window().maximize();
            driver.set(dr);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}