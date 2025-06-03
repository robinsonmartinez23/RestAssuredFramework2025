package com.qa.api.gorest.tests;

import com.qa.api.base.BaseTest;
import com.qa.api.configmanager.ConfigManager;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.JsonUtils;
import com.qa.api.utils.StringUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetAUserWithDeserializationTest extends BaseTest {
    private String tokenId ;
    @BeforeClass
    public void setUpToken(){
        tokenId = "081368555efd9e49c46a729ba889822011be6ed2977146ecfcdd4d33e33ccf0e";
        ConfigManager.setProperty("bearertoken",tokenId);
    }
    @Test
    public void createAUserTest() {

        User user = new User(null,"Priyanka", StringUtils.getRandomEmailId(), "female", "active");

        Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertEquals(response.jsonPath().getString("name"), "Priyanka");
        Assert.assertNotNull(response.jsonPath().getString("id"));

        String userID = response.jsonPath().getString("id");
        System.out.println("user id ====>" + userID);

        // GET:
        // 2. GET: fetch the user using the same user id:
        Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT + "/" + userID, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
        Assert.assertTrue(responseGet.statusLine().contains("OK"));

        User userResponse = JsonUtils.deserialize(responseGet, User.class);

        Assert.assertEquals(userResponse.getName(), user.getName());


    }

}
