package io.okami101.realworld.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.api.exception.ResourceNotFoundException;
import io.okami101.realworld.application.user.ProfileDTO;
import io.okami101.realworld.application.user.ProfileResponse;
import io.okami101.realworld.application.user.UserService;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Profiles")
@RequestMapping(path = "/profiles/celeb_{username}")
public class ProfilesApi {

    @Autowired
    private UserService service;

    @GetMapping
    @Operation(summary = "Get profile", description = "Get a profile of a user of the system. Auth is optional")
    @Parameter(name = "username", description = "Get a profile of a user of the system. Auth is optional")
    public ProfileResponse get(@PathVariable("username") String username, @AuthenticationPrincipal User currentUser) {
        return service.findByName(username).map(user -> new ProfileResponse(new ProfileDTO(user, currentUser)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping(path = "/follow")
    @Operation(summary = "Follow a user", description = "Follow a user by username")
    @Parameter(name = "username", description = "Username of the profile you want to follow")
    @SecurityRequirement(name = "Bearer")
    public ProfileResponse follow(@PathVariable("username") String username,
            @AuthenticationPrincipal User currentUser) {
        return service.findByName(username).map(user -> {
            service.follow(currentUser, user);

            return new ProfileResponse(new ProfileDTO(user, currentUser));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @DeleteMapping(path = "/follow")
    @Operation(summary = "Unfollow a user", description = "Unfollow a user by username")
    @Parameter(name = "username", description = "Username of the profile you want to unfollow")
    @SecurityRequirement(name = "Bearer")
    public ProfileResponse unfollow(@PathVariable("username") String username,
            @AuthenticationPrincipal User currentUser) {
        return service.findByName(username).map(user -> {
            service.unfollow(currentUser, user);

            return new ProfileResponse(new ProfileDTO(user, currentUser));
        }).orElseThrow(ResourceNotFoundException::new);
    }
}
