package io.okami101.realworld.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.application.article.ArticlesService;
import io.okami101.realworld.application.article.SingleArticleResponse;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Favorites")
@RequestMapping(path = "/articles/{slug}/favorite")
public class FavoritesApi {

    @Autowired
    private ArticlesService service;

    @PostMapping
    @Operation(summary = "Favorite an article", description = "Favorite an article. Auth is required")
    @Parameter(name = "slug", description = "Slug of the article that you want to favorite")
    @SecurityRequirement(name = "Bearer")
    public SingleArticleResponse favorite(@PathVariable("slug") String slug,
            @AuthenticationPrincipal User currentUser) {
        return new SingleArticleResponse(null);
    }

    @DeleteMapping
    @Operation(summary = "Unfavorite an article", description = "Unfavorite an article. Auth is required")
    @Parameter(name = "slug", description = "Slug of the article that you want to unfavorite")
    @SecurityRequirement(name = "Bearer")
    public SingleArticleResponse unfavorite(@PathVariable("slug") String slug,
            @AuthenticationPrincipal User currentUser) {
        return new SingleArticleResponse(null);
    }
}
