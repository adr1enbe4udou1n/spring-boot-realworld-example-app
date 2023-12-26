package io.okami101.realworld.application.article;

import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.user.User;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class MultipleArticlesResponse {
  private List<ArticleDTO> articles;
  private long articlesCount;

  public MultipleArticlesResponse(Page<Article> articles, User currentUser) {
    this.articles = articles.map(a -> new ArticleDTO(a, currentUser)).getContent();
    this.articlesCount = articles.getTotalElements();
  }
}
