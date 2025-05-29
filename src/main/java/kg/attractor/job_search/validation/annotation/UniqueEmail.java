package kg.attractor.job_search.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.job_search.validation.validator.UniqueEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "{validation.email.unique}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
