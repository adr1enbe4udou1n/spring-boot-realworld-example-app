package io.okami101.realworld.application.article;

import java.util.List;
import lombok.Getter;

@Getter
public class MultipleCommentsResponse {
  private List<CommentDTO> comments;

  public MultipleCommentsResponse(List<CommentDTO> comments) {
    this.comments = comments;
  }
}
