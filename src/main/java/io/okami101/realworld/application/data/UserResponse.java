package io.okami101.realworld.application.data;

import lombok.Getter;

@Getter
public class UserResponse {
    private UserDTO user;

    public UserResponse(UserDTO user) {
        this.user = user;
    }
}
