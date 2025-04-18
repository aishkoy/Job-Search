package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.*;
import kg.attractor.job_search.validation.UniqueEmail;
import kg.attractor.job_search.validation.ValidUserByRole;
import lombok.*;

@ValidUserByRole
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {
    @NotBlank(message = "Имя/название компании не может быть пустым")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s-]+$",
            message = "Имя/название компании может содержать только буквы, пробелы и дефисы")
    private String name;

    private String surname;

    private Integer age;

    @Email
    @NotBlank
    @UniqueEmail
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Длина пароля должна быть 8-20 символов")
    @Pattern(
            regexp = "^(?=.*[A-Za-zА-Яа-я])(?=.*\\d)[A-Za-zА-Яа-я\\d@#$%^&+=!]{8,}$",
            message = "Пароль должен содержать хотя бы одну букву (русскую или латинскую) и одну цифру"
    )
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d+$", message = "Номер телефона должен содержать только + и цифры")
    private String phoneNumber;

    @NotNull
    private Long roleId;
}