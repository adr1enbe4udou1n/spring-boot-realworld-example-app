package io.okami101.realworld.application.article;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.utils.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticlesService {

  @Autowired private ArticleRepository articles;

  @Autowired private TagRepository tags;

  @Autowired private SlugService slugService;

  @Autowired private EntityManager em;

  public Optional<Article> findBySlug(String slug) {
    return articles.findBySlug(slug);
  }

  public Tuple<ArrayList<ArticleDTO>, Long> list(
      int offset, int limit, String author, String tag, String favorited, User currentUser) {
    return filtredList(offset, limit, author, tag, favorited, false, currentUser);
  }

  public Tuple<ArrayList<ArticleDTO>, Long> feed(int offset, int limit, User currentUser) {
    return filtredList(offset, limit, null, null, null, true, currentUser);
  }

  private Tuple<ArrayList<ArticleDTO>, Long> filtredList(
      int offset,
      int limit,
      String author,
      String tag,
      String favorited,
      Boolean following,
      User currentUser) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    Root<Article> root = query.from(Article.class);

    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Article> countRoot = countQuery.from(Article.class);

    filterQuery(query, root, author, tag, favorited, following, currentUser);
    filterQuery(countQuery, countRoot, author, tag, favorited, following, currentUser);

    List<Long> ids =
        em.createQuery(query.select(root.get("id")).orderBy(cb.desc(root.get("id"))))
            .setFirstResult(offset)
            .setMaxResults(Math.min(limit, 20))
            .getResultList();

    CriteriaQuery<Article> articleQuery = cb.createQuery(Article.class);
    Root<Article> articleRoot = articleQuery.from(Article.class);
    articleRoot.fetch("author", JoinType.LEFT).fetch("followers", JoinType.LEFT);
    articleRoot.fetch("tags", JoinType.LEFT);
    articleRoot.fetch("favoritedBy", JoinType.LEFT);
    articleQuery
        .where(articleRoot.get("id").in(ids))
        .orderBy(cb.desc(root.get("id")))
        .distinct(true);

    countQuery.select(cb.count(countRoot));

    return new Tuple<ArrayList<ArticleDTO>, Long>(
        em.createQuery(articleQuery).getResultList().stream()
            .map(a -> new ArticleDTO(a, currentUser))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
        em.createQuery(countQuery).getSingleResult());
  }

  private void filterQuery(
      CriteriaQuery<Long> query,
      Root<Article> root,
      String author,
      String tag,
      String favorited,
      Boolean following,
      User currentUser) {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    if (author != null) {
      Join<Article, User> join = root.join("author", JoinType.LEFT);
      query.where(cb.like(cb.lower(join.get("name")), "%" + author.toLowerCase() + "%"));
    }

    if (tag != null) {
      Join<Article, Tag> join = root.join("tags", JoinType.LEFT);
      query.where(cb.like(cb.lower(join.get("name")), "%" + tag.toLowerCase() + "%"));
    }

    if (favorited != null) {
      Join<Article, User> join = root.join("favoritedBy", JoinType.LEFT);
      query.where(cb.like(cb.lower(join.get("name")), "%" + favorited.toLowerCase() + "%"));
    }

    if (following) {
      Join<Article, User> join =
          root.join("author", JoinType.LEFT).join("followers", JoinType.LEFT);
      query.where(cb.equal(join.get("id"), currentUser.getId()));
    }
  }

  @Transactional
  public ArticleDTO create(NewArticle articleDTO, User currentUser) {
    Article article = new Article();
    article.setTitle(articleDTO.getTitle());
    article.setSlug(slugService.generate(articleDTO.getTitle()));
    article.setDescription(articleDTO.getDescription());
    article.setBody(articleDTO.getBody());
    article.setAuthor(currentUser);
    article.setTags(
        articleDTO.getTagList().stream()
            .map(
                name -> {
                  Optional<Tag> tag = tags.findByName(name);

                  if (tag.isPresent()) {
                    return tag.get();
                  }
                  return tags.save(new Tag(name));
                })
            .collect(Collectors.toSet()));

    return new ArticleDTO(articles.saveAndFlush(article), currentUser);
  }

  @Transactional
  public ArticleDTO update(Article article, UpdateArticle articleDTO, User currentUser) {
    if (!article.getAuthor().equals(currentUser)) {
      throw new ForbiddenException();
    }

    Optional.ofNullable(articleDTO.getTitle()).ifPresent(article::setTitle);
    Optional.ofNullable(articleDTO.getDescription()).ifPresent(article::setDescription);
    Optional.ofNullable(articleDTO.getBody()).ifPresent(article::setBody);

    return new ArticleDTO(articles.saveAndFlush(article), currentUser);
  }

  public void delete(Article article, User currentUser) {
    if (!article.getAuthor().equals(currentUser)) {
      throw new ForbiddenException();
    }

    articles.delete(article);
  }

  public ArticleDTO favorite(Article article, User currentUser) {
    article.getFavoritedBy().add(currentUser);
    return new ArticleDTO(articles.saveAndFlush(article), currentUser);
  }

  public ArticleDTO unfavorite(Article article, User currentUser) {
    article.getFavoritedBy().remove(currentUser);
    return new ArticleDTO(articles.saveAndFlush(article), currentUser);
  }
}
