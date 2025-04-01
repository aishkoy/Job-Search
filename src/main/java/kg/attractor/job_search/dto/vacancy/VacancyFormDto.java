package kg.attractor.job_search.dto.vacancy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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
}
