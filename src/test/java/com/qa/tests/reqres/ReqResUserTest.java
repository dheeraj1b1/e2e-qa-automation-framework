package com.qa.tests.reqres;

import com.qa.framework.utils.LoggerUtil;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqResUserTest extends BaseReqResTest {

    // Shared Data for Chaining
    private static int userId;
    private static final String SCHEMA_PATH = "src/test/resources/schemas/reqres-user-schema.json";

    // ---------------------------------------------------------------
    // SCENARIO 1: SMOKE TEST - LIST USERS
    // ---------------------------------------------------------------
    @Test(priority = 1, description = "GET List Users - Smoke Test")
    public void testListUsers() {
        LoggerUtil.logInfo("ReqRes: GET /users?page=2 - Fetching user list");

        given()
                .spec(reqSpec) // Use our "Separate Stencil"
                .when()
                .get("/users?page=2")
                .then()
                .log().ifError()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(greaterThan(0)));

        LoggerUtil.logInfo("ReqRes: List Users fetched successfully");
    }

    // ---------------------------------------------------------------
    // SCENARIO 2: NEGATIVE TEST - USER NOT FOUND
    // ---------------------------------------------------------------
    @Test(priority = 2, description = "GET Single User - 404 Not Found")
    public void testSingleUserNotFound() {
        LoggerUtil.logInfo("ReqRes: GET /users/23 - Expecting 404");

        given()
                .spec(reqSpec)
                .when()
                .get("/users/23") // ID 23 doesn't exist in ReqRes
                .then()
                .log().ifError()
                .statusCode(404);

        LoggerUtil.logInfo("ReqRes: 404 Not Found validated successfully");
    }

    // ---------------------------------------------------------------
    // SCENARIO 3: CREATE USER (Start of Chain)
    // ---------------------------------------------------------------
    @Test(priority = 3, description = "POST Create User")
    public void testCreateUser() {
        LoggerUtil.logInfo("ReqRes: POST /users - Creating 'Automation SDET'");

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", "Automation SDET");
        newUser.put("job", "QA Engineer");

        userId = given()
                .spec(reqSpec)
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .log().ifError()
                .statusCode(201)
                .body("name", equalTo("Automation SDET"))
                .extract().jsonPath().getInt("id");

        LoggerUtil.logInfo("ReqRes: User created with ID: " + userId);
        Assert.assertTrue(userId > 0, "User ID should be generated");
    }

    // ---------------------------------------------------------------
    // SCENARIO 4: VALIDATE SCHEMA & DATA
    // ---------------------------------------------------------------
    @Test(priority = 4, description = "GET User - JSON Schema Validation")
    public void testGetUserWithSchema() {
        int stableId = 2; // ReqRes data resets, so we use a stable ID for schema checks
        LoggerUtil.logInfo("ReqRes: GET /users/" + stableId + " - Validating Schema");

        given()
                .spec(reqSpec)
                .when()
                .get("/users/" + stableId)
                .then()
                .statusCode(200)
                .body("data.id", equalTo(stableId))
                .body(JsonSchemaValidator.matchesJsonSchema(new File(SCHEMA_PATH)));

        LoggerUtil.logInfo("ReqRes: Schema validation passed");
    }

    // ---------------------------------------------------------------
    // SCENARIO 5: PUT UPDATE (Full Update)
    // ---------------------------------------------------------------
    @Test(priority = 5, description = "PUT Update User", dependsOnMethods = "testCreateUser")
    public void testUpdateUser() {
        LoggerUtil.logInfo("ReqRes: PUT /users/" + userId + " - Promoting to Senior SDET");

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", "Senior SDET");
        updatedData.put("job", "Lead QA");

        given()
                .spec(reqSpec)
                .body(updatedData)
                .when()
                .put("/users/" + userId)
                .then()
                .log().ifError()
                .statusCode(200)
                .body("name", equalTo("Senior SDET"))
                .body("job", equalTo("Lead QA"));

        LoggerUtil.logInfo("ReqRes: PUT Update successful");
    }

    // ---------------------------------------------------------------
    // SCENARIO 6: PATCH UPDATE (Partial Update)
    // ---------------------------------------------------------------
    @Test(priority = 6, description = "PATCH Update User", dependsOnMethods = "testCreateUser")
    public void testPatchUser() {
        LoggerUtil.logInfo("ReqRes: PATCH /users/" + userId + " - Changing job only");

        Map<String, Object> patchData = new HashMap<>();
        patchData.put("job", "Architect");

        given()
                .spec(reqSpec)
                .body(patchData)
                .when()
                .patch("/users/" + userId)
                .then()
                .log().ifError()
                .statusCode(200)
                .body("job", equalTo("Architect")); // Name should remain untouched ideally, but ReqRes echoes back

        LoggerUtil.logInfo("ReqRes: PATCH Update successful");
    }

    // ---------------------------------------------------------------
    // SCENARIO 7: DELETE USER
    // ---------------------------------------------------------------
    @Test(priority = 7, description = "DELETE User", dependsOnMethods = "testCreateUser")
    public void testDeleteUser() {
        LoggerUtil.logInfo("ReqRes: DELETE /users/" + userId);

        given()
                .spec(reqSpec)
                .when()
                .delete("/users/" + userId)
                .then()
                .log().ifError()
                .statusCode(204);

        LoggerUtil.logInfo("ReqRes: User deleted successfully");
    }

    // ---------------------------------------------------------------
    // SCENARIO 8: REGISTER SUCCESSFUL (Auth Flow)
    // ---------------------------------------------------------------
    @Test(priority = 8, description = "POST Register - Successful")
    public void testRegisterSuccessful() {
        LoggerUtil.logInfo("ReqRes: POST /register - Valid Registration");

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in"); // Hardcoded valid email for ReqRes
        credentials.put("password", "pistol");

        String token = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/register")
                .then()
                .log().ifError()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue())
                .extract().path("token");

        LoggerUtil.logInfo("ReqRes: Registration successful, Token: " + token);
    }
}