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
 *
 * Architecture & OOP notes:
 * - ThreadLocal Pattern: holds a WebDriver per thread so parallel tests do not
 * share browser state.
 * - Factory-like behavior: `initDriver()` chooses a concrete WebDriver (Chrome,
 * Firefox, Edge)
 * at runtime based on configuration; this is a simple form of the Factory
 * pattern.
 * - Single Responsibility: this class focuses solely on WebDriver lifecycle
 * management
 * (creation, providing, quitting) and delegates configuration values to
 * `ConfigReader`.
 */
public class DriverManager {

    /**
     * ThreadLocal container for WebDriver instances.
     *
     * Architecture note:
     * - We use a ThreadLocal-backed singleton-ish pattern to ensure each test
     * thread
     * receives its own WebDriver instance. This provides thread-safety during
     * parallel execution while keeping a global-access style convenience via
     * static accessor methods (`getDriver()`). It is not a strict singleton
     * (one instance per JVM); instead it is "one instance per thread" which is
     * the correct approach for parallel WebDriver-based test runs.
     */
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Initialize the WebDriver for the current thread.
     *
     * Behavior:
     * - Reads `browser` from `ConfigReader` (or system property) to choose which
     * browser to start.
     * - Honors a `headless` setting from config or the `HEADLESS` environment
     * variable.
     * - Applies additional arguments recommended for CI/Linux environments
     * (e.g. `--no-sandbox`, `--disable-dev-shm-usage`) when running headless.
     */
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

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    // Helpful flags for Linux CI (AWS, Docker):
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
                    // Use addArguments for headless mode in Firefox
                    ffOptions.addArguments("--headless");
                    // add Linux-friendly args where applicable
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

    /**
     * Returns the WebDriver instance for the current thread.
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Quits and removes the WebDriver instance for the current thread.
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}