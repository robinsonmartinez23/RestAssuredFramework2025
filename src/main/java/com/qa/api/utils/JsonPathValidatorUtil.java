package com.qa.api.utils;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import io.restassured.response.Response;

public class JsonPathValidatorUtil {


    /**
     * Returns the JSON response as a string.
     *
     * @param response the HTTP response containing the JSON data
     * @return the JSON response as a string
     */
    private static String getJsonResponseAsString(Response response) {
        return response.getBody().asString();
    }

    /**
     * Reads a value of a specified type from a JSON response using a given JSONPath expression.
     *
     * @param <T>      the type of the value to be read from the JSON response
     * @param response the HTTP response containing the JSON data
     * @param jsonPath the JSONPath expression used to extract the value
     * @return the value of type T extracted from the JSON response based on the JSONPath
     */
    public static <T> T read(Response response, String jsonPath) { // $.id -- 123
        ReadContext ctx = JsonPath.parse(getJsonResponseAsString(response));
        return ctx.read(jsonPath);
    }

    /**
     * Reads a list of elements from a JSON response based on the specified JSON path.
     *
     * @param <T>        the type of the elements in the returned list
     * @param response   the HTTP response containing the JSON data
     * @param jsonPath   the JSONPath expression used to extract the list of elements
     * @return a list of elements of type T extracted from the JSON response
     */
    public static <T> List<T> readList(Response response, String jsonPath) { // $.id -- 123
        ReadContext ctx = JsonPath.parse(getJsonResponseAsString(response));
        return ctx.read(jsonPath);
    }

    /**
     * Reads a list of maps from a JSON response based on the specified JSON path.
     *
     * @param <T>        the type of the elements in the returned list
     * @param response   the HTTP response containing the JSON data
     * @param jsonPath   the JSONPath expression used to extract the list of maps
     * @return a list of maps extracted from the JSON response
     */
    public static <T> List<Map<String, T>> readListOfMaps(Response response, String jsonPath) { // $.id -- 123
        ReadContext ctx = JsonPath.parse(getJsonResponseAsString(response));
        return ctx.read(jsonPath);
    }
}
