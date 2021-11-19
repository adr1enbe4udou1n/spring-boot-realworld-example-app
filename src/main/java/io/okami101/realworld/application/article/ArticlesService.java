package io.okami101.realworld.application.article;

import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.utils.Tuple;

@Service
public class ArticlesService {

    @Autowired
    private ArticleRepository articles;

    @Autowired
    private TagRepository tags;

    @Autowired
    private SlugService slugService;

    @Autowired
    private EntityManager em;

    public Optional<Article> findBySlug(String slug) {
        return articles.findBySlug(slug);
    }

    public Tuple<ArrayList<ArticleDTO>, Integer> list(int offset, int limit, String tag, String favoritedBy,
            String author, User currentUser) {
        TypedQuery<Article> query = em.createQuery("from articles", Article.class);

        return paginator(offset, limit, currentUser, query);
    }

    public Tuple<ArrayList<ArticleDTO>, Integer> feed(int offset, int limit, User currentUser) {
        TypedQuery<Article> query = em.createQuery("from articles", Article.class);

        return paginator(offset, limit, currentUser, query);
    }

    private Tuple<ArrayList<ArticleDTO>, Integer> paginator(int offset, int limit, User currentUser,
            TypedQuery<Article> query) {
        return new Tuple<ArrayList<ArticleDTO>, Integer>(query.setFirstResult(offset).setMaxResults(limit)
                .getResultList().stream().map(a -> new ArticleDTO(a, currentUser))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll), query.getResultList().size());
    }

    @Transactional
    public ArticleDTO create(NewArticle articleDTO, User currentUser) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setSlug(slugService.generate(articleDTO.getTitle()));
        article.setDescription(articleDTO.getDescription());
        article.setBody(articleDTO.getBody());
        article.setAuthor(currentUser);

        articleDTO.getTagList().forEach(name -> {
            tags.findByName(name).ifPresentOrElse(t -> article.getTags().add(t),
                    () -> article.getTags().add(tags.save(new Tag(name))));
        });

        return new ArticleDTO(articles.save(article), currentUser);
    }

    @Transactional
    public ArticleDTO update(Article article, UpdateArticle articleDTO, User currentUser) {
        if (!article.getAuthor().equals(currentUser)) {
            throw new ForbiddenException();
        }

        Optional.ofNullable(articleDTO.getTitle()).ifPresent(article::setTitle);
        Optional.ofNullable(articleDTO.getDescription()).ifPresent(article::setDescription);
        Optional.ofNullable(articleDTO.getBody()).ifPresent(article::setBody);

        return new ArticleDTO(articles.save(article), currentUser);
    }

    public void delete(Article article, User currentUser) {
        if (!article.getAuthor().equals(currentUser)) {
            throw new ForbiddenException();
        }

        articles.delete(article);
    }

    public ArticleDTO favorite(Article article, User currentUser) {
        article.getFavoritedBy().add(currentUser);
        return new ArticleDTO(articles.save(article), currentUser);
    }

    public ArticleDTO unfavorite(Article article, User currentUser) {
        article.getFavoritedBy().remove(currentUser);
        return new ArticleDTO(articles.save(article), currentUser);
    }
}
