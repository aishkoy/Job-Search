package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.*;
import kg.attractor.job_search.dto.RoleDto;
import kg.attractor.job_search.validation.annotation.ValidUserByRole;
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
public class UserDto {
    Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$", message = "{validation.name.pattern}")
    String name;

    String surname;

    Integer age;

    String email;

    String password;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d+$", message = "{validation.phone.pattern}")
    String phoneNumber;

    @Builder.Default
    String avatar = null;

    @Builder.Default
    Boolean enabled = true;

    @NotNull
    RoleDto role;

    @Builder.Default
    String resetPasswordToken = null;

    @NotBlank
    @Builder.Default
    String preferredLanguage = "ru";

    Integer resumesCount;
    Integer vacanciesCount;
}