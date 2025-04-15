package kg.attractor.job_search.dto.vacancy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import kg.attractor.job_search.validation.ValidExperienceRange;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ValidExperienceRange(message = "Минимальный опыт работы должен быть меньше максимального\"")
public class VacancyFormDto {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long categoryId;
    @NotNull @PositiveOrZero
    private Float salary;
    @NotNull @Positive
    private Integer expFrom;
    @NotNull @Positive
    private Integer expTo;
    @NotNull
    private Long authorId;
    private Boolean isActive;
}
