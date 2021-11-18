package io.okami101.realworld.application.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.article.CommentRepository;
import io.okami101.realworld.core.user.User;

@Service
public class CommentsService {

    @Autowired
    private CommentRepository comments;

    public CommentDTO create(NewComment commentDTO, Article article, User currentUser) {
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(currentUser);
        comment.setBody(commentDTO.getBody());

        return new CommentDTO(comments.save(comment), currentUser);
    }
}
