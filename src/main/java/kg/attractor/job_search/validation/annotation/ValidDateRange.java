package kg.attractor.job_search.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.job_search.validation.validator.DateRangeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "{validation.date.range}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String startDateField() default "startDate";
    String endDateField() default "endDate";
}
