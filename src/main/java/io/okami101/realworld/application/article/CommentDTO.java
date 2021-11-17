package io.okami101.realworld.application.article;

import java.time.Instant;

import io.okami101.realworld.application.user.ProfileDTO;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.user.User;
import lombok.Getter;

@Getter
public class CommentDTO {
    private Long id;
    private String body;
    private ProfileDTO author;
    private Instant createdAt;
    private Instant updatedAt;

    public CommentDTO(Comment comment, User currentUser) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.author = new ProfileDTO(comment.getAuthor(), currentUser);
    }
}
