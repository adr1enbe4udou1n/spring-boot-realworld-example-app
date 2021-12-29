package io.okami101.realworld.core.article;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findByTitle(String title);

  @Query(
      "select a from Article a left join fetch a.favoritedBy left join fetch a.tags left join fetch a.author aa left join fetch aa.followers where a.slug = ?1")
  Optional<Article> findBySlug(String name);

  boolean existsBySlug(String value);
}
