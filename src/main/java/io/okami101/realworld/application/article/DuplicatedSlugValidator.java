package io.okami101.realworld.application.article;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import io.okami101.realworld.core.article.ArticleRepository;
import io.okami101.realworld.core.service.SlugService;

public class DuplicatedSlugValidator implements ConstraintValidator<DuplicatedSlugConstraint, String> {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SlugService slugService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !articleRepository.existsBySlug(slugService.generate(value));
    }
}
