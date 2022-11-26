package io.okami101.realworld.application.article;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DuplicatedSlugValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedSlugConstraint {
  String message() default "duplicated slug";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
