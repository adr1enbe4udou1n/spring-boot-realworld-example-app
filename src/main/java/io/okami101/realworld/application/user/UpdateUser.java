package io.okami101.realworld.application.user;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
    @Email
    @DuplicatedEmailConstraint
    private String email;

    private String username;

    private String bio;

    private String image;
}
