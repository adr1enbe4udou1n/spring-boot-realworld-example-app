package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.article.NewArticle;
import io.okami101.realworld.application.article.NewArticleRequest;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ArticleCreateTest extends RealworldApplicationTests {

  @Test
  public void guest_cannot_create_article() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .post(baseUrl + "/api/articles")
        .then()
        .statusCode(401);
  }

  @ParameterizedTest
  @MethodSource("dataProvider")
  public void cannot_create_article_with_invalid_data(NewArticle article) {
    actingAsJohnUser()
        .contentType(ContentType.JSON)
        .body(new NewArticleRequest(article))
        .when()
        .post(baseUrl + "/api/articles")
        .then()
        .statusCode(400);
  }

  static Stream<NewArticle> dataProvider() {
    return Stream.of(
        new NewArticle("", "Test Description", "Test Body", null),
        new NewArticle("Test Title", "", "Test Body", null),
        new NewArticle("Test Title", "Test Description", "", null));
  }

  @Test
  public void cannot_create_article_with_same_title() {
    User user = createJohnUser();
    createArticle(user);

    actingAs(user)
        .contentType(ContentType.JSON)
        .body(
            new NewArticleRequest(
                new NewArticle("Test Title", "Test Description", "Test Body", null)))
        .when()
        .post(baseUrl + "/api/articles")
        .then()
        .statusCode(400);
  }

  @Test
  public void can_create_article() {
    tagRepository.save(new Tag("Existing Tag"));

    actingAsJohnUser()
        .contentType(ContentType.JSON)
        .body(
            new NewArticleRequest(
                new NewArticle(
                    "Test Title",
                    "Test Description",
                    "Test Body",
                    Arrays.asList(new String[] {"Tag 1", "Tag 2", "Existing Tag"}))))
        .when()
        .post(baseUrl + "/api/articles")
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
        .body("article.favoritesCount", equalTo(0))
        .body(
            "article.tagList",
            equalTo(Arrays.asList(new String[] {"Existing Tag", "Tag 1", "Tag 2"})));

    Optional<Article> article = articleRepository.findBySlugWithTags("test-title");
    assertNotNull(article);
    assertEquals(3, article.get().getTags().size());
    assertEquals(3, tagRepository.count());
  }
}
