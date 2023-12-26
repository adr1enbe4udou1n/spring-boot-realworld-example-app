package io.okami101.realworld.application.article;

import java.util.List;
import lombok.Getter;

@Getter
public class MultipleArticlesResponse {
  private List<ArticleDTO> articles;
  private long articlesCount;

  public MultipleArticlesResponse(List<ArticleDTO> articles, long articlesCount) {
    this.articles = articles;
    this.articlesCount = articlesCount;
  }
}
