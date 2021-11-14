package io.okami101.realworld.api;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.okami101.realworld.api.exception.InvalidAuthenticationException;
import io.okami101.realworld.application.data.UserDTO;
import io.okami101.realworld.application.data.UserResponse;
import io.okami101.realworld.application.user.LoginUserRequest;
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
    public UserResponse register(@Valid @RequestBody NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getUser().getEmail());
        user.setName(request.getUser().getUsername());
        user.setPassword(passwordHashService.hash(request.getUser().getPassword()));

        userRepository.save(user);

        return new UserResponse(new UserDTO(user, jwtService.encode(user)));
    }

    @RequestMapping(path = "/users/login", method = RequestMethod.POST)
    public UserResponse register(@Valid @RequestBody LoginUserRequest request) {
        Optional<User> optional = userRepository.findByEmail(request.getUser().getEmail());
        if (!optional.isPresent()
                || !passwordHashService.check(request.getUser().getPassword(), optional.get().getPassword())) {

            throw new InvalidAuthenticationException();
        }

        return new UserResponse(new UserDTO(optional.get(), jwtService.encode(optional.get())));
    }
}
