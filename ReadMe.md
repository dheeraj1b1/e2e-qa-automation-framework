# E2E QA Automation Framework

A robust, scalable, and thread-safe test automation framework built with **Java**, **Selenium WebDriver**, and **TestNG**. It implements the **Page Object Model (POM)** design pattern and features detailed HTML reporting via **Extent Reports**.

---

## 🚀 Tech Stack

| Component | Technology | Version |
| :--- | :--- | :--- |
| **Language** | Java | 17+ |
| **Build Tool** | Gradle | 8.x |
| **UI Automation** | Selenium WebDriver | 4.27.0 |
| **Test Runner** | TestNG | 7.10.2 |
| **Reporting** | Extent Reports | 5.1.1 |
| **Driver Management** | WebDriverManager | 5.9.2 |
| **Logging** | SLF4J / Log4j2 | 2.0.x |

---

## 📂 Project Structure

The project strictly separates **Framework Logic** (Core) from **Test Execution** (Tests).

```text
e2e-qa-automation-framework/
├── src/main/java/com/qa/framework/
│   ├── config/          # Reads properties (Browser, URL)
│   ├── driver/          # Thread-safe Driver Initialization
│   ├── pages/           # Page Object Classes (Locators & Actions)
│   ├── utils/           # Utilities (Reporting, File IO)
│   └── listeners/       # TestNG Listeners (Logging & Reporting hooks)
├── src/test/java/com/qa/tests/
│   ├── base/            # BaseTest (Setup/Teardown)
│   └── ui/              # Actual Test Classes
├── src/main/resources/
│   └── config.properties # Global Configuration File
└── reports/             # Generated HTML Reports