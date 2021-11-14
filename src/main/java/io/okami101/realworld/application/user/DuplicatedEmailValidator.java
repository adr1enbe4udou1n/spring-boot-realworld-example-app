package io.okami101.realworld.application.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import io.okami101.realworld.core.user.User;
import io.okami101.realworld.core.user.UserRepository;

public class DuplicatedEmailValidator implements ConstraintValidator<DuplicatedEmailConstraint, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(value).map(u -> u.getId() == user.getId()).orElse(true);
    }
}
