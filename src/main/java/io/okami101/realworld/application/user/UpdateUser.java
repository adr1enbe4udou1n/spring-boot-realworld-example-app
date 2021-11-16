package io.okami101.realworld.application.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
    @NotEmpty
    @Email
    @DuplicatedEmailConstraint
    private String email;

    @NotEmpty
    private String username;

    private String bio;

    private String image;
}
