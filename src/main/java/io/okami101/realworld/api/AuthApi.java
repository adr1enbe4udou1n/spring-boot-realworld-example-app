package io.okami101.realworld.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.api.exception.InvalidAuthenticationException;
import io.okami101.realworld.application.user.LoginUserRequest;
import io.okami101.realworld.application.user.NewUserRequest;
import io.okami101.realworld.application.user.UserDTO;
import io.okami101.realworld.application.user.UserResponse;
import io.okami101.realworld.application.user.UserService;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User and Authentication")
@RequestMapping(path = "/users")
public class AuthApi extends ApiController {
    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Register a new user", description = "Register a new user")
    @PostMapping
    public UserResponse register(@Valid @RequestBody NewUserRequest request) {
        User user = service.create(request.getUser());

        return new UserResponse(new UserDTO(user, jwtService.encode(user)));
    }

    @Operation(summary = "Existing user login", description = "Login for existing user")
    @PostMapping(path = "/login")
    public UserResponse register(@Valid @RequestBody LoginUserRequest request) {
        User user = service.checkCredentials(request.getUser().getEmail(), request.getUser().getPassword());
        if (user == null) {

            throw new InvalidAuthenticationException();
        }

        return new UserResponse(new UserDTO(user, jwtService.encode(user)));
    }
}
