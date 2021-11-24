package io.okami101.realworld.application.article;

import lombok.Getter;

@Getter
public class SingleArticleResponse {
  private ArticleDTO article;

  public SingleArticleResponse(ArticleDTO article) {
    this.article = article;
  }
}
