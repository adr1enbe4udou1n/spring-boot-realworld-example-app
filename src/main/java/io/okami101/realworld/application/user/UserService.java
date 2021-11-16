package io.okami101.realworld.application.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHashService passwordHashService;

    public User create(NewUser userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getUsername());
        user.setPassword(passwordHashService.hash(userDto.getPassword()));

        return userRepository.save(user);
    }

    public User update(User currentUser, UpdateUser userDto) {
        Optional.ofNullable(userDto.getEmail()).ifPresent(currentUser::setEmail);
        Optional.ofNullable(userDto.getUsername()).ifPresent(currentUser::setName);
        Optional.ofNullable(userDto.getBio()).ifPresent(currentUser::setBio);
        Optional.ofNullable(userDto.getImage()).ifPresent(currentUser::setImage);

        return userRepository.save(currentUser);
    }

    public Optional<User> checkCredentials(String email, String password) {
        Optional<User> optional = userRepository.findByEmail(email);
        return optional.isPresent() && passwordHashService.check(password, optional.get().getPassword()) ? optional
                : Optional.empty();
    }

    public Optional<User> findByName(String username) {
        return userRepository.findByName(username);
    }
}
