package io.okami101.realworld.comment;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

public class CommentDeleteTest extends RealworldApplicationTests {
    @Test
    public void guest_cannot_delete_comment() {
        given().contentType(ContentType.JSON).when().delete(baseUrl + "/api/articles/test-title/comments/1").then()
                .statusCode(401);
    }

    @Test
    public void cannot_delete_comment_with_non_existent_article() {
        actingAsJohnUser().contentType(ContentType.JSON).when().delete(baseUrl + "/api/articles/test-title/comments/1")
                .then().statusCode(404);
    }

    @Test
    public void cannot_delete_non_existent_comment() {
        User user = createJohnUser();
        createArticle(user);

        actingAs(user).contentType(ContentType.JSON).when().delete(baseUrl + "/api/articles/test-title/comments/1")
                .then().statusCode(404);
    }

    @Test
    public void cannot_delete_comment_of_other_author() {
        User jane = createJaneUser();
        Article article = createArticle(jane);

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(jane);
        comment.setBody("Jane Comment");

        comment = commentRepository.save(comment);

        actingAsJohnUser().contentType(ContentType.JSON).when()
                .delete(baseUrl + "/api/articles/test-title/comments/" + comment.getId()).then().statusCode(403);
    }

    @Test
    public void cannot_delete_comment_with_bad_article() {
        User user = createJohnUser();
        createArticle(user);
        Article article = createArticleWithSlug(user, "other-article");

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(user);
        comment.setBody("John Comment");

        comment = commentRepository.save(comment);

        actingAs(user).contentType(ContentType.JSON).when()
                .delete(baseUrl + "/api/articles/test-title/comments/" + comment.getId()).then().statusCode(403);
    }

    @Test
    public void can_delete_all_comments_of_own_article() {
        User john = createJohnUser();
        User jane = createJaneUser();
        Article article = createArticle(john);

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(jane);
        comment.setBody("Jane Comment");

        comment = commentRepository.save(comment);

        actingAs(john).contentType(ContentType.JSON).when()
                .delete(baseUrl + "/api/articles/test-title/comments/" + comment.getId()).then().statusCode(200);

        assertEquals(0, commentRepository.count());
    }

    @Test
    public void can_delete_own_comment() {
        User user = createJohnUser();
        Article article = createArticle(user);

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(user);
        comment.setBody("John Comment");

        comment = commentRepository.save(comment);

        actingAs(user).contentType(ContentType.JSON).when()
                .delete(baseUrl + "/api/articles/test-title/comments/" + comment.getId()).then().statusCode(200);

        assertEquals(0, commentRepository.count());
    }
}
