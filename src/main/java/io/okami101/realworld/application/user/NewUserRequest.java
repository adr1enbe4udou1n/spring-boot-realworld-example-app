package io.okami101.realworld.application.user;

import javax.validation.Valid;

import lombok.Getter;

@Getter
public class NewUserRequest {
    @Valid
    private NewUser user;
}
