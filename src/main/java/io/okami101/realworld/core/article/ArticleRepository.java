package io.okami101.realworld.core.article;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String name);

    boolean existsBySlug(String value);
}
