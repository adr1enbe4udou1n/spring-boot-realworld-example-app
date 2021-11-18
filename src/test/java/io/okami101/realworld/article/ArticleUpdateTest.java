package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.article.UpdateArticle;
import io.okami101.realworld.application.article.UpdateArticleRequest;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

import static org.hamcrest.core.IsEqual.equalTo;

public class ArticleUpdateTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_update_article() {
        given().contentType(ContentType.JSON).when().put(baseUrl + "/api/articles/test-title").then().statusCode(401);
    }

    @Test
    public void cannot_update_non_existent_article() {
        actingAsJohnUser().contentType(ContentType.JSON).body(new UpdateArticleRequest(new UpdateArticle())).when()
                .put(baseUrl + "/api/articles/test-title").then().statusCode(404);
    }

    @Test
    public void cannot_update_article_of_other_author() {
        createArticle(createJaneUser());

        actingAsJohnUser().contentType(ContentType.JSON).body(new UpdateArticleRequest(new UpdateArticle())).when()
                .put(baseUrl + "/api/articles/test-title").then().statusCode(403);
    }

    @Test
    public void can_update_own_article() {
        User user = createJohnUser();
        createArticle(user);

        actingAs(user).contentType(ContentType.JSON)
                .body(new UpdateArticleRequest(new UpdateArticle("New Title", "New Description", "New Body"))).when()
                .put(baseUrl + "/api/articles/test-title").then().statusCode(200)
                .body("article.title", equalTo("New Title")).body("article.slug", equalTo("test-title"))
                .body("article.description", equalTo("New Description")).body("article.body", equalTo("New Body"));

        assertNotNull(articleRepository.findByTitle("New Title"));
    }
}
