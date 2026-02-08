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

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {

        String browser = ConfigReader.getProperty("browser").toLowerCase();
        WebDriver dr = null;

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();

                // LOGIC UPDATE: Check both Config file AND Jenkins Environment Variable
                String headlessEnv = System.getenv("HEADLESS");
                boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"))
                        || (headlessEnv != null && headlessEnv.equals("true"));

                if (isHeadless) {
                    options.addArguments("--headless=new");

                    // CRITICAL FLAGS FOR AWS/LINUX SERVER STABILITY
                    options.addArguments("--no-sandbox"); // Bypass OS security model (required on some Linux
                                                          // environments)
                    options.addArguments("--disable-dev-shm-usage"); // Prevents crash on limited memory (t2.micro)
                    options.addArguments("--disable-gpu"); // Disables GPU hardware acceleration
                    options.addArguments("--window-size=1920,1080"); // Ensures elements are visible/clickable
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

        // Add a null check before setting timeouts to avoid NullPointerException if
        // driver creation fails
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