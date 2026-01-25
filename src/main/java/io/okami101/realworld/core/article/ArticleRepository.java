package io.okami101.realworld.core.article;

import io.okami101.realworld.core.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findByTitle(String title);

  @EntityGraph(attributePaths = {"author", "tags", "favoritedBy"})
  Optional<Article> findBySlug(String slug);

  @EntityGraph(attributePaths = {"author"})
  Page<Article> findAllByOrderByIdDesc(Pageable pageable);

  @EntityGraph(attributePaths = {"author"})
  @Query("SELECT a FROM Article a WHERE :user MEMBER OF a.favoritedBy ORDER BY a.id DESC")
  Page<Article> findAllByFavoritedByOrderByIdDesc(@Param("user") User user, Pageable pageable);

  @EntityGraph(attributePaths = {"author"})
  Page<Article> findAllByAuthorNameOrderByIdDesc(String author, Pageable pageable);

  @EntityGraph(attributePaths = {"author"})
  @Query("SELECT a FROM Article a WHERE :tag MEMBER OF a.tags ORDER BY a.id DESC")
  Page<Article> findAllByTagsOrderByIdDesc(@Param("tag") Tag tag, Pageable pageable);

  @EntityGraph(attributePaths = {"author"})
  Page<Article> findAllByAuthorFollowersOrderByIdDesc(User user, Pageable pageable);

  boolean existsBySlug(String value);
}
