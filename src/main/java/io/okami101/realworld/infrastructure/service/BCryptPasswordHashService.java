package io.okami101.realworld.infrastructure.service;

import io.okami101.realworld.core.user.PasswordHashService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordHashService implements PasswordHashService {

  private PasswordEncoder encoder;

  public BCryptPasswordHashService() {
    this.encoder = new BCryptPasswordEncoder();
  }

  @Override
  public String hash(String password) {
    return encoder.encode(password);
  }

  @Override
  public boolean check(String checkPassword, String realPassword) {
    return encoder.matches(checkPassword, realPassword);
  }
}
