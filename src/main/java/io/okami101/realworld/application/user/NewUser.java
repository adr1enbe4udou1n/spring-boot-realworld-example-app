package io.okami101.realworld.application.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
    @NotBlank
    @Email
    @DuplicatedEmailConstraint
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
