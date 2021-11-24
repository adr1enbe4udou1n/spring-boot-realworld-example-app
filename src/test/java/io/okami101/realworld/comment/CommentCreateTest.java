package io.okami101.realworld.comment;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.application.article.NewComment;
import io.okami101.realworld.application.article.NewCommentRequest;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class CommentCreateTest extends RealworldApplicationTests {
  @Test
  public void guest_cannot_create_comment() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .post(baseUrl + "/api/articles/test-title/comments")
        .then()
        .statusCode(401);
  }

  @Test
  public void cannot_create_comment_to_non_existent_article() {
    actingAsJohnUser()
        .contentType(ContentType.JSON)
        .body(new NewCommentRequest(new NewComment("Test Comment")))
        .when()
        .post(baseUrl + "/api/articles/test-title/comments")
        .then()
        .statusCode(404);
  }

  @ParameterizedTest
  @MethodSource("dataProvider")
  public void cannot_create_comment_with_invalid_data(NewComment comment) {
    User user = createJohnUser();
    createArticle(user);

    actingAs(user)
        .contentType(ContentType.JSON)
        .body(new NewCommentRequest(comment))
        .when()
        .post(baseUrl + "/api/articles/test-title/comments")
        .then()
        .statusCode(400);
  }

  static Stream<NewComment> dataProvider() {
    return Stream.of(new NewComment(""));
  }

  @Test
  public void can_create_comment() {
    User user = createJohnUser();
    createArticle(user);

    actingAs(user)
        .contentType(ContentType.JSON)
        .body(new NewCommentRequest(new NewComment("Test Comment")))
        .when()
        .post(baseUrl + "/api/articles/test-title/comments")
        .then()
        .statusCode(200)
        .body("comment.body", equalTo("Test Comment"))
        .body("comment.author.username", equalTo("John Doe"))
        .body("comment.author.bio", equalTo("John Bio"))
        .body("comment.author.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
        .body("comment.author.following", equalTo(false));

    assertEquals(1, commentRepository.count());
  }
}
