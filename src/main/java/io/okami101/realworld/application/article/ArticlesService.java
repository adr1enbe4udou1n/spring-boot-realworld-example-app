package io.okami101.realworld.application.article;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import io.okami101.realworld.utils.Tuple;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticlesService {

  private final ArticleRepository articles;
  private final TagRepository tags;
  private final UserRepository users;
  private final SlugService slugService;

  public ArticlesService(
      ArticleRepository articles,
      TagRepository tags,
      UserRepository users,
      SlugService slugService) {
    this.articles = articles;
    this.tags = tags;
    this.users = users;
    this.slugService = slugService;
  }

  @Transactional(readOnly = true)
  public Optional<Article> findBySlug(String slug) {
    return articles.findBySlug(slug);
  }

  @Transactional(readOnly = true)
  public Optional<ArticleDTO> get(String slug, User currentUser) {
    return findBySlug(slug).map(a -> new ArticleDTO(a, currentUser));
  }

  @Transactional(readOnly = true)
  public Tuple<List<ArticleDTO>, Long> list(
      String author, String tag, String favorited, User currentUser, int offset, int limit) {

    if (author != null) {
      return getPaginatedResponse(
          articles.findAllByAuthorNameOrderByIdDesc(author, PageRequest.of(offset / limit, limit)),
          currentUser);
    }

    if (tag != null) {
      return getPaginatedResponse(
          articles.findAllByTagsOrderByIdDesc(
              tags.findByName(tag).get(), PageRequest.of(offset / limit, limit)),
          currentUser);
    }

    if (favorited != null) {
      return getPaginatedResponse(
          articles.findAllByFavoritedByOrderByIdDesc(
              users.findByName(favorited).get(), PageRequest.of(offset / limit, limit)),
          currentUser);
    }

    return getPaginatedResponse(
        articles.findAllByOrderByIdDesc(PageRequest.of(offset / limit, limit)), currentUser);
  }

  @Transactional(readOnly = true)
  public Tuple<List<ArticleDTO>, Long> feed(User currentUser, int offset, int limit) {
    return getPaginatedResponse(
        articles.findAllByAuthorFollowersOrderByIdDesc(
            currentUser, PageRequest.of(offset / limit, limit)),
        currentUser);
  }

  private Tuple<List<ArticleDTO>, Long> getPaginatedResponse(
      Page<Article> articles, User currentUser) {
    return new Tuple<>(
        articles.map(a -> new ArticleDTO(a, currentUser)).getContent(),
        articles.getTotalElements());
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
  public Optional<ArticleDTO> favorite(String slug, User currentUser) {
    return findBySlug(slug)
        .map(
            article -> {
              article.getFavoritedBy().add(currentUser);
              return new ArticleDTO(articles.saveAndFlush(article), currentUser);
            });
  }

  @Transactional
  public Optional<ArticleDTO> unfavorite(String slug, User currentUser) {
    return findBySlug(slug)
        .map(
            article -> {
              article.getFavoritedBy().remove(currentUser);
              return new ArticleDTO(articles.saveAndFlush(article), currentUser);
            });
  }
}
