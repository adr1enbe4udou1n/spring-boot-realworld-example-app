package io.okami101.realworld.application.user;

import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class DuplicatedEmailValidator
    implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  private final UserRepository userRepository;

  public DuplicatedEmailValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return userRepository
        .findByEmail(value)
        .map(
            u -> {
              User user = null;
              Object principal =
                  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

              if (principal instanceof User) {
                user = (User) principal;
              }

              return user != null && u.getId().equals(user.getId());
            })
        .orElse(true);
  }
}
