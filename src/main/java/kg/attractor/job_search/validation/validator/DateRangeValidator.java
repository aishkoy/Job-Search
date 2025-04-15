package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.validation.ValidDateRange;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    private String startDateField;
    private String endDateField;
    private String message;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        startDateField = constraintAnnotation.startDateField();
        endDateField = constraintAnnotation.endDateField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        Date startDate = (Date) wrapper.getPropertyValue(startDateField);
        Date endDate = (Date) wrapper.getPropertyValue(endDateField);

        if (startDate == null || endDate == null) {
            return true;
        }

        boolean isValid = startDate.before(endDate);

        if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(endDateField)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
