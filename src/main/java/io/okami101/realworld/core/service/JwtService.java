package io.okami101.realworld.core.service;

import io.okami101.realworld.core.user.User;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
  String encode(User user);

  Optional<String> parse(String token);
}
