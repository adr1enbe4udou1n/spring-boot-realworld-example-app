package io.okami101.realworld.application.user;

import lombok.Getter;

@Getter
public class UserResponse {
  private UserDTO user;

  public UserResponse(UserDTO user) {
    this.user = user;
  }
}
