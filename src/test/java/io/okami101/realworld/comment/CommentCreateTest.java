package io.okami101.realworld.comment;

import static io.restassured.RestAssured.given;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.article.NewComment;
import io.restassured.http.ContentType;

public class CommentCreateTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_create_comment() {
        given().contentType(ContentType.JSON).when().post(baseUrl + "/api/articles/test-title/comments").then()
                .statusCode(401);
    }

    @Test
    public void cannot_create_comment_to_non_existent_article() {
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void cannot_create_comment_with_invalid_data(NewComment comment) {
    }

    static Stream<NewComment> dataProvider() {
        return Stream.of(new NewComment(""));
    }

    @Test
    public void can_create_comment() {
    }
}
