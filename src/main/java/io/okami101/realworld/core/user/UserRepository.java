package io.okami101.realworld.core.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findById(Long id);

  Optional<User> findByName(String username);

  Optional<User> findByEmail(String email);
}
