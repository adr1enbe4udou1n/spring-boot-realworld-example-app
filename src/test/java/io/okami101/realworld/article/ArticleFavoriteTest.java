package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class ArticleFavoriteTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_favorite_article() {
        given().contentType(ContentType.JSON).when().post(baseUrl + "/api/articles/test-title/favorite").then()
                .statusCode(401);
    }

    @Test
    public void cannot_favorite_non_existent_article() {
    }

    @Test
    public void can_favorite_article() {
    }

    @Test
    public void can_unfavorite_article() {
    }
}
