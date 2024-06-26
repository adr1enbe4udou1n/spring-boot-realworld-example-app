package io.okami101.realworld.api;

import io.okami101.realworld.api.exception.InvalidAuthenticationException;
import io.okami101.realworld.application.user.LoginUserRequest;
import io.okami101.realworld.application.user.NewUserRequest;
import io.okami101.realworld.application.user.UserResponse;
import io.okami101.realworld.application.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User and Authentication")
@RequestMapping(path = "/users")
public class AuthApi extends ApiController {
  private final UserService service;

  public AuthApi(UserService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(
      operationId = "CreateUser",
      summary = "Register a new user",
      description = "Register a new user")
  public UserResponse register(@Valid @RequestBody NewUserRequest request) {
    return new UserResponse(service.create(request.getUser()));
  }

  @PostMapping(path = "/login")
  @Operation(
      operationId = "Login",
      summary = "Existing user login",
      description = "Login for existing user")
  public UserResponse register(@Valid @RequestBody LoginUserRequest request) {
    return service
        .checkCredentials(request.getUser().getEmail(), request.getUser().getPassword())
        .map(user -> new UserResponse(service.getUserWithToken(user)))
        .orElseThrow(InvalidAuthenticationException::new);
  }
}
