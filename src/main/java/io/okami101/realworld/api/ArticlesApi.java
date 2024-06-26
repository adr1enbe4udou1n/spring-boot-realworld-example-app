package io.okami101.realworld.api;

import io.okami101.realworld.api.exception.ResourceNotFoundException;
import io.okami101.realworld.application.article.ArticlesService;
import io.okami101.realworld.application.article.MultipleArticlesResponse;
import io.okami101.realworld.application.article.NewArticleRequest;
import io.okami101.realworld.application.article.SingleArticleResponse;
import io.okami101.realworld.application.article.UpdateArticleRequest;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Articles")
@RequestMapping(path = "/articles")
public class ArticlesApi {

  private ArticlesService service;

  public ArticlesApi(ArticlesService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(
      operationId = "GetArticles",
      summary = "Get recent articles globally",
      description =
          "Get most recent articles globally. Use query parameters to filter results. Auth is optional")
  @Parameter(name = "limit", description = "Limit number of articles returned (default is 20)")
  @Parameter(name = "offset", description = "Offset/skip number of articles (default is 0)")
  @Parameter(name = "author", description = "Filter by author (username)")
  @Parameter(name = "tag", description = "Filter by tag")
  @Parameter(name = "favorited", description = "Filter by favorites of a user (username)")
  public MultipleArticlesResponse list(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "20") int limit,
      @RequestParam(value = "author", required = false) String author,
      @RequestParam(value = "tag", required = false) String tag,
      @RequestParam(value = "favorited", required = false) String favoritedBy,
      @AuthenticationPrincipal User currentUser) {
    var result = service.list(author, tag, favoritedBy, currentUser, offset, limit);
    return new MultipleArticlesResponse(result.getFirst(), result.getSecond());
  }

  @GetMapping(path = "/feed")
  @Operation(
      operationId = "GetArticlesFeed",
      summary = "Get recent articles from users you follow",
      description =
          "Get most recent articles from users you follow. Use query parameters to limit. Auth is required")
  @Parameter(name = "limit", description = "Limit number of articles returned (default is 20)")
  @Parameter(name = "offset", description = "Offset/skip number of articles (default is 0)")
  @SecurityRequirement(name = "Bearer")
  public MultipleArticlesResponse feed(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "20") int limit,
      @AuthenticationPrincipal User currentUser) {
    var result = service.feed(currentUser, offset, limit);
    return new MultipleArticlesResponse(result.getFirst(), result.getSecond());
  }

  @GetMapping(path = "/{slug}")
  @Operation(
      operationId = "GetArticle",
      summary = "Get an article",
      description = "Get an article. Auth not required")
  @Parameter(name = "slug", description = "Slug of the article to get")
  public SingleArticleResponse get(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User currentUser) {
    return service
        .get(slug, currentUser)
        .map(SingleArticleResponse::new)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @PostMapping
  @Operation(
      operationId = "CreateArticle",
      summary = "Create an article",
      description = "Create an article. Auth is required")
  @SecurityRequirement(name = "Bearer")
  public SingleArticleResponse create(
      @Valid @RequestBody NewArticleRequest request, @AuthenticationPrincipal User currentUser) {
    return new SingleArticleResponse(service.create(request.getArticle(), currentUser));
  }

  @PutMapping(path = "/{slug}")
  @Operation(
      operationId = "UpdateArticle",
      summary = "Update an article",
      description = "Update an article. Auth is required")
  @Parameter(name = "slug", description = "Slug of the article to update")
  @SecurityRequirement(name = "Bearer")
  public SingleArticleResponse update(
      @Valid @RequestBody UpdateArticleRequest request,
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User currentUser) {
    return service
        .findBySlug(slug)
        .map(
            article ->
                new SingleArticleResponse(
                    service.update(article, request.getArticle(), currentUser)))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DeleteMapping(path = "/{slug}")
  @Operation(
      operationId = "DeleteArticle",
      summary = "Delete an article",
      description = "Delete an article. Auth is required")
  @Parameter(name = "slug", description = "Slug of the article to delete")
  @SecurityRequirement(name = "Bearer")
  public void delete(@PathVariable("slug") String slug, @AuthenticationPrincipal User currentUser) {
    service
        .findBySlug(slug)
        .ifPresentOrElse(
            article -> service.delete(article, currentUser),
            () -> {
              throw new ResourceNotFoundException();
            });
  }
}
