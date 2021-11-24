package io.okami101.realworld.application.article;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicatedSlugValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedSlugConstraint {
  String message() default "duplicated slug";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
