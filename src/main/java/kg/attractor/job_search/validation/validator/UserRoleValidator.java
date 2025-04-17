package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.validation.ValidUserByRole;

public class UserRoleValidator implements ConstraintValidator<ValidUserByRole, CreateUserDto> {

    @Override
    public boolean isValid(CreateUserDto dto, ConstraintValidatorContext context) {
        if (dto.getRole().getId() == null) {
            return false;
        }

        context.disableDefaultConstraintViolation();

        return switch (dto.getRole().getId().intValue()) {
            case 1 -> validateEmployer(dto, context);
            case 2 -> validateApplicant(dto, context);
            default -> {
                addViolation(context, "Неизвестный тип роли", "roleId");
                yield false;
            }
        };
    }

    private boolean validateEmployer(CreateUserDto dto, ConstraintValidatorContext context) {
        if (dto.getAge() != null) {
            addViolation(context, "Возраст не должен указываться для работодателя", "age");
            return false;
        }
        if (isBlank(dto.getName())) {
            addViolation(context, "Название компании обязательно", "name");
            return false;
        }
        return true;
    }

    private boolean validateApplicant(CreateUserDto dto, ConstraintValidatorContext context) {
        if (dto.getAge() == null) {
            addViolation(context, "Возраст обязателен для соискателя", "age");
            return false;
        }
        if (dto.getAge() < 18 || dto.getAge() > 100) {
            addViolation(context, "Возраст должен быть в диапазоне 18-100 лет", "age");
            return false;
        }

        if (isBlank(dto.getSurname())) {
            addViolation(context, "Фамилия обязательна для соискателя", "surname");
            return false;
        }

        if (!dto.getSurname().matches("^[A-Za-zА-Яа-яЁё-]+$")) {
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
