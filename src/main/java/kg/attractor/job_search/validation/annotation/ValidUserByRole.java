package kg.attractor.job_search.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.job_search.validation.validator.UserRoleValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRoleValidator.class)
public @interface ValidUserByRole {
    String message() default "{validation.user.role.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}