package com.qa.api.base;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.client.RestClient;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

@Listeners(ChainTestListener.class)
public class BaseTest {

    protected RestClient restClient;

    //***********API Base URLs***********//
    protected static final String BASE_URL_GOREST= "https://gorest.co.in";
    protected static final String BASE_URL_CONTACTS= "https://thinking-tester-contact-list.herokuapp.com";
    protected static final String BASE_URL_REQRES = "https://reqres.in";
    protected static final String BASE_URL_BASIC_AUTH = "https://the-internet.herokuapp.com";
    protected static final String BASE_URL_PRODUCTS = "https://fakestoreapi.com";
    protected static final String BASE_URL_OAUTH2_AMADEUS = "https://test.api.amadeus.com";
    protected final static String BASE_URL_ERGAST_CIRCUIT = "http://ergast.com";

    //***********API Endpoints***********//
    protected static final String GOREST_USERS_ENDPOINT = "/public/v2/users";
    protected static final String CONTACTS_LOGIN_ENDPOINT = "/users/login";
    protected static final String CONTACTS_ENDPOINT = "/contacts";
    protected static final String REQRES_ENDPOINT = "/api/users";
    protected static final String BASIC_AUTH_ENDPOINT = "/basic_auth";
    protected static final String PRODUCTS_ENDPOINT = "/products";
    protected static final String AMADEUS_OAUTH2_TOKEN_ENDPOINT = "/v1/security/oauth2/token";
    protected static final String AMADEUS_FLIGHT_DEST_ENDPOINT = "/v1/shopping/flight-destinations";
    protected final static String ERGAST_CIRCUIT_ENDPOINT = "/api/f1/2017/circuits.xml";

    @BeforeSuite
    public void setupAllureReport(){
        RestAssured.filters((new AllureRestAssured()));
    }

    @BeforeTest
    public void setup(){
       restClient = new RestClient();
    }
}
