package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.validation.ValidUserByRole;

import java.util.regex.Pattern;

public class UserRoleValidator implements ConstraintValidator<ValidUserByRole, SimpleUserDto> {
    private static final int EMPLOYER_ROLE_ID = 1;
    private static final int APPLICANT_ROLE_ID = 2;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 100;
    private static final Pattern SURNAME_PATTERN = Pattern.compile("^[A-Za-zА-Яа-яЁё-]+$");

    @Override
    public boolean isValid(SimpleUserDto dto, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (dto.getRole() == null || dto.getRole().getId() == null) {
            addViolation(context, "Роль пользователя не указана", "role.id");
            return false;
        }

        return switch (dto.getRole().getId().intValue()) {
            case EMPLOYER_ROLE_ID -> validateEmployer(dto, context);
            case APPLICANT_ROLE_ID -> validateApplicant(dto, context);
            default -> {
                addViolation(context, "Неизвестный тип роли", "role.id");
                yield false;
            }
        };
    }

    private boolean validateEmployer(SimpleUserDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (dto.getAge() != null) {
            addViolation(context, "Возраст не должен указываться для работодателя", "age");
            isValid = false;
        }

        if (isBlank(dto.getName())) {
            addViolation(context, "Название компании обязательно", "name");
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
            addViolation(context, "Возраст обязателен для соискателя", "age");
            return false;
        }

        if (dto.getAge() < MIN_AGE || dto.getAge() > MAX_AGE) {
            addViolation(context,
                    "Возраст должен быть в диапазоне " + MIN_AGE + "-" + MAX_AGE + " лет",
                    "age"
            );
            return false;
        }

        return true;
    }

    private boolean validateSurname(SimpleUserDto dto, ConstraintValidatorContext context) {
        if (isBlank(dto.getSurname())) {
            addViolation(context, "Фамилия обязательна для соискателя", "surname");
            return false;
        }

        if (!SURNAME_PATTERN.matcher(dto.getSurname()).matches()) {
            addViolation(context, "Фамилия может содержать только буквы и дефисы", "surname");
            return false;
        }

        return true;
    }

    private void addViolation(ConstraintValidatorContext context, String message, String property) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}