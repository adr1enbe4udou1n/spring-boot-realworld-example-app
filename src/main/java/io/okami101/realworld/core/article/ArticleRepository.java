package io.okami101.realworld.core.article;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findByTitle(String title);

  Optional<Article> findBySlug(String slug);

  boolean existsBySlug(String value);
}
