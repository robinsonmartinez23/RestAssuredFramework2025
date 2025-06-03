package com.qa.api.gorest.tests;

import com.qa.api.base.BaseTest;
import com.qa.api.configmanager.ConfigManager;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@Epic("Epic 100: Go Rest Get User API Feature")
@Story("US 102: Feature go rest api - Update user api")
public class UpdateUserTest extends BaseTest {

    private String tokenId ;
    @BeforeClass
    public void setUpToken(){
        tokenId = "081368555efd9e49c46a729ba889822011be6ed2977146ecfcdd4d33e33ccf0e";
        ConfigManager.setProperty("bearertoken",tokenId);
    }

    @Test
    public void updateAUserTest() {
        // 1. Create a user - POST

        //*** Using @NoArgsConstructor ***

        // User user = new User();
        // user.setName("John");
        // user.setEmail(StringUtils.getRandomEmailId();
        // user.setGender("male");
        // user.setStatus("active");

        //*** Using @AllArgsConstructor ***

        // User user = new User ("John", StringUtils.getRandomEmailId(), "male", "active");

        //*** Using Builder Pattern @Builder ***

        User user = User.builder()
                .name("Robin")
                .email(StringUtils.getRandomEmailId())
                .status("inactive")
                .gender("male")
                .build();
        Response responsePost = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertEquals(responsePost.jsonPath().getString("name"), "Robin");
        Assert.assertNotNull(responsePost.jsonPath().getString("id"));

        // Fetch the userId from the response
        String userId = responsePost.jsonPath().getString("id");
        System.out.println("user id ====>" +  userId);

        //2. GET: fetch the user using the same user id:
        Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(responseGet.statusLine().contains("OK"));
        Assert.assertEquals(responseGet.jsonPath().getString("id"), userId);

        //3. Update the user using the same user id:
        user.setName("Robinson Martinez");
        user.setStatus("active");
        Response responsePUT = restClient.put(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(responsePUT.statusLine().contains("OK"));
        Assert.assertEquals(responsePUT.jsonPath().getString("id"), userId);
        Assert.assertEquals(responsePUT.jsonPath().getString("name"), "Robinson Martinez");
        Assert.assertEquals(responsePUT.jsonPath().getString("status"), "active");

        //4. GET: fetch the user using the same user id:
        responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(responseGet.statusLine().contains("OK"));
        Assert.assertEquals(responseGet.jsonPath().getString("id"), userId);
        Assert.assertEquals(responseGet.jsonPath().getString("name"), "Robinson Martinez");
        Assert.assertEquals(responseGet.jsonPath().getString("status"), "active");

    }
}
