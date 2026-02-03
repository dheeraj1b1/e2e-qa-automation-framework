package com.qa.tests.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class ProductsTest extends BaseApiTest {

    @Test(priority = 1, description = "GET All Products")
    public void testGetAllProducts() {
        RestAssured
                .given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test(priority = 2, description = "GET Single Product by ID")
    public void testGetSingleProduct() {
        RestAssured
                .given()
                .when()
                .get("/products/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", not(empty()));
    }

    @Test(priority = 3, description = "POST Create New Product")
    public void testCreateProduct() {
        // Construct JSON Payload
        Map<String, Object> product = new HashMap<>();
        product.put("title", "Test Automation Product");
        product.put("price", 29.99);
        product.put("description", "Created via RestAssured");
        product.put("image", "https://i.pravatar.cc");
        product.put("category", "electronic");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/products")
                .then()
                .statusCode(201) // FakeStoreAPI returns 200 (not 201)
                .body("id", notNullValue())
                .body("title", equalTo("Test Automation Product"));
    }

    @Test(priority = 4, description = "PUT Update Product")
    public void testUpdateProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("title", "Updated Product Name");
        product.put("price", 45.00);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .put("/products/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Product Name"));
    }

    @Test(priority = 5, description = "DELETE Product")
    public void testDeleteProduct() {
        RestAssured
                .given()
                .when()
                .delete("/products/1")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }
}