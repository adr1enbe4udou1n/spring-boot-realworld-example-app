package io.okami101.realworld.application.user;

import io.okami101.realworld.core.service.JwtService;
import io.okami101.realworld.core.user.PasswordHashService;
import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordHashService passwordHashService;
  private final JwtService jwtService;

  public UserService(
      UserRepository userRepository,
      PasswordHashService passwordHashService,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordHashService = passwordHashService;
    this.jwtService = jwtService;
  }

  @Transactional
  public UserDTO create(NewUser userDto) {
    User user = new User();
    user.setEmail(userDto.getEmail());
    user.setName(userDto.getUsername());
    user.setPassword(passwordHashService.hash(userDto.getPassword()));

    return getUserWithToken(userRepository.saveAndFlush(user));
  }

  @Transactional
  public UserDTO update(User currentUser, UpdateUser userDto) {
    Optional.ofNullable(userDto.getEmail()).ifPresent(currentUser::setEmail);
    Optional.ofNullable(userDto.getUsername()).ifPresent(currentUser::setName);
    Optional.ofNullable(userDto.getBio()).ifPresent(currentUser::setBio);
    Optional.ofNullable(userDto.getImage()).ifPresent(currentUser::setImage);

    return getUserWithToken(userRepository.saveAndFlush(currentUser));
  }

  public Optional<User> checkCredentials(String email, String password) {
    Optional<User> optional = userRepository.findByEmail(email);
    return optional.isPresent() && passwordHashService.check(password, optional.get().getPassword())
        ? optional
        : Optional.empty();
  }

  @Transactional(readOnly = true)
  public Optional<User> findByName(String username) {
    return userRepository.findByName(username);
  }

  public UserDTO getUserWithToken(User user) {
    return new UserDTO(user, jwtService.encode(user));
  }

  @Transactional
  public void follow(User source, User target) {
    target.getFollowers().add(source);
    userRepository.saveAndFlush(target);
  }

  @Transactional
  public void unfollow(User source, User target) {
    target.getFollowers().remove(source);
    userRepository.saveAndFlush(target);
  }
}
