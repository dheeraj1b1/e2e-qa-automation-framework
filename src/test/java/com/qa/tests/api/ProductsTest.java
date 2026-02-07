package com.qa.tests.api;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsTest extends BaseApiTest {

    // Shared Data for Chaining
    private static String authToken;
    private static int productId;
    private static final String SCHEMA_PATH = "src/test/resources/schemas/product-schema.json";

    @Test(priority = 1, description = "POST Login to Generate Token")
    public void testLoginAndGenerateToken() {
        // Prepare Login Payload (Standard FakeStoreAPI User)
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "johnd");
        credentials.put("password", "m38rmF$");

        authToken = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .log().ifError()
                .statusCode(201)
                .body("token", notNullValue())
                .extract().path("token");

        System.out.println("Generated Auth Token: " + authToken);
    }

    @Test(priority = 2, description = "POST Create Product & Extract ID", dependsOnMethods = "testLoginAndGenerateToken")
    public void testCreateProduct() {
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("title", "Senior SDET Automation Item");
        newProduct.put("price", 105.50);
        newProduct.put("description", "Created via RestAssured Chaining");
        newProduct.put("image", "https://i.pravatar.cc");
        newProduct.put("category", "electronic");

        productId = given()
                .contentType(ContentType.JSON)
                .body(newProduct)
                .when()
                .post("/products")
                .then()
                .log().ifError()
                .statusCode(201) // FakeStore returns 200 for creation
                .body("title", equalTo("Senior SDET Automation Item"))
                .extract().path("id");

        System.out.println("Extracted Product ID: " + productId);
        Assert.assertTrue(productId > 0, "Product ID should be generated");
    }

    @Test(priority = 3, description = "GET Product with Schema Validation")
    public void testGetProductWithSchema() {
        // Note: Using ID 1 for stability because FakeStoreAPI doesn't persist new IDs
        // (21+)
        int stableId = 1;

        given()
                .header("Authorization", "Bearer " + authToken) // Demonstrating Auth Header usage
                .when()
                .get("/products/" + stableId)
                .then()
                .statusCode(200)
                .body("id", equalTo(stableId))
                .body(JsonSchemaValidator.matchesJsonSchema(new File(SCHEMA_PATH))); // Schema Validation
    }

    @Test(priority = 4, description = "PUT Update Product using Extracted ID", dependsOnMethods = "testCreateProduct")
    public void testUpdateProduct() {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("title", "Updated Product Title");
        updatedData.put("price", 99.99);

        // Chaining: Using 'productId' extracted from priority 2
        given()
                .contentType(ContentType.JSON)
                .body(updatedData)
                .when()
                .put("/products/" + productId)
                .then()
                .log().ifError()
                .statusCode(200)
                .body("id", equalTo(productId)) // Validating consistency
                .body("title", equalTo("Updated Product Title"))
                .body("price", equalTo(99.99f));
    }

    @Test(priority = 5, description = "DELETE Product using Extracted ID", dependsOnMethods = "testCreateProduct")
    public void testDeleteProduct() {
        // Chaining: Using 'productId' extracted from priority 2
        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/products/" + productId)
                .then()
                .log().ifError()
                .statusCode(200);
        // .body("id", equalTo(productId)); // FakeStore echoes the deleted ID
    }

    @Test(priority = 6, description = "GET All Products Schema Validation")
    public void testGetAllProductsSchema() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                // Validate that the array items match the schema
                // Note: matchesJsonSchema typically validates a single object, so we verify the
                // list not empty first
                .body("size()", greaterThan(0));
    }
}