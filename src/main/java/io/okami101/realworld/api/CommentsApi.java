package io.okami101.realworld.api;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.api.exception.ResourceNotFoundException;
import io.okami101.realworld.application.article.ArticlesService;
import io.okami101.realworld.application.article.CommentsService;
import io.okami101.realworld.application.article.MultipleCommentsResponse;
import io.okami101.realworld.application.article.NewCommentRequest;
import io.okami101.realworld.application.article.SingleCommentResponse;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Comments")
@RequestMapping(path = "/articles/{slug}/comments")
public class CommentsApi {

  @Autowired private ArticlesService articleService;

  @Autowired private CommentsService service;

  @GetMapping
  @Operation(
      operationId = "GetArticleComments",
      summary = "Get comments for an article",
      description = "Get the comments for an article. Auth is optional")
  @Parameter(name = "slug", description = "Slug of the article that you want to get comments for")
  public MultipleCommentsResponse list(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User currentUser) {
    return articleService
        .findBySlug(slug)
        .map(article -> new MultipleCommentsResponse(service.list(article, currentUser)))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @PostMapping
  @Operation(
      operationId = "CreateArticleComment",
      summary = "Create a comment for an article",
      description = "Create a comment for an article. Auth is required")
  @Parameter(
      name = "slug",
      description = "Slug of the article that you want to create a comment for")
  @SecurityRequirement(name = "Bearer")
  public SingleCommentResponse create(
      @PathVariable("slug") String slug,
      @Valid @RequestBody NewCommentRequest request,
      @AuthenticationPrincipal User currentUser) {
    return articleService
        .findBySlug(slug)
        .map(
            article ->
                new SingleCommentResponse(
                    service.create(request.getComment(), article, currentUser)))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DeleteMapping(path = "/{commentId}")
  @Operation(
      operationId = "DeleteArticleComment",
      summary = "Delete a comment for an article",
      description = "Delete a comment for an article. Auth is required")
  @Parameter(
      name = "slug",
      description = "Slug of the article that you want to delete a comment for")
  @Parameter(name = "commentId", description = "ID of the comment you want to delete")
  @SecurityRequirement(name = "Bearer")
  public void delete(
      @PathVariable("slug") String slug,
      @PathVariable("commentId") Long commentId,
      @AuthenticationPrincipal User currentUser) {
    articleService
        .findBySlug(slug)
        .ifPresentOrElse(
            article ->
                service
                    .findById(commentId)
                    .ifPresentOrElse(
                        comment -> {
                          if (!comment.getArticle().equals(article)) {
                            throw new ForbiddenException();
                          }
                          service.delete(comment, currentUser);
                        },
                        () -> {
                          throw new ResourceNotFoundException();
                        }),
            () -> {
              throw new ResourceNotFoundException();
            });
  }
}
