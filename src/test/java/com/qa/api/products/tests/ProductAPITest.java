package com.qa.api.products.tests;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.Product;
import com.qa.api.utils.JsonUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductAPITest extends BaseTest {
    @Test
    public void getProductsTest() {
        Response response = restClient.get(BASE_URL_PRODUCTS, PRODUCTS_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.ANY);
        Assert.assertEquals(response.statusCode(), 200);

        Product[] product = JsonUtils.deserialize(response, Product[].class);

        for (Product each : product) {
            System.out.println("id: " + each.getId());
            System.out.println("title: " + each.getTitle());
            System.out.println("price: " + each.getPrice());
            System.out.println("description: " + each.getDescription());
            System.out.println("image: " + each.getImage());
            System.out.println("category: " + each.getCategory());


            System.out.println("rate: " + each.getRating().getRate());
            System.out.println("count: " + each.getRating().getCount());

        }
    }
}
