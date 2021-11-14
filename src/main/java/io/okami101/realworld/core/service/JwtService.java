package io.okami101.realworld.core.service;

import org.springframework.stereotype.Service;

import io.okami101.realworld.core.user.User;

import java.util.Optional;

@Service
public interface JwtService {
    String encode(User user);

    Optional<String> parse(String token);
}
