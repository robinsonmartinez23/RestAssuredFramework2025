package com.qa.api.gorest.tests;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.base.BaseTest;

import com.qa.api.configmanager.ConfigManager;
import com.qa.api.constants.AuthType;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;


@Epic("Epic 100: Go Rest Get User API Feature")
@Story("US 100: Feature go rest api - Get user api")
public class GetUserTest extends BaseTest {

    @Description("Getting all the users")
    @Owner("Robinson Martinez")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void getAllUsersTest(){
        ChainTestListener.log("Getting all users api test"); // ChainTestListener log
        Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT,null,null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(response.statusLine().contains("OK"));
    }
    @Description("Getting all the users with Query Params")
    @Owner("Robinson Martinez")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void getAllUsersWithQueryParamTest(){

        // Map<String, String> queryParams = new HashMap<>();
        // queryParams.put("name", "naveen");
        // queryParams.put("status", "active");

        // Java 9+
        Map<String, String> queryParams = Map.of(
                "name", "naveen",
                "status", "active"
        );
        Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT,queryParams,null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(response.statusLine().contains("OK"));
    }

    @Test (enabled = false)
    public void getSingleUserTest(){
        String userId = "7908895"; // Valid or existing userId
        Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId,null,null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(response.statusLine().contains("OK"));
        Assert.assertEquals(response.jsonPath().getString("id"), userId );
    }

}
