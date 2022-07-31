package io.okami101.realworld.application.article;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Comment;
import io.okami101.realworld.core.article.CommentRepository;
import io.okami101.realworld.core.user.User;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentsService {

  @Autowired private CommentRepository comments;

  @Transactional(readOnly = true)
  public Optional<Comment> findById(Long id) {
    return comments.findById(id);
  }

  @Transactional(readOnly = true)
  public ArrayList<CommentDTO> list(Article article, User currentUser) {
    return comments.findAllByArticleOrderByIdDesc(article).stream()
        .map(c -> new CommentDTO(c, currentUser))
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  @Transactional
  public CommentDTO create(NewComment commentDTO, Article article, User currentUser) {
    Comment comment = new Comment();
    comment.setArticle(article);
    comment.setAuthor(currentUser);
    comment.setBody(commentDTO.getBody());

    return new CommentDTO(comments.saveAndFlush(comment), currentUser);
  }

  @Transactional
  public void delete(Comment comment, User currentUser) {
    if (!comment.getAuthor().equals(currentUser)
        && !comment.getArticle().getAuthor().equals(currentUser)) {
      throw new ForbiddenException();
    }

    comments.delete(comment);
  }
}
