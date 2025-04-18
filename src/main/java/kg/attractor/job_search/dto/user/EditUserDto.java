package kg.attractor.job_search.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @NotBlank @Pattern(regexp = "^\\+?\\d+$", message = "Номер телефона должен содержать только + и цифры")
    private String phoneNumber;
}