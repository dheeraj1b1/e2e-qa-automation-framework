package com.qa.tests.api;

import com.qa.framework.config.ConfigReader; // Keep this import
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();

        // --- FIX: Force the correct URL directly here ---
        // reqBuilder.setBaseUri(ConfigReader.getProperty("baseUrl")); // <-- COMMENT
        // THIS OUT
        reqBuilder.setBaseUri("https://fakestoreapi.com"); // <-- ADD THIS
        // ------------------------------------------------

        reqBuilder.setContentType(ContentType.JSON);

        // Add the User-Agent header to trick Cloudflare
        reqBuilder.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        requestSpec = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(5000L));
        responseSpec = resBuilder.build();
    }
}