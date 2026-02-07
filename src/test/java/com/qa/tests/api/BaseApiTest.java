package com.qa.tests.api;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://fakestoreapi.com";

        // Force RestAssured to always use JSON parser
        RestAssured.defaultParser = Parser.JSON;
    }
}