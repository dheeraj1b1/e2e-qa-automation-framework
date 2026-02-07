# 🚀 Hybrid E2E QA Automation Framework (UI + API)

A production-grade, robust, and scalable test automation framework built with **Java**, **Selenium WebDriver**, **REST Assured**, and **TestNG**.

This project demonstrates a **Hybrid Architecture** that strictly separates test logic from framework logic, utilizing industry-standard design patterns like **Page Object Model (POM)**, **Singleton**, and **Factory** patterns. It is designed for **CI/CD integration** with dynamic suite execution and thread-safe parallel processing.

---

## 🌟 Key Features

### **1. Architecture & Design**
* **Hybrid Framework:** Unified codebase supporting both **UI (Selenium)** and **API (REST Assured)** automation.
* **Page Object Model (POM):** Decoupled locators/actions from assertions for maintainability.
* **Thread-Safe Execution:** Implemented `ThreadLocal<WebDriver>` to ensure isolated browser sessions during **Parallel Execution**.
* **Singleton & Factory Patterns:** Centralized `DriverManager` to handle cross-browser logic (Chrome, Firefox, Edge, Headless).

### **2. Reliability & Resilience**
* **Self-Healing Mechanism:** Integrated `IRetryAnalyzer` to automatically retry failed tests (handling flaky 500 errors or network lag).
* **Smart Synchronization:** Replaced hard sleeps with explicit `WebDriverWait` and dynamic polling.
* **Error Handling:** Custom `TestListener` capturing screenshots **only on UI failures** (skipping API tests) to reduce noise.

### **3. Reporting & Observability**
* **Dynamic Extent Reports:** Automatically generates separate reports (`UI_ExtentReport.html` vs `API_ExtentReport.html`) based on the execution suite to prevent overwriting.
* **Structured Logging:** Integrated **SLF4J** with a custom `LoggerUtil` that logs actions to both the Console and the HTML Report simultaneously.

### **4. CI/CD Readiness**
* **Gradle-Based Execution:** Tests are triggered via command-line arguments, making it Jenkins/GitHub Actions ready.
* **Suite Separation:** Strict isolation ensures UI and API tests never run together unless explicitly configured.

---

## 🛠️ Tech Stack

| Component | Technology | Version |
| :--- | :--- | :--- |
| **Language** | Java (JDK 17+) | 17 |
| **Build Tool** | Gradle | 8.5+ |
| **UI Automation** | Selenium WebDriver | 4.27.0 |
| **API Automation** | REST Assured | 5.4.0 |
| **Test Runner** | TestNG | 7.10.2 |
| **Reporting** | Extent Reports | 5.1.1 |
| **Logging** | SLF4J Simple | 2.0.16 |
| **Utils** | Lombok, Jackson Databind | Latest |

---

## 📂 Project Structure

The project follows a modular structure separating the **Core Framework** from the **Test Layer**.

```text
e2e-qa-automation-framework/
├── src/main/java/com/qa/framework/
│   ├── config/          # ConfigReader (Environment/Browser props)
│   ├── driver/          # Thread-safe Driver Factory
│   ├── pages/           # Page Object Classes (Locators & Actions)
│   ├── utils/           # Utilities (Logger, Retry, Screenshot, Reports)
│   └── listeners/       # TestNG Listeners (Hooks for Logs/Reports)
│
├── src/test/java/com/qa/tests/
│   ├── api/             # REST Assured Tests (CRUD, Schema Validation)
│   ├── ui/              # Selenium Tests (Login, E2E Flows)
│   └── base/            # BaseTest (Setup/Teardown)
│
├── src/test/resources/
│   ├── testng-ui.xml    # UI Test Suite
│   ├── testng-api.xml   # API Test Suite
│   └── schemas/         # JSON Schemas for API Validation
│
├── reports/             # Generated HTML Reports (Git Ignored)
└── build.gradle         # Dependencies & Task Configuration
```

---

## ⚡ How to Run Tests

The framework uses **Gradle Properties (-P)** to switch suites dynamically. This allows precise control over which layer (UI vs API) is executed.

### **1. Default Execution (UI Suite)**

If no suite is specified, the framework defaults to running the UI Suite.

```bash
./gradlew clean test
```

- **Runs:** Selenium Tests
- **Report:** `reports/UI_ExtentReport.html`

### **2. Run UI Tests Explicitly**

Executes Selenium tests on the configured browser (default: Chrome).

```bash
./gradlew clean test -Psuite=ui
```

- **Target Application:** OrangeHRM Demo
- **Report Generated:** `reports/UI_ExtentReport.html`

### **3. Run API Tests Only**

Executes REST Assured tests (Headless/No Browser).

```bash
./gradlew clean test -Psuite=api
```

- **Target Application:** FakeStore API
- **Report Generated:** `reports/API_ExtentReport.html`

## 🧪 Scenarios Covered

### **UI Scenarios (OrangeHRM)**
- **Data-Driven Testing (DDT):** Valid/Invalid Login scenarios utilizing `@DataProvider` to test multiple credential sets.
- **E2E Transaction:** Complete workflow: Login → Admin Panel Navigation → User Search → Logout.
- **Negative Testing:** Verification of error messages, field validation, and boundary analysis.
- **Cross Browser:** Validated on Chrome, Firefox, and Edge browsers.

### **API Scenarios (FakeStore)**
- **Chaining:** Advanced request chaining: POST Create → Extract ID → GET / PUT / DELETE using the extracted ID.
- **Auth Handling:** Login flow to generate Bearer Token → Use Token in subsequent authenticated requests.
- **Schema Validation:** Automated JSON response validation against `product-schema.json`.
- **CRUD Lifecycle:** Full Create, Read, Update, Delete verification cycle.

## 📊 Reporting

The framework produces rich HTML Dashboards (**Extent Reports**) designed for clarity and quick debugging:

- **Pass/Fail Charts:** Visual breakdown of execution status.
- **Execution Time:** Detailed duration tracking for every test step.
- **Smart Screenshots:** Automatically captures and attaches screenshots only when a UI test fails.
- **API Logs:** Request methods, Endpoints, and Response Status codes are logged cleanly in the report without clutter.

---

## 👤 Author

**Dheeraj B**  
*SDET / Software Quality Engineer*