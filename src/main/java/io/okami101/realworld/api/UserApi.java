package io.okami101.realworld.api;

import io.okami101.realworld.application.user.UpdateUserRequest;
import io.okami101.realworld.application.user.UserResponse;
import io.okami101.realworld.application.user.UserService;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User and Authentication")
@RequestMapping(path = "/user")
@SecurityRequirement(name = "Bearer")
public class UserApi extends ApiController {
  private final UserService service;

  public UserApi(UserService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(
      operationId = "GetCurrentUser",
      summary = "Get current user",
      description = "Gets the currently logged-in user")
  public UserResponse current(@AuthenticationPrincipal User currentUser) {
    return new UserResponse(service.getUserWithToken(currentUser));
  }

  @PutMapping
  @Operation(
      operationId = "UpdateCurrentUser",
      summary = "Update current user",
      description = "Updated user information for current user")
  public UserResponse update(
      @AuthenticationPrincipal User currentUser, @Valid @RequestBody UpdateUserRequest request) {
    return new UserResponse(service.update(currentUser, request.getUser()));
  }
}
