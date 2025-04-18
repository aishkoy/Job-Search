package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.enums.ContactType;
import kg.attractor.job_search.validation.ValidContactValue;
import org.springframework.beans.BeanWrapperImpl;

public class ContactValueValidator implements ConstraintValidator<ValidContactValue, Object> {
    private String typeIdField;
    private String contactValueField;

    @Override
    public void initialize(ValidContactValue constraintAnnotation) {
        this.typeIdField = constraintAnnotation.typeIdField();
        this.contactValueField = constraintAnnotation.contactValueField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Long typeId = (Long) beanWrapper.getPropertyValue(typeIdField);
        String contactValue = (String) beanWrapper.getPropertyValue(contactValueField);

        if(typeId == null || contactValue == null || contactValue.isEmpty()) {
            return true;
        }

        ContactType type = ContactType.fromId(typeId);

        if(type == null) {
            return true;
        }

        boolean isValid = type.validate(contactValue);
        if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(type.getErrorMessage())
                    .addPropertyNode(contactValueField)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
