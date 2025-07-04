package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.*;
import kg.attractor.job_search.validation.annotation.UniqueEmail;
import kg.attractor.job_search.validation.annotation.ValidUserByRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@ValidUserByRole
public class CreateUserDto extends UserDto {
    @Email
    @NotBlank
    @UniqueEmail
    String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "{validation.password.size}")
    @Pattern(
            regexp = "^(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d)[A-Za-zА-Яа-я\\d@#$%^&+=!]{8,}$",
            message = "{validation.password.pattern}"
    )
    String password;
}