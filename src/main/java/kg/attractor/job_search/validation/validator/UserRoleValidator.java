package kg.attractor.job_search.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.validation.ValidUserByRole;

public class UserRoleValidator implements ConstraintValidator<ValidUserByRole, CreateUserDto> {

    @Override
    public boolean isValid(CreateUserDto dto, ConstraintValidatorContext context) {
        if (dto.getRole() == null || dto.getRole().getId() == null) {
            return false;
        }

        context.disableDefaultConstraintViolation();
        boolean isValid;

        switch (dto.getRole().getId().intValue()) {
            case 1 -> isValid = validateEmployer(dto, context);
            case 2 -> isValid = validateApplicant(dto, context);
            default -> {
                addViolation(context, "Неизвестный тип роли", "role.id");
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean validateEmployer(CreateUserDto dto, ConstraintValidatorContext context) {
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

    private boolean validateApplicant(CreateUserDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (dto.getAge() == null) {
            addViolation(context, "Возраст обязателен для соискателя", "age");
            isValid = false;
        } else if (dto.getAge() < 18 || dto.getAge() > 100) {
            addViolation(context, "Возраст должен быть в диапазоне 18-100 лет", "age");
            isValid = false;
        }

        if (dto.getSurname() == null || dto.getSurname().isBlank()) {
            addViolation(context, "Фамилия обязательна для соискателя", "surname");
            isValid = false;
        } else if (!dto.getSurname().matches("^[A-Za-zА-Яа-яЁё-]+$")) {
            addViolation(context, "Фамилия может содержать только буквы и дефисы", "surname");
            isValid = false;
        }

        return isValid;
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