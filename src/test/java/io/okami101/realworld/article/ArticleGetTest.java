package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class ArticleGetTest extends RealworldApplicationTests {
  @Test
  public void cannot_get_non_existent_article() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .get(baseUrl + "/api/articles/test-title")
        .then()
        .statusCode(404);
  }

  @Test
  public void can_get_article() {
    createArticle(createJohnUser());

    given()
        .contentType(ContentType.JSON)
        .when()
        .get(baseUrl + "/api/articles/test-title")
        .then()
        .statusCode(200)
        .body("article.title", equalTo("Test Title"))
        .body("article.slug", equalTo("test-title"))
        .body("article.description", equalTo("Test Description"))
        .body("article.body", equalTo("Test Body"))
        .body("article.author.username", equalTo("John Doe"))
        .body("article.author.bio", equalTo("John Bio"))
        .body("article.author.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
        .body("article.author.following", equalTo(false))
        .body("article.favorited", equalTo(false))
        .body("article.favoritesCount", equalTo(0));
  }
}
