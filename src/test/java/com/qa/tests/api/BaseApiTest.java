package com.qa.tests.api;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public void setup() {
        // Set the base URI used by RestAssured for all tests in this class
        RestAssured.baseURI = "https://fakestoreapi.com";

        // Add a common User-Agent header to RestAssured requests to avoid Cloudflare
        // blocks
        RestAssured.requestSpecification = RestAssured.given().header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        // Enable logging on validation failures to help debug flaky API validations
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}