package io.okami101.realworld.core.article;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Optional<Comment> findById(Long id);

  @Query(
      "select c from Comment c left join fetch c.author a left join fetch a.followers where c.article = ?1 order by c.id desc")
  List<Comment> findAllByArticleOrderByIdDesc(Article article);
}
