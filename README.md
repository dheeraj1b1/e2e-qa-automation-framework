# Hybrid E2E QA Automation Framework

> A modular, multi-layer test automation framework supporting **UI automation**, **API testing**, and **Database validation** in a single unified codebase. Built with Java, Selenium WebDriver, REST Assured, and TestNG, with CI/CD pipelines for both Jenkins and GitHub Actions.

---

## What This Project Demonstrates for QA / SDET Roles

| Skill | Implementation |
|---|---|
| **UI Automation** | Selenium WebDriver 4 with Page Object Model — OrangeHRM flows |
| **API Testing** | REST Assured — CRUD lifecycle, schema validation, auth flows |
| **DB Validation** | JDBC + MySQL — seeded test data, query-based assertions |
| **Reusable Framework Design** | Strict separation of framework layer vs test layer |
| **Design Patterns** | POM, Singleton (DriverManager), Factory (cross-browser) |
| **Waits & Synchronization** | `WebDriverWait` with `ExpectedConditions` — no hard sleeps |
| **Reporting** | Dynamic ExtentReports per suite (UI / API / DB), SLF4J logging |
| **Retry Logic** | `IRetryAnalyzer` for automatic test retries on transient failures |
| **CI/CD Integration** | GitHub Actions (working pipeline) + Jenkinsfile (pipeline config) |
| **Cross-Browser Testing** | Chrome, Firefox, Edge — configurable via command line |

---

## Tech Stack

| Component | Technology | Version |
|---|---|---|
| **Language** | Java (JDK 17) | 17 |
| **Build Tool** | Gradle | 8.5+ |
| **UI Automation** | Selenium WebDriver | 4.27.0 |
| **Driver Management** | WebDriverManager | 5.9.2 |
| **API Automation** | REST Assured | 5.4.0 |
| **API Schema Validation** | REST Assured JSON Schema Validator | 5.4.0 |
| **JSON Parsing** | Jackson Databind | 2.16.1 |
| **Test Runner** | TestNG | 7.10.2 |
| **Reporting** | Extent Reports | 5.1.1 |
| **Logging** | SLF4J Simple | 2.0.16 |
| **Utilities** | Lombok | 1.18.34 |
| **DB Driver** | MySQL Connector/J | 8.0.33 |

---

## Architecture Overview

The framework uses a **layered architecture** that strictly separates framework infrastructure from test logic.

```
┌─────────────────────────────────────────────────┐
│                  TEST LAYER                      │
│   UI Tests │ API Tests │ ReqRes Tests │ DB Tests │
└─────────────────────┬───────────────────────────┘
                      │ uses
┌─────────────────────▼───────────────────────────┐
│               FRAMEWORK LAYER                    │
│  DriverManager (ThreadLocal) │ ConfigReader      │
│  BasePage (shared actions)   │ BaseTest          │
│  Page Objects (POM)          │ DBUtil (JDBC)     │
│  ExtentReportManager         │ LoggerUtil        │
│  RetryAnalyzer               │ ScreenshotUtil    │
└─────────────────────────────────────────────────┘
                      │ config
┌─────────────────────▼───────────────────────────┐
│               RESOURCES / CONFIG                 │
│  testng-ui.xml │ testng-api.xml │ testng-db.xml  │
│  testng-reqres.xml │ JSON Schemas │ db_seed.sql  │
└─────────────────────────────────────────────────┘
```

### Framework Design Decisions

- **`ThreadLocal<WebDriver>`** — Enables thread-safe parallel test execution without shared state
- **Factory Pattern in DriverManager** — Cross-browser logic (Chrome/Firefox/Edge/Headless) is centralized and configurable via `-Dbrowser=`
- **Suite-based Gradle tasks** — Each test layer is invoked with `-Psuite=ui|api|api-ci|db`, preventing layer interference
- **Listener-based reporting** — `TestListener` hooks into TestNG lifecycle to auto-capture screenshots on UI failure only (API tests are excluded)

---

## Project Structure

```
e2e-qa-automation-framework/
├── src/
│   ├── main/java/com/qa/framework/
│   │   ├── config/
│   │   │   └── ConfigReader.java          # Reads environment/browser properties
│   │   ├── driver/
│   │   │   └── DriverManager.java         # ThreadLocal WebDriver factory (Chrome/Firefox/Edge)
│   │   ├── pages/
│   │   │   ├── BasePage.java              # Shared Selenium actions (click, type, wait)
│   │   │   ├── LoginPage.java             # OrangeHRM login page object
│   │   │   ├── DashboardPage.java         # Dashboard post-login page object
│   │   │   └── AdminPage.java             # Admin user management page object
│   │   ├── utils/
│   │   │   ├── DBUtil.java                # JDBC helper — MySQL connection + query execution
│   │   │   ├── ExtentReportManager.java   # Dynamic report creation per suite
│   │   │   ├── LoggerUtil.java            # Dual-output logger (console + report)
│   │   │   ├── RetryAnalyzer.java         # IRetryAnalyzer — auto-retries on failure
│   │   │   └── ScreenshotUtil.java        # Screenshot capture on UI test failure
│   │   └── listeners/
│   │       └── TestListener.java          # TestNG hooks for reporting and screenshots
│   │
│   └── test/
│       ├── java/com/qa/tests/
│       │   ├── base/
│       │   │   └── BaseTest.java          # Setup/Teardown — driver init and report init
│       │   ├── ui/                        # Selenium UI tests (OrangeHRM)
│       │   ├── api/                       # REST Assured tests (FakeStore API)
│       │   ├── reqres/                    # REST Assured tests (ReqRes.in — CI safe)
│       │   └── db/                        # JDBC database validation tests
│       └── resources/
│           ├── testng-ui.xml
│           ├── testng-api.xml
│           ├── testng-reqres.xml
│           ├── testng-db.xml
│           ├── db_seed.sql                # MySQL test data seed script
│           └── schemas/
│               ├── product-schema.json    # JSON schema for FakeStore API
│               └── reqres-user-schema.json
│
├── .github/workflows/
│   └── main.yml                           # GitHub Actions CI pipeline
├── Jenkinsfile                            # Jenkins declarative pipeline config
├── build.gradle                           # Dependencies and suite task config
├── settings.gradle
└── gradlew / gradlew.bat
```

---

## Test Scenarios Covered

### UI Automation — OrangeHRM (Selenium)

| Scenario | Type |
|---|---|
| Valid + Invalid Login (multiple credential sets via `@DataProvider`) | Data-driven |
| E2E flow: Login → Dashboard → Admin Panel → User Search → Logout | E2E |
| Negative testing: Error message and field validation assertions | Negative |
| Cross-browser execution: Chrome, Firefox, Edge | Cross-browser |

### API Testing — ReqRes.in (CI/Cloud-safe)

| Scenario | Coverage |
|---|---|
| CRUD lifecycle: Create → Read → Update (PUT & PATCH) → Delete | CRUD |
| Negative testing: `404` handling for non-existent resources | Negative |
| JSON schema validation against `reqres-user-schema.json` | Schema |
| Registration flow: `POST /register` → token assertion | Auth |
| List retrieval with custom `User-Agent` headers | Smoke |

### API Testing — FakeStore (REST Assured)

| Scenario | Coverage |
|---|---|
| Request chaining: POST → extract ID → GET/PUT/DELETE | Chaining |
| Auth flow: Login → generate Bearer token → authenticated requests | Auth |
| JSON schema validation against `product-schema.json` | Schema |
| Full CRUD lifecycle | CRUD |

### Database Validation — MySQL (JDBC)

| Scenario | Coverage |
|---|---|
| Verify seeded records exist with correct values | Data integrity |
| Assert row count after insert/delete operations | DB state |
| Query-based validation of application data | DB assertions |

---

## How to Run Tests

### Prerequisites

- Java 17+
- Gradle (or use the `./gradlew` wrapper)
- Chrome (default browser) — Firefox/Edge optional
- MySQL running locally on port `3306` for the DB suite

### Suite Commands

```bash
# Run UI tests (default browser: Chrome)
./gradlew clean test -Psuite=ui

# Run API tests — ReqRes.in (CI-safe, no browser)
./gradlew clean test -Psuite=api-ci

# Run API tests — FakeStore
./gradlew clean test -Psuite=api

# Run DB validation tests
./gradlew clean test -Psuite=db
```

### Cross-Browser and Headless

```bash
# Run on Firefox
./gradlew clean test -Psuite=ui -Dbrowser=firefox

# Run on Edge
./gradlew clean test -Psuite=ui -Dbrowser=edge

# Run headless (CI/CD)
./gradlew clean test -Psuite=ui -Dheadless=true
```

### Database Setup (for DB suite)

```bash
# Create automation_db and seed test data
mysql -u root -p < src/test/resources/db_seed.sql
```

---

## CI/CD

This project has **working CI/CD pipelines** on both GitHub Actions and Jenkins.

### GitHub Actions (`main.yml`)

Triggers on push and pull request to `main`.

**Pipeline stages:**
1. Checkout code
2. Set up JDK 17
3. Spin up MySQL service container (`mysql:8.0`) and seed with `db_seed.sql`
4. Run API tests (ReqRes suite)
5. Run UI tests in headless Chrome
6. Run DB validation tests
7. Upload Extent Report HTML files as build artifacts

### Jenkins Pipeline (`Jenkinsfile`)

A declarative pipeline for self-hosted Jenkins environments:
- Injects DB credentials securely via Jenkins Credentials Manager
- Runs API → UI → DB suites in sequence
- Publishes separate HTML report tabs for each suite

---

## Reporting

Extent Reports are generated per execution suite to avoid overwriting:

| Suite | Report File |
|---|---|
| UI | `reports/UI_ExtentReport.html` |
| API (ReqRes) | `reports/ReqRes_API_ExtentReport.html` |
| API (FakeStore) | `reports/API_ExtentReport.html` |
| DB Validation | `reports/DB_Validation_ExtentReport.html` |

Reports include:
- Pass/Fail/Skip status with visual charts
- Step-by-step execution logs
- Screenshots embedded on UI test failure
- API request method, endpoint, and response status per test step

---

## Future Improvements

- [ ] Add end-to-end test scenarios for more application flows
- [ ] Integrate Allure Reports as an alternative to ExtentReports
- [ ] Add parallel execution configuration in TestNG XML suites
- [ ] Expand `@DataProvider` usage to external CSV/Excel files
- [ ] Add negative API schema validation tests (malformed response assertions)
- [ ] Add performance thresholds to API tests (response time assertions)

---

## GitHub Topics

`selenium` `java` `testng` `rest-assured` `qa-automation` `automation-testing` `pom` `webdriver` `api-testing` `database-testing` `extentreports` `gradle` `github-actions` `ci-cd`
