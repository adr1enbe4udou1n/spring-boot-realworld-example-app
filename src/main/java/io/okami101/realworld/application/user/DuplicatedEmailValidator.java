package io.okami101.realworld.application.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import io.okami101.realworld.core.user.UserRepository;

public class DuplicatedEmailValidator implements ConstraintValidator<DuplicatedEmailConstraint, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value == null || value.isEmpty()) || !userRepository.findByEmail(value).isPresent();
    }
}
