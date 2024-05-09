package io.okami101.realworld.api;

import io.okami101.realworld.api.exception.ResourceNotFoundException;
import io.okami101.realworld.application.article.ArticlesService;
import io.okami101.realworld.application.article.SingleArticleResponse;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Favorites")
@RequestMapping(path = "/articles/{slug}/favorite")
public class FavoritesApi {

  private ArticlesService service;

  public FavoritesApi(ArticlesService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(
      operationId = "CreateArticleFavorite",
      summary = "Favorite an article",
      description = "Favorite an article. Auth is required")
  @Parameter(name = "slug", description = "Slug of the article that you want to favorite")
  @SecurityRequirement(name = "Bearer")
  public SingleArticleResponse favorite(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User currentUser) {
    return service
        .favorite(slug, currentUser)
        .map(SingleArticleResponse::new)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DeleteMapping
  @Operation(
      operationId = "DeleteArticleFavorite",
      summary = "Unfavorite an article",
      description = "Unfavorite an article. Auth is required")
  @Parameter(name = "slug", description = "Slug of the article that you want to unfavorite")
  @SecurityRequirement(name = "Bearer")
  public SingleArticleResponse unfavorite(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User currentUser) {
    return service
        .unfavorite(slug, currentUser)
        .map(SingleArticleResponse::new)
        .orElseThrow(ResourceNotFoundException::new);
  }
}
