package io.okami101.realworld.application.article;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.utils.Tuple;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticlesService {

  @Autowired private ArticleRepository articles;

  @Autowired private TagRepository tags;

  @Autowired private SlugService slugService;

  @Autowired private EntityManager em;

  @Transactional(readOnly = true)
  public Optional<Article> findBySlug(String slug) {
    return articles.findBySlug(slug);
  }

  @Transactional(readOnly = true)
  public Tuple<ArrayList<ArticleDTO>, Long> list(
      int offset, int limit, String author, String tag, String favorited, User currentUser) {
    return filtredList(offset, limit, author, tag, favorited, false, currentUser);
  }

  @Transactional(readOnly = true)
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

    CriteriaQuery<Article> cq = cb.createQuery(Article.class);
    Root<Article> root = cq.from(Article.class);

    root.fetch("author", JoinType.LEFT).fetch("followers", JoinType.LEFT);
    root.fetch("favoritedBy", JoinType.LEFT);
    root.fetch("tags", JoinType.LEFT);

    CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
    Root<Article> countRoot = countCq.from(Article.class);
    countCq.select(cb.count(countRoot));

    var predicates = getPredicates(author, tag, favorited, following, currentUser, cb, root);
    var predicatesCount =
        getPredicates(author, tag, favorited, following, currentUser, cb, countRoot);

    if (!predicates.isEmpty()) {
      cq.where(predicates.toArray(new Predicate[0]));
    }

    if (!predicatesCount.isEmpty()) {
      countCq.where(predicatesCount.toArray(new Predicate[0]));
    }

    var articles =
        em.createQuery(cq.orderBy(cb.desc(root.get("id"))))
            .setFirstResult(offset)
            .setMaxResults(Math.min(limit, 20))
            .getResultList();

    var articlesCount = em.createQuery(countCq).getSingleResult();

    ArrayList<ArticleDTO> articleDTOs =
        articles.stream()
            .map(a -> new ArticleDTO(a, currentUser))
            .collect(Collectors.toCollection(ArrayList::new));

    return new Tuple<>(articleDTOs, articlesCount);
  }

  private List<Predicate> getPredicates(
      String author,
      String tag,
      String favorited,
      Boolean following,
      User currentUser,
      CriteriaBuilder cb,
      Root<Article> root) {
    List<Predicate> predicates = new ArrayList<>();

    if (author != null) {
      var join = root.join("author", JoinType.LEFT);
      predicates.add(cb.equal(join.get("name"), author));
    }

    if (tag != null) {
      var join = root.join("tags", JoinType.LEFT);
      predicates.add(cb.equal(join.get("name"), tag));
    }

    if (favorited != null) {
      var join = root.join("favoritedBy", JoinType.LEFT);
      predicates.add(cb.equal(join.get("name"), favorited));
    }

    if (following) {
      var join = root.join("author", JoinType.LEFT).join("followers", JoinType.LEFT);
      predicates.add(cb.equal(join.get("id"), currentUser.getId()));
    }

    return predicates;
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

  @Transactional
  public void delete(Article article, User currentUser) {
    if (!article.getAuthor().equals(currentUser)) {
      throw new ForbiddenException();
    }

    articles.delete(article);
  }

  @Transactional
  public ArticleDTO favorite(Article article, User currentUser) {
    article.getFavoritedBy().add(currentUser);
    return new ArticleDTO(articles.saveAndFlush(article), currentUser);
  }

  @Transactional
  public ArticleDTO unfavorite(Article article, User currentUser) {
    article.getFavoritedBy().remove(currentUser);
    return new ArticleDTO(articles.saveAndFlush(article), currentUser);
  }
}
