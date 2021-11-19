package io.okami101.realworld.comment;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.user.User;
import io.restassured.http.ContentType;

import static org.hamcrest.core.IsEqual.equalTo;

public class CommentListTest extends RealworldApplicationTests {
    @Test
    public void cannot_list_all_comments_of_non_existent_article() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/test-title/comments").then()
                .statusCode(404);
    }

    @Test
    public void cannot_list_all_comments_of_article() {
        User user = createJohnUser();
        Article article = createArticle(user);

        // Create 10 comments
        for (int i = 1; i <= 10; i++) {
            Comment comment = new Comment();
            comment.setArticle(article);
            comment.setAuthor(user);
            comment.setBody("John Comment " + i);

            comment = commentRepository.save(comment);
        }

        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/articles/test-title/comments").then()
                .statusCode(200).body("comments.size()", equalTo(10))
                .body("comments[0].body", equalTo("John Comment 10"))
                .body("comments[0].author.username", equalTo("John Doe"))
                .body("comments[0].author.bio", equalTo("John Bio"))
                .body("comments[0].author.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
                .body("comments[0].author.following", equalTo(false));
    }
}
