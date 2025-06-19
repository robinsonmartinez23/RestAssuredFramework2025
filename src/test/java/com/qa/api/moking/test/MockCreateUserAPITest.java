package com.qa.api.moking.test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.moking.APIMocks;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class MockCreateUserAPITest extends BaseTest {
    @Test
    public void createAFakeUserTest() {

        APIMocks.defineCreateUserMock();

        String dummyUserJson = "{\n"
                + "    \"name\": \"tom\"\n"
                + "}";

        Response response = restClient.post(BASE_URL_MOCK_SERVER, "/api/users", dummyUserJson, null, null, AuthType.NO_AUTH, ContentType.JSON);

        response.then().assertThat().statusCode(201);
    }
}
