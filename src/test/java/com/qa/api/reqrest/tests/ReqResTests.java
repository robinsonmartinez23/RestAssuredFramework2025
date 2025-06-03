package com.qa.api.reqrest.tests;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

@Epic("Epic 100: ReqRes Get User API Feature")
@Story("US 300: Feature ReqRes api - get user")
public class ReqResTests extends BaseTest {
    @Test
    public void getUserTest(){
        Map<String,String> queryParams = new java.util.HashMap<>();
        queryParams.put("page", "2");
        Response response = restClient.get(BASE_URL_REQRES,REQRES_ENDPOINT,queryParams,null, AuthType.NO_AUTH, ContentType.ANY);
        Assert.assertEquals(response.statusCode(), 200);
    }
}
