package io.okami101.realworld.application.data;

import io.okami101.realworld.core.user.User;
import lombok.Getter;

@Getter
public class UserDTO {
    private String email;
    private String username;
    private String bio;
    private String image;
    private String token;

    public UserDTO(User user, String token) {
        this.email = user.getEmail();
        this.username = user.getName();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.token = token;
    }
}
