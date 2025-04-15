package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.validation.ValidExperienceRange;
import org.springframework.beans.BeanWrapperImpl;

public class ExperienceRangeValidator implements ConstraintValidator<ValidExperienceRange, Object> {
    private String expFromField;
    private String expToField;
    private String errorMessage;


    @Override
    public void initialize(ValidExperienceRange constraintAnnotation) {
        this.expFromField = constraintAnnotation.expFromField();
        this.expToField = constraintAnnotation.expToField();
        this.errorMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) return true;

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Integer expFrom = (Integer) beanWrapper.getPropertyValue(expFromField);
        Integer expTo = (Integer) beanWrapper.getPropertyValue(expToField);

        if(expFrom == null || expTo == null) return true;

        boolean isValid = expFrom < expTo;

        if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addPropertyNode(expToField)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
