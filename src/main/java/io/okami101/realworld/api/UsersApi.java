package io.okami101.realworld.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.application.data.UserDTO;
import io.okami101.realworld.application.data.UserResponse;
import io.okami101.realworld.application.user.NewUserRequest;
import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;

@RestController
public class UsersApi {
    private UserRepository userRepository;
    private PasswordHashService passwordHashService;
    private JwtService jwtService;

    @Autowired
    public UsersApi(UserRepository userRepository, PasswordHashService passwordHashService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordHashService = passwordHashService;
        this.jwtService = jwtService;
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getUser().getEmail());
        user.setName(request.getUser().getUsername());
        user.setPassword(passwordHashService.hash(request.getUser().getPassword()));

        userRepository.save(user);

        return ResponseEntity.status(201).body(new UserResponse(new UserDTO(user, jwtService.encode(user))));
    }
}
