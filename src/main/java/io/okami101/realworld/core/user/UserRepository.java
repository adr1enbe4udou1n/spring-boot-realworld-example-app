package io.okami101.realworld.core.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("select u from User u left join fetch u.followers where u.id = ?1")
  Optional<User> findById(Long id);

  @Query("select u from User u left join fetch u.followers where u.name = ?1")
  Optional<User> findByName(String username);

  Optional<User> findByEmail(String email);
}
