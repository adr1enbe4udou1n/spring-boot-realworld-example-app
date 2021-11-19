package io.okami101.realworld.application.article;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class MultipleArticlesResponse {
    private List<ArticleDTO> articles;
    private Long articlesCount;

    public MultipleArticlesResponse(ArrayList<ArticleDTO> articles, Long count) {
        this.articles = articles;
        this.articlesCount = count;
    }
}
