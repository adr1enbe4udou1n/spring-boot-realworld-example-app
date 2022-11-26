package io.okami101.realworld.application.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
  @NotBlank @Email @DuplicatedEmailConstraint private String email;

  @NotBlank private String username;

  @NotBlank
  @Size(min = 8)
  private String password;
}
