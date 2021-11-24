package io.okami101.realworld.application.article;

import lombok.Getter;

@Getter
public class SingleCommentResponse {
  private CommentDTO comment;

  public SingleCommentResponse(CommentDTO comment) {
    this.comment = comment;
  }
}
