package io.okami101.realworld.application.user;

import io.okami101.realworld.core.user.User;
import lombok.Getter;

@Getter
public class ProfileDTO {
  private String username;
  private String bio;
  private String image;
  private Boolean following;

  public ProfileDTO(User user, User currentUser) {
    this.username = user.getName();
    this.bio = user.getBio();
    this.image = user.getImage();
    this.following = user.getFollowers().contains(currentUser);
  }
}
