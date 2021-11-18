package io.okami101.realworld.application.article;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.User;

@Service
public class ArticlesService {

    @Autowired
    private ArticleRepository articles;

    @Autowired
    private TagRepository tags;

    @Autowired
    private SlugService slugService;

    @Transactional
    public ArticleDTO create(NewArticle article, User currentUser) {
        Article newArticle = new Article();
        newArticle.setTitle(article.getTitle());
        newArticle.setSlug(slugService.generate(article.getTitle()));
        newArticle.setDescription(article.getDescription());
        newArticle.setBody(article.getBody());
        newArticle.setAuthor(currentUser);

        article.getTagList().forEach(name -> {
            tags.findByName(name).ifPresentOrElse(t -> newArticle.getTags().add(t),
                    () -> newArticle.getTags().add(tags.save(new Tag(name))));
        });

        return new ArticleDTO(articles.save(newArticle), currentUser);
    }
}
