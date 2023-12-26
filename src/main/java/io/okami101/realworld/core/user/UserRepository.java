package io.okami101.realworld.core.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"followers"})
  Optional<User> findById(Long id);

  @EntityGraph(attributePaths = {"followers"})
  Optional<User> findByName(String username);

  Optional<User> findByEmail(String email);
}
