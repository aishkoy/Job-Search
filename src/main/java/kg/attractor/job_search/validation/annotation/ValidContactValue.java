package kg.attractor.job_search.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.job_search.validation.validator.ContactValueValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactValueValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContactValue {
    String message() default "{validation.contact.format}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String typeIdField() default "typeId";
    String contactValueField() default "contactValue";
}