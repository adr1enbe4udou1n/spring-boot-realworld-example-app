package io.okami101.realworld.api;

import java.util.Optional;

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
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User and Authentication")
public class UserApi extends ApiController {
    private UserRepository userRepository;
    private JwtService jwtService;

    @Autowired
    public UserApi(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Get current user", description = "Gets the currently logged-in user")
    @GetMapping(path = "/user")
    public UserResponse current(@AuthenticationPrincipal User currentUser) {
        return new UserResponse(new UserDTO(currentUser, jwtService.encode(currentUser)));
    }

    @Operation(summary = "Update current user", description = "Updated user information for current user")
    @PutMapping(path = "/user")
    public UserResponse update(@AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateUserRequest request) {
        Optional.ofNullable(request.getUser().getEmail()).ifPresent(currentUser::setEmail);
        Optional.ofNullable(request.getUser().getUsername()).ifPresent(currentUser::setName);
        Optional.ofNullable(request.getUser().getBio()).ifPresent(currentUser::setBio);
        Optional.ofNullable(request.getUser().getImage()).ifPresent(currentUser::setImage);

        userRepository.save(currentUser);

        return new UserResponse(new UserDTO(currentUser, jwtService.encode(currentUser)));
    }
}
