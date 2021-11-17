package io.okami101.realworld.application.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.core.article.ArticleRepository;

@Service
public class ArticlesService {

    @Autowired
    private ArticleRepository articles;
}
