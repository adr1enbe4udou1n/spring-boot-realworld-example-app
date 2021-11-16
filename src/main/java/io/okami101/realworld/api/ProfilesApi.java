package io.okami101.realworld.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.api.exception.ResourceNotFoundException;
import io.okami101.realworld.application.user.ProfileDTO;
import io.okami101.realworld.application.user.ProfileResponse;
import io.okami101.realworld.application.user.UserService;
import io.okami101.realworld.core.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Profiles")
@RequestMapping(path = "/profiles/celeb_{username}")
public class ProfilesApi {

    @Autowired
    private UserService service;

    @GetMapping
    public ProfileResponse get(@PathVariable("username") String username, @AuthenticationPrincipal User currentUser) {
        return service.findByName(username).map(user -> new ProfileResponse(new ProfileDTO(user, false)))
                .orElseThrow(ResourceNotFoundException::new);
    }
}
