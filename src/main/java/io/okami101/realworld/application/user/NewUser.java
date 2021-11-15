package io.okami101.realworld.application.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

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
    @Min(8)
    private String password;
}
