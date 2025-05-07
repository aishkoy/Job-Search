package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.*;
import kg.attractor.job_search.dto.RoleDto;
import kg.attractor.job_search.validation.ValidUserByRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)

@ValidUserByRole
public class SimpleUserDto {
    Long id;

    @NotNull
    RoleDto role;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$", message = "{validation.name.pattern}")
    String name;

    String surname;

    Integer age;

    String resetPasswordToken;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d+$", message = "{validation.phone.pattern}")
    String phoneNumber;
}