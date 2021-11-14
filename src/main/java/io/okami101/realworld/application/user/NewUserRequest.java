package io.okami101.realworld.application.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class NewUserRequest {
    @Valid
    @NotNull
    private NewUser user;
}
