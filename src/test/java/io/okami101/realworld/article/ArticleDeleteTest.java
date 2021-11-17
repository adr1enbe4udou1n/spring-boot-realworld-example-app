package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class ArticleDeleteTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_delete_article() {
        given().contentType(ContentType.JSON).when().post(baseUrl + "/api/profiles/celeb_John Doe/follow").then()
                .statusCode(401);
    }

    @Test
    public void cannot_delete_non_existent_article() {
    }

    @Test
    public void cannot_delete_article_of_other_author() {
    }

    @Test
    public void can_delete_article_with_all_comments() {
    }
}
