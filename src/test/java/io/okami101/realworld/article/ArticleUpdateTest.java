package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.article.UpdateArticle;
import io.restassured.http.ContentType;

public class ArticleUpdateTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_update_article() {
        given().contentType(ContentType.JSON).when().put(baseUrl + "/api/articles/test-title").then().statusCode(401);
    }

    @Test
    public void cannot_update_non_existent_article() {
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void cannot_update_article_with_invalid_data(UpdateArticle article) {
    }

    static Stream<UpdateArticle> dataProvider() {
        return Stream.of(new UpdateArticle("Test Title", "", ""));
    }

    @Test
    public void cannot_update_article_of_other_author() {
    }

    @Test
    public void can_update_own_article() {
    }
}
