package io.okami101.realworld.article;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class ArticleDeleteTest extends RealworldApplicationTests {
  @Test
  public void guest_cannot_delete_article() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .delete(baseUrl + "/api/articles/test-title")
        .then()
        .statusCode(401);
  }

  @Test
  public void cannot_delete_non_existent_article() {
    actingAsJohnUser()
        .contentType(ContentType.JSON)
        .when()
        .delete(baseUrl + "/api/articles/test-title")
        .then()
        .statusCode(401);
  }

  @Test
  public void cannot_delete_article_of_other_author() {
    createArticle(createJaneUser());

    actingAsJohnUser()
        .contentType(ContentType.JSON)
        .when()
        .delete(baseUrl + "/api/articles/test-title")
        .then()
        .statusCode(401);
  }

  @Test
  public void can_delete_article_with_all_comments() {

    User john = createJohnUser();
    User jane = createJaneUser();
    Article article = createArticle(john);

    Comment comment = new Comment();
    comment.setArticle(article);
    comment.setAuthor(john);
    comment.setBody("John Comment");

    commentRepository.save(comment);

    comment = new Comment();
    comment.setArticle(article);
    comment.setAuthor(jane);
    comment.setBody("Jane Comment");

    commentRepository.save(comment);

    actingAs(john)
        .contentType(ContentType.JSON)
        .when()
        .delete(baseUrl + "/api/articles/test-title")
        .then()
        .statusCode(200);

    assertEquals(0, articleRepository.count());
    assertEquals(0, commentRepository.count());
  }
}
