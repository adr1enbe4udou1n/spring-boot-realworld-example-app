package io.okami101.realworld.core.article;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    Optional<Comment> findById(Long id);

    Optional<Comment> findByName(String name);
}
