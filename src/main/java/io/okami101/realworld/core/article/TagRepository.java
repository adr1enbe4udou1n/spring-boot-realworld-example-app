package io.okami101.realworld.core.article;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findAllByOrderByNameAsc();

  Optional<Tag> findByName(String name);
}
