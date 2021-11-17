package io.okami101.realworld.comment;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class CommentListTest extends RealworldApplicationTests {
    @Test
    public void cannot_list_all_comments_of_non_existent_article() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/test-title/comments").then()
                .statusCode(200);
    }

    @Test
    public void cannot_list_all_comments_of_article() {
    }
}
