package io.okami101.realworld.comment;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

public class CommentDeleteTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_delete_comment() {
        given().contentType(ContentType.JSON).when().delete(baseUrl + "/api/articles/test-title/comments/1").then()
                .statusCode(401);
    }

    @Test
    public void cannot_delete_comment_with_non_existent_article() {
    }

    @Test
    public void cannot_delete_non_existent_comment() {
    }

    @Test
    public void cannot_delete_comment_of_other_author() {
    }

    @Test
    public void cannot_delete_comment_with_bad_article() {
    }

    @Test
    public void can_delete_all_comments_of_own_article() {
    }

    @Test
    public void can_delete_own_comment() {
    }
}
