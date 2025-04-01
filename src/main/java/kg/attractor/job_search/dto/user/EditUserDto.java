package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditUserDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$",
            message = "Имя может содержать только буквы и дефисы")
    private String name;
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$",
            message = "Фамилия может содержать только буквы и дефисы")
    private String surname;
    @NotNull
    @Min(18)
    private Integer age;
    @NotBlank @Pattern(regexp = "^\\+?\\d+$", message = "Номер телефона должен содержать только + и цифры")
    private String phoneNumber;
    private String avatar;
}