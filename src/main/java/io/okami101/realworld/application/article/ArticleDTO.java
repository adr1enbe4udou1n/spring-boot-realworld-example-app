package io.okami101.realworld.application.article;

import io.okami101.realworld.application.user.ProfileDTO;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.user.User;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ArticleDTO {
  private String title;
  private String slug;
  private String description;
  private String body;
  private ProfileDTO author;
  private List<String> tagList;
  private Instant createdAt;
  private Instant updatedAt;
  private Boolean favorited;
  private int favoritesCount;

  public ArticleDTO(Article article, User currentUser) {
    this.title = article.getTitle();
    this.slug = article.getSlug();
    this.description = article.getDescription();
    this.body = article.getBody();
    this.tagList =
        article.getTags().stream().map(t -> t.getName()).sorted().collect(Collectors.toList());
    this.createdAt = article.getCreatedAt();
    this.updatedAt = article.getUpdatedAt();
    this.author = new ProfileDTO(article.getAuthor(), currentUser);
    this.favorited = article.getFavoritedBy().contains(currentUser);
    this.favoritesCount = article.getFavoritedBy().size();
  }
}
