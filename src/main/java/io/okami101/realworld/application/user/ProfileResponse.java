package io.okami101.realworld.application.user;

import lombok.Getter;

@Getter
public class ProfileResponse {
  private ProfileDTO profile;

  public ProfileResponse(ProfileDTO profile) {
    this.profile = profile;
  }
}
