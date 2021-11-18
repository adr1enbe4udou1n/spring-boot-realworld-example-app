package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleFavoriteTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_favorite_article() {
        given().contentType(ContentType.JSON).when().post(baseUrl + "/api/articles/test-title/favorite").then()
                .statusCode(401);
    }

    @Test
    public void cannot_favorite_non_existent_article() {
        actingAsJohnUser().contentType(ContentType.JSON).when().post(baseUrl + "/api/articles/test-title/favorite")
                .then().statusCode(404);
    }

    @Test
    public void can_favorite_article() {
        User user = createJohnUser();
        createArticle(user);

        actingAs(user).contentType(ContentType.JSON).when().post(baseUrl + "/api/articles/test-title/favorite").then()
                .statusCode(200).body("article.title", equalTo("Test Title"))
                .body("article.slug", equalTo("test-title")).body("article.author.following", equalTo(false))
                .body("article.favorited", equalTo(true)).body("article.favoritesCount", equalTo(1));

        assertEquals(1, articleRepository.findBySlug("test-title").get().getFavoritedBy().size());
    }

    @Test
    public void can_unfavorite_article() {
        User user = createJohnUser();
        Article article = createArticle(user);

        article.getFavoritedBy().add(user);
        articleRepository.save(article);

        actingAs(user).contentType(ContentType.JSON).when().delete(baseUrl + "/api/articles/test-title/favorite").then()
                .statusCode(200).body("article.title", equalTo("Test Title"))
                .body("article.slug", equalTo("test-title")).body("article.author.following", equalTo(false))
                .body("article.favorited", equalTo(false)).body("article.favoritesCount", equalTo(0));

        assertEquals(0, articleRepository.findBySlug("test-title").get().getFavoritedBy().size());
    }
}
