package io.okami101.realworld.api.security;

import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
  private final UserRepository userRepository;
  private final JwtService jwtService;

  public JwtTokenFilter(UserRepository userRepository, JwtService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  private String header = "Authorization";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    getTokenString(request.getHeader(header))
        .ifPresent(
            token ->
                jwtService
                    .parse(token)
                    .ifPresent(
                        id -> {
                          if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            userRepository
                                .findById(Long.parseLong(id))
                                .ifPresent(
                                    user -> {
                                      UsernamePasswordAuthenticationToken authenticationToken =
                                          new UsernamePasswordAuthenticationToken(
                                              user, null, Collections.emptyList());
                                      authenticationToken.setDetails(
                                          new WebAuthenticationDetailsSource()
                                              .buildDetails(request));
                                      SecurityContextHolder.getContext()
                                          .setAuthentication(authenticationToken);
                                    });
                          }
                        }));

    filterChain.doFilter(request, response);
  }

  private Optional<String> getTokenString(String header) {
    if (header == null) {
      return Optional.empty();
    }

    String[] split = header.split(" ");
    if (split.length < 2) {
      return Optional.empty();
    }
    return Optional.ofNullable(split[1]);
  }
}
