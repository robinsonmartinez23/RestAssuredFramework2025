package com.qa.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;


public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /*
     *  e.g.,
     *  Class<User> x = User.class;
     *  I am creating a variable called x that will contain the programmatical representation of the class User
     *  Like:
     *  int n = 5;
     * --------------------------
     *  Generics Class<T> must be declared at the beginning as <T>
     *  T represents any class type and preserves its original type information. Object could be used, but it
     *  would require explicit casting and offers no type safety at compile time.
     */

    public static <T> T deserialize(Response response, Class<T> targetClass) {
        try {
            /*
             T deserializedObj = objectMapper.readValue(response.getBody().asString(), targetClass);
             return deserializedObj;
            */
            return objectMapper.readValue(response.getBody().asString(), targetClass);
        }
        catch(Exception e) {
            throw new RuntimeException("Deserialization failed..."+ targetClass.getName());
        }
    }
}
