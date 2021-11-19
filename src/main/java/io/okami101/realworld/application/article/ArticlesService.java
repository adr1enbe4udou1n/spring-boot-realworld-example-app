package io.okami101.realworld.application.article;

import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
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

    public Tuple<ArrayList<ArticleDTO>, Long> list(int offset, int limit, String author, String tag, String favorited,
            User currentUser) {
        return filtredList(offset, limit, author, tag, favorited, false, currentUser);
    }

    public Tuple<ArrayList<ArticleDTO>, Long> feed(int offset, int limit, User currentUser) {
        return filtredList(offset, limit, null, null, null, true, currentUser);
    }

    private Tuple<ArrayList<ArticleDTO>, Long> filtredList(int offset, int limit, String author, String tag,
            String favorited, Boolean following, User currentUser) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Article> query = cb.createQuery(Article.class);
        Root<Article> root = query.from(Article.class);

        Subquery<Long> subQuery = query.subquery(Long.class);
        Root<Article> rootSub = subQuery.from(Article.class);
        subQuery.select(rootSub.get("id"));

        if (author != null) {
            Join<Article, User> join = rootSub.join("author", JoinType.LEFT);
            subQuery.where(cb.like(cb.lower(join.get("name")), "%" + author.toLowerCase() + "%"));
        }

        if (tag != null) {
            Join<Article, Tag> join = rootSub.join("tags", JoinType.LEFT);
            subQuery.where(cb.like(cb.lower(join.get("name")), "%" + tag.toLowerCase() + "%"));
        }

        if (favorited != null) {
            Join<Article, User> join = rootSub.join("favoritedBy", JoinType.LEFT);
            subQuery.where(cb.like(cb.lower(join.get("name")), "%" + favorited.toLowerCase() + "%"));
        }

        if (following) {
            Join<Article, User> join = rootSub.join("author", JoinType.LEFT).join("followers", JoinType.LEFT);
            subQuery.where(cb.equal(join.get("id"), currentUser.getId()));
        }

        query.select(root).where(cb.in(root.get("id")).value(subQuery)).orderBy(cb.desc(root.get("id")));

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Article.class))).where(cb.in(root.get("id")).value(subQuery));

        return new Tuple<ArrayList<ArticleDTO>, Long>(em.createQuery(query).setFirstResult(offset)
                .setMaxResults(Math.min(limit, 20)).getResultList().stream().map(a -> new ArticleDTO(a, currentUser))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
                em.createQuery(countQuery).getSingleResult());
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
