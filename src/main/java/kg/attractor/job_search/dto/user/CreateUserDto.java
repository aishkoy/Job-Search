package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateUserDto {
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$",
            message = "Имя может содержать только буквы и дефисы")
    private String name;
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$",
            message = "Фамилия может содержать только буквы и дефисы")
    private String surname;
    @NotNull @Min(18)
    private Integer age;
    @Email @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Длина пароля должна быть 8-20 символов")
    @Pattern(
            regexp = "^(?=.*[A-Za-zА-Яа-я])(?=.*\\d)[A-Za-zА-Яа-я\\d@#$%^&+=!]{8,}$",
            message = "Пароль должен содержать хотя бы одну букву (русскую или латинскую) и одну цифру"
    )
    private String password;

    @NotBlank @Pattern(regexp = "^\\+?\\d+$", message = "Номер телефона должен содержать только + и цифры")
    private String phoneNumber;

    @NotNull
    private Long roleId;
}
