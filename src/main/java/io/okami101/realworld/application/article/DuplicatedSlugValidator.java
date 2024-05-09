package io.okami101.realworld.application.article;

import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.service.SlugService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DuplicatedSlugValidator
    implements ConstraintValidator<DuplicatedSlugConstraint, String> {

  private final ArticleRepository articleRepository;
  private final SlugService slugService;

  public DuplicatedSlugValidator(ArticleRepository articleRepository, SlugService slugService) {
    this.articleRepository = articleRepository;
    this.slugService = slugService;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !articleRepository.existsBySlug(slugService.generate(value));
  }
}
