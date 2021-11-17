package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class ArticleGetTest extends RealworldApplicationTests {
    @Test
    public void cannot_get_non_existent_article() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/test-title").then().statusCode(200);
    }

    @Test
    public void can_get_article() {
    }
}
