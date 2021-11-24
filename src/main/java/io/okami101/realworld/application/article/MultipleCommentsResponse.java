package io.okami101.realworld.application.article;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class MultipleCommentsResponse {
  private List<CommentDTO> comments;

  public MultipleCommentsResponse(ArrayList<CommentDTO> comments) {
    this.comments = comments;
  }
}
