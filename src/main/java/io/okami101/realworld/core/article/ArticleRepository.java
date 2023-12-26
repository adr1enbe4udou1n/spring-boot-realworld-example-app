package io.okami101.realworld.core.article;

import io.okami101.realworld.core.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findByTitle(String title);

  @EntityGraph(attributePaths = {"author", "tags", "favoritedBy"})
  Optional<Article> findBySlug(String slug);

  Page<Article> findAllByOrderByIdDesc(Pageable pageable);

  Page<Article> findAllByFavoritedByOrderByIdDesc(User user, Pageable pageable);

  Page<Article> findAllByAuthorNameOrderByIdDesc(String author, Pageable pageable);

  Page<Article> findAllByTagsOrderByIdDesc(Tag tag, Pageable pageable);

  Page<Article> findAllByAuthorFollowersOrderByIdDesc(User user, Pageable pageable);

  boolean existsBySlug(String value);
}
