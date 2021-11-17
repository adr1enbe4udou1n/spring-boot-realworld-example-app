package io.okami101.realworld.tag;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class TagListTest extends RealworldApplicationTests {
    @Test
    public void can_list_all_tags() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/tags").then().statusCode(200);
    }
}
