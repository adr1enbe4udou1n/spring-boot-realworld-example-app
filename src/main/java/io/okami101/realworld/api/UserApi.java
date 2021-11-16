package io.okami101.realworld.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.application.user.UpdateUserRequest;
import io.okami101.realworld.application.user.UserDTO;
import io.okami101.realworld.application.user.UserResponse;
import io.okami101.realworld.application.user.UserService;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User and Authentication")
@RequestMapping(path = "/user")
public class UserApi extends ApiController {
    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Get current user", description = "Gets the currently logged-in user")
    @GetMapping
    public UserResponse current(@AuthenticationPrincipal User currentUser) {
        return new UserResponse(new UserDTO(currentUser, jwtService.encode(currentUser)));
    }

    @Operation(summary = "Update current user", description = "Updated user information for current user")
    @PutMapping
    public UserResponse update(@AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateUserRequest request) {
        service.update(currentUser, request.getUser());

        return new UserResponse(new UserDTO(currentUser, jwtService.encode(currentUser)));
    }
}
