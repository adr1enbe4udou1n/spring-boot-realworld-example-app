package io.okami101.realworld.application.article;

import io.okami101.realworld.api.exception.ForbiddenException;
import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.article.Tag;
import io.okami101.realworld.core.article.TagRepository;
import io.okami101.realworld.core.service.SlugService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticlesService {

  @Autowired private ArticleRepository articles;

  @Autowired private TagRepository tags;

  @Autowired private UserRepository users;

  @Autowired private SlugService slugService;

  @Transactional(readOnly = true)
  public Optional<Article> findBySlug(String slug) {
    return articles.findBySlug(slug);
  }

  @Transactional(readOnly = true)
  public Page<Article> list(
      String author, String tag, String favorited, User currentUser, int offset, int limit) {

    if (author != null) {
      return articles.findAllByAuthorNameOrderByIdDesc(
          author, PageRequest.of(offset / limit, limit));
    }

    if (tag != null) {
      return articles.findAllByTagsOrderByIdDesc(
          tags.findByName(tag).get(), PageRequest.of(offset / limit, limit));
    }

    if (favorited != null) {
      return articles.findAllByFavoritedByOrderByIdDesc(
          users.findByName(favorited).get(), PageRequest.of(offset / limit, limit));
    }

    return articles.findAllByOrderByIdDesc(PageRequest.of(offset / limit, limit));
  }

  @Transactional(readOnly = true)
  public Page<Article> feed(User currentUser, int offset, int limit) {
    return articles.findAllByAuthorFollowersOrderByIdDesc(
        currentUser, PageRequest.of(offset / limit, limit));
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
