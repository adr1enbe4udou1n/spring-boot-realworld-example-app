package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.article.NewArticle;
import io.restassured.http.ContentType;

public class ArticleCreateTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_create_article() {
        given().contentType(ContentType.JSON).when().post(baseUrl + "/api/articles").then().statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void cannot_create_article_with_invalid_data(NewArticle article) {
    }

    static Stream<NewArticle> dataProvider() {
        return Stream.of(new NewArticle("", "Test Description", "Test Body", null),
                new NewArticle("Test Title", "", "Test Body", null),
                new NewArticle("Test Title", "Test Description", "", null));
    }

    @Test
    public void cannot_create_article_with_same_title() {
    }

    @Test
    public void can_create_article() {
    }
}
