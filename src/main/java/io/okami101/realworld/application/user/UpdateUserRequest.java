package io.okami101.realworld.application.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UpdateUserRequest {
    @Valid
    @NotNull
    private UpdateUser user;
}
