package com.qa.tests.reqres;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseReqResTest {

    protected RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {
        // 1. Set Base URL
        RestAssured.baseURI = "https://reqres.in/api";

        // 2. Build the "Separate Stencil" with Strict Headers
        RequestSpecBuilder builder = new RequestSpecBuilder();

        // "Step 2: Always send a real User-Agent" (Crucial for AWS)
        builder.addHeader("User-Agent", "reqres-qa-tests/1.0");

        // "Step 1: QA API Key" (Using the one you generated)
        builder.addHeader("x-api-key", "reqres_47d5a647b9674253a0e0404d81cdc60c");

        // Common Content-Type
        builder.addHeader("Content-Type", "application/json");

        reqSpec = builder.build();
    }
}