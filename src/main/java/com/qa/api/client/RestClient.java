package com.qa.api.client;


import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.configmanager.ConfigManager;
import com.qa.api.constants.AuthType;
import com.qa.api.exceptions.APIException;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.File;
import java.util.Base64;
import java.util.Map;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class RestClient {
    // Define Response Specifications
    private final ResponseSpecification responseSpec200 = expect().statusCode(200);
    private final ResponseSpecification responseSpec201 = expect().statusCode(201);
    private final ResponseSpecification responseSpec204 = expect().statusCode(204);
    private final ResponseSpecification responseSpec400 = expect().statusCode(400);
    private final ResponseSpecification responseSpec404 = expect().statusCode(404);

    private final ResponseSpecification responseSpec200or201 = expect().statusCode(anyOf(equalTo(200),equalTo(201)));
    private final ResponseSpecification responseSpec200or404 = expect().statusCode(anyOf(equalTo(200),equalTo(404)));




    private RequestSpecification setup(String baseUrl, AuthType authType, ContentType contentType) {
        ChainTestListener.log("API base url : "+ baseUrl);   // ChainTestListener log
        ChainTestListener.log("Auth Type : "+ authType.toString());   // ChainTestListener log
        RequestSpecification request = RestAssured.given().log().all()
                .baseUri(baseUrl)
                .contentType(contentType)
                .accept(contentType);

        switch (authType){
            case BEARER_TOKEN:
                request.header("Authorization", "Bearer "+ ConfigManager.getProperty("bearertoken"));
                break;
            case BASIC_AUTH:
                request.header("Authorization", "Basic " + generateBasicAuthToken());
                break;
            case API_KEY:
                request.header("x-api-key", ConfigManager.getProperty("apikey"));
                break;
            case NO_AUTH:
                System.out.println("Auth is not required");
                break;
            default:
                System.out.println("This AuthType is not supported, please check the AuthType enum");
                throw new APIException("===Invalid AuthType===");
        }
        return request;
    }

    private String generateBasicAuthToken() {
        String credentials = ConfigManager.getProperty("basicauthusername") + ":" + ConfigManager.getProperty("basicauthpassword");
        //admin:admin --> "YWRtaW46YWRtaW4=" (base64 encoded value)
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes());
        System.out.println("It is the encoded basic auth token ---> "+ basicAuth);
        return basicAuth;
    }

    private void applyParams(RequestSpecification request, Map<String, String> queryParams, Map<String,String> pathParams){
        ChainTestListener.log("Query Params : "+ queryParams); // ChainTestListener log
        ChainTestListener.log("Path Params : "+ pathParams); // ChainTestListener log
        if(queryParams != null){
            request.queryParams(queryParams);
        }
        if(pathParams != null){
            request.pathParams(pathParams);
        }
    }

    //***********Get response from the API***************

    /**
     * Sends a GET request to a specified endpoint with given parameters and authentication type.
     *
     * @param baseUrl      The base URL for the API.
     * @param endPoint     The specific endpoint to which the GET request will be sent.
     * @param queryParams  A map of query parameters to be sent with the request.
     * @param pathParams   A map of path parameters to be sent with the request.
     * @param authType     The type of authentication to be used for the request.
     * @param contentType  The content type for the request.
     * @return The response received from the API call.
     */
    @Step("Calling GET api with base url: {0}")
    public Response get(String baseUrl, String endPoint,
                        Map<String, String> queryParams,
                        Map<String,String> pathParams,
                        AuthType authType,
                        ContentType contentType){
        RequestSpecification request = setup(baseUrl, authType, contentType);
        applyParams(request, queryParams, pathParams);
        Response response = request.get(endPoint).then().spec(responseSpec200or404).extract().response();
        response.prettyPrint();
        return response;
    }

    //****************Post*****************

    /**
     * Sends a POST request to a specified endpoint with the given parameters, request body, and authentication type.
     *
     * @param <T>          The type of the request body (file, pojo, etc.).
     * @param baseUrl      The base URL for the API.
     * @param endPoint     The specific endpoint to which the POST request will be sent.
     * @param body         The body of the request.
     * @param queryParams  A map of query parameters to be included in the request.
     * @param pathParams   A map of path parameters to be included in the request.
     * @param authType     The authentication type to be used for the request.
     * @param contentType  The content type of the request.
     * @return The response received from the API call.
     */
    public <T> Response post(String baseUrl, String endPoint,
                             T body,
                             Map<String, String> queryParams,
                             Map<String,String> pathParams,
                             AuthType authType,
                             ContentType contentType){
        RequestSpecification request = setup(baseUrl, authType, contentType);
        applyParams(request, queryParams, pathParams);
        Response response = request.body(body).post(endPoint).then().spec(responseSpec200or201).extract().response();
        response.prettyPrint();
        return response;
    }

    /**
     * Sends a POST request to a specified endpoint with the given file as the request body,
     * along with optional query parameters, path parameters, and authentication type.
     *
     * @param baseUrl      The base URL for the API.
     * @param endPoint     The specific endpoint to which the POST request will be sent.
     * @param file         The file to be sent as the request body.
     * @param queryParams  A map of query parameters to be included in the request.
     * @param pathParams   A map of path parameters to be included in the request.
     * @param authType     The authentication type to be used for the request.
     * @param contentType  The content type of the request.
     * @return The response received from the API call.
     */
    public Response post(String baseUrl, String endPoint,
                         File file,
                         Map<String, String> queryParams,
                         Map<String,String> pathParams,
                         AuthType authType,
                         ContentType contentType){
        RequestSpecification request = setup(baseUrl, authType, contentType);
        applyParams(request, queryParams, pathParams);
        Response response = request.body(file).post(endPoint).then().spec(responseSpec200or201).extract().response();
        response.prettyPrint();
        return response;
    }

    /**
     * Sends a POST request to the specified API endpoint using client credentials and
     * a grant type typically, for OAuth2 authentication. The request includes form parameters
     * and a defined content type.
     *
     * @param baseUrl      The base URL for the API.
     * @param endPoint     The specific endpoint to which the POST request is sent.
     * @param clientId     The client ID required for the request authentication.
     * @param clientSecret The client secret required for the request authentication.
     * @param grantType    The type of grant (e.g., client credentials) used in the request.
     * @param contentType  The content type of the request (e.g., application/json).
     * @return The response received from the API call encapsulated in a Response object.
     */
    public Response post(String baseUrl, String endPoint,
                             String clientId, String clientSecret, String grantType,
                             ContentType contentType){
        Response response = RestAssured.given()
                .contentType(contentType)
                .formParam("grant_type", grantType)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(baseUrl+endPoint);
        response.prettyPrint();
        return response;

    }

    //***************PUT*****************

    /**
     * Sends a PUT request to a specified endpoint with the given parameters, request body, and authentication type.
     *
     * @param <T>          The type of the request body (e.g., POJO, JSON, etc.).
     * @param baseUrl      The base URL for the API.
     * @param endPoint     The specific endpoint to which the PUT request will be sent.
     * @param body         The body of the PUT request.
     * @param queryParams  A map of query parameters to be included in the request.
     * @param pathParams   A map of path parameters to be included in the request.
     * @param authType     The authentication type to be used for the request.
     * @param contentType  The content type of the request.
     * @return The response received from the API call.
     */
    public <T> Response put(String baseUrl, String endPoint,
                            T body,
                            Map<String, String> queryParams,
                            Map<String,String> pathParams,
                            AuthType authType,
                            ContentType contentType){
        RequestSpecification request = setup(baseUrl, authType, contentType);
        applyParams(request, queryParams, pathParams);
        Response response = request.body(body).put(endPoint).then().spec(responseSpec200).extract().response();
        response.prettyPrint();
        return response;
    }

    //*******************PATCH*******************

    /**
     * Sends a PATCH request to a specified endpoint with the given parameters, request body,
     * and authentication type.
     *
     * @param <T>          The type of the request body (e.g., POJO, JSON, etc.).
     * @param baseUrl      The base URL for the API.
     * @param endPoint     The specific endpoint to which the PATCH request will be sent.
     * @param body         The body of the PATCH request.
     * @param queryParams  A map of query parameters to be included in the request.
     * @param pathParams   A map of path parameters to be included in the request.
     * @param authType     The authentication type to be used for the request.
     * @param contentType  The content type of the request.
     * @return The response received from the API call.
     */
    public <T> Response patch(String baseUrl, String endPoint,
                              T body,
                              Map<String, String> queryParams,
                              Map<String,String> pathParams,
                              AuthType authType,
                              ContentType contentType){
        RequestSpecification request = setup(baseUrl, authType, contentType);
        applyParams(request, queryParams, pathParams);
        Response response = request.body(body).patch(endPoint).then().spec(responseSpec200).extract().response();
        response.prettyPrint();
        return response;
    }

    //********************DELETE*********************

    public Response delete(String baseUrl, String endPoint,
                           Map<String, String> queryParams,
                           Map<String,String> pathParams,
                           AuthType authType,
                           ContentType contentType){
        RequestSpecification request = setup(baseUrl, authType, contentType);
        applyParams(request, queryParams, pathParams);
        Response response = request.delete(endPoint).then().spec(responseSpec204).extract().response();
        response.prettyPrint();
        return response;
    }
}
