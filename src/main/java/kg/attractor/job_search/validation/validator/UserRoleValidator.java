package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.validation.ValidUserByRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.regex.Pattern;

public class UserRoleValidator implements ConstraintValidator<ValidUserByRole, SimpleUserDto> {
    private static final int EMPLOYER_ROLE_ID = 1;
    private static final int APPLICANT_ROLE_ID = 2;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 100;
    private static final Pattern SURNAME_PATTERN = Pattern.compile("^[A-Za-zА-Яа-яЁё-]+$");

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean isValid(SimpleUserDto dto, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (dto.getRole() == null || dto.getRole().id() == null) {
            addViolation(context, "validation.user.role.required", "role.id");
            return false;
        }

        return switch (dto.getRole().id().intValue()) {
            case EMPLOYER_ROLE_ID -> validateEmployer(dto, context);
            case APPLICANT_ROLE_ID -> validateApplicant(dto, context);
            default -> {
                addViolation(context, "validation.user.role.unknown", "role.id");
                yield false;
            }
        };
    }

    private boolean validateEmployer(SimpleUserDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (dto.getAge() != null) {
            addViolation(context, "validation.employer.age.notallowed", "age");
            isValid = false;
        }

        if (isBlank(dto.getName())) {
            addViolation(context, "validation.employer.name.required", "name");
            isValid = false;
        }

        return isValid;
    }

    private boolean validateApplicant(SimpleUserDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        isValid &= validateAge(dto, context);

        isValid &= validateSurname(dto, context);

        return isValid;
    }

    private boolean validateAge(SimpleUserDto dto, ConstraintValidatorContext context) {
        if (dto.getAge() == null) {
            addViolation(context, "validation.applicant.age.required", "age");
            return false;
        }

        if (dto.getAge() < MIN_AGE || dto.getAge() > MAX_AGE) {
            addViolation(context,
                    "validation.applicant.age.range",
                    "age",
                    new Object[]{MIN_AGE, MAX_AGE}
            );
            return false;
        }

        return true;
    }

    private boolean validateSurname(SimpleUserDto dto, ConstraintValidatorContext context) {
        if (isBlank(dto.getSurname())) {
            addViolation(context, "validation.applicant.surname.required", "surname");
            return false;
        }

        if (!SURNAME_PATTERN.matcher(dto.getSurname()).matches()) {
            addViolation(context, "validation.applicant.surname.pattern", "surname");
            return false;
        }

        return true;
    }

    private void addViolation(ConstraintValidatorContext context, String messageKey, String property) {
        addViolation(context, messageKey, property, null);
    }

    private void addViolation(ConstraintValidatorContext context, String messageKey, String property, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(messageKey, args, locale);

        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}