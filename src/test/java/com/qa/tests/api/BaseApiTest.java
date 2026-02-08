package com.qa.tests.api;

// import io.restassured.RestAssured;
// import io.restassured.parsing.Parser;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import com.qa.framework.config.*;

public class BaseApiTest {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        // Build the Request Specification
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(ConfigReader.getProperty("baseUrl"));
        reqBuilder.setContentType(ContentType.JSON);

        // --- ADD THIS LINE TO TRICK CLOUDFLARE ---
        reqBuilder.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        // -----------------------------------------

        requestSpec = reqBuilder.build();

        // Build the Response Specification
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(5000L));
        responseSpec = resBuilder.build();
    }
}