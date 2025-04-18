package kg.attractor.job_search.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.attractor.job_search.validation.validator.ExperienceRangeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ExperienceRangeValidator.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExperienceRange {
    String message() default "Минимальный опыт работы должен быть меньше максимального";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String expFromField() default "expFrom";
    String expToField() default "expTo";
}
