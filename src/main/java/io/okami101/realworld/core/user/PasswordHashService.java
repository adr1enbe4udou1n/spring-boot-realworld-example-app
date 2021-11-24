package io.okami101.realworld.core.user;

public interface PasswordHashService {
  String hash(String password);

  boolean check(String checkPassword, String realPassword);
}
